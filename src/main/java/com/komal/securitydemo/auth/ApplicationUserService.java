package com.komal.securitydemo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService{

	private final ApplicationUserDao applicationUserDao;
	
	@Autowired
	public ApplicationUserService(@Qualifier("fake") ApplicationUserDao applicationUserDao) {
		super();
		this.applicationUserDao = applicationUserDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return applicationUserDao
				.selectApplicationUserByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException(String.format("Username %s not found", username)));
	}

}
