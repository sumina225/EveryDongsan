package com.dongsan.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dongsan.member.entity.MemberEntity;
import com.dongsan.member.mapper.MemberMapper;

@Service
public class MemberDetailsServiceImpl implements MemberDetailsService{

	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberEntity member = memberMapper.findById(username);
		if(member == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		return User.builder()
				.username(member.getId())
				.password(member.getPw())
				.roles(member.getRole())
				.build();
	}
	
	public boolean checkPassword(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
