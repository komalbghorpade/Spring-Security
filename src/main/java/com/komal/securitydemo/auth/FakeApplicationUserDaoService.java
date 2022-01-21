package com.komal.securitydemo.auth;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.komal.securitydemo.security.ApplicationUserRole;


@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao{

	private final PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
	
		this.passwordEncoder = passwordEncoder;
	}



	@Override
	public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
		// TODO Auto-generated method stub
		return getApplicationUsers()
				.stream()
				//.filter(ApplicationUser->username.equals(ApplicationUser.getUsername()))
				.filter(applicationUser ->username.equals(applicationUser.getUsername()))
				
				
				.findFirst();
	}

	private List<ApplicationUser> getApplicationUsers(){
		List<ApplicationUser> applicationUsers = Lists.newArrayList(
				
				new ApplicationUser("user1",
						passwordEncoder.encode("password"),
						ApplicationUserRole.STUDENT.getGrantedAuthorities(),
						true,true,true,true),
				
				new ApplicationUser(
						"user2",
						passwordEncoder.encode("password"),
						ApplicationUserRole.ADMIN.getGrantedAuthorities(),
						true,true,true,true),
				
				new ApplicationUser("user3",
						passwordEncoder.encode("password"),
						ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities(),
						true,true,true,true)
				);
		return applicationUsers;
	}
	
}
