package com.spring.pidev.config;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.spring.pidev.model.Profession;
import com.spring.pidev.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.spring.pidev.dto.SocialProvider;
import com.spring.pidev.model.Role;
import com.spring.pidev.model.User;
import com.spring.pidev.repo.RoleRepository;
import com.spring.pidev.repo.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;




	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if (alreadySetup) {
		return;
		}
		// Create initial roles
		Role userRole = createRoleIfNotFound(Role.ROLE_USER);
		Role adminRole = createRoleIfNotFound(Role.ROLE_ADMIN);
	    Role modRole = createRoleIfNotFound(Role.ROLE_MODERATOR);
		createUserIfNotFound("admin@gmail.com", Set.of(adminRole,userRole));
		alreadySetup = true;
	}

	@Transactional
	User createUserIfNotFound(final String email, Set<Role> roles) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			user = new User();
			user.setDisplayName("Admin");
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode("admin@"));
		user.setRoles(roles);
			user.setProvider(SocialProvider.LOCAL.getProviderType());
			user.setEnabled(true);
			user.setProfession(Profession.ADMIN);
			Date now = Calendar.getInstance().getTime();
		user.setCreatedDate(now);
			user.setState(State.DISCIPLINED);
			user.setModifiedDate(now);
			user = userRepository.save(user);
		}
		return user;
	}

	@Transactional
	Role createRoleIfNotFound(final String name) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = roleRepository.save(new Role(name));
		}
		return role;
	}
}
