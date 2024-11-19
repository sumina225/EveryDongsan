package com.dongsan.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberDetailsService {

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
