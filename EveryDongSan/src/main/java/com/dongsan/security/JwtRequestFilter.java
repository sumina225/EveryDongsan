package com.dongsan.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dongsan.member.service.MemberDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MemberDetailsServiceImpl memberDetailsService;
    
    @Autowired
    private JwtTokenBlacklistService jwtTokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더에서 JWT 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값 추출
            try {
                username = jwtTokenUtil.extractUsername(token); // 토큰에서 사용자 이름 추출
            } catch (Exception e) {
                logger.error("JWT 토큰 처리 중 오류 발생: " + e.getMessage());
            }
        }
        
        if (token != null && jwtTokenBlacklistService.isTokenBlacklisted(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is blacklisted.");
            return; // 블랙리스트에 있으면 더 이상 처리하지 않음
        }

        // 2. SecurityContextHolder에 인증 정보 설정
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = memberDetailsService.loadUserByUsername(username);

            // 토큰 검증
            if (jwtTokenUtil.validateToken(token, userDetails.getUsername())) {
                List<String> roles = jwtTokenUtil.extractRoles(token);

                // "부동산" 역할 확인
                if (!roles.contains("부동산")) {
                    logger.error("필수 권한(부동산)이 없습니다.");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Insufficient permissions.");
                    return;
                }

                // 권한 변환
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 3. 다음 필터로 전달
        filterChain.doFilter(request, response);
    }
}
