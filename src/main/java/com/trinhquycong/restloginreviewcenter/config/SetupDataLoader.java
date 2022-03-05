package com.trinhquycong.restloginreviewcenter.config;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.trinhquycong.restloginreviewcenter.dto.SocialProvider;
import com.trinhquycong.restloginreviewcenter.model.Role;
import com.trinhquycong.restloginreviewcenter.model.User;
import com.trinhquycong.restloginreviewcenter.repo.RoleRepo;
import com.trinhquycong.restloginreviewcenter.repo.UserRepo;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;
	
	@Autowired
    private UserRepo userRepo;
 
    @Autowired
    private RoleRepo roleRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${trinhquycong.default.password}")
    private String adminPassword;
    
    @Value("${trinhquycong.default.email}")
    private String adminEmail;
    
    
    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        // Create initial roles
        createRoleIfNotFound(Role.ROLE_USER);
        createRoleIfNotFound(Role.ROLE_MODERATOR);
        Role adminRole = createRoleIfNotFound(Role.ROLE_ADMIN);
        
        createUserIfNotFound(Set.of(adminRole));
        alreadySetup = true;
        
        
    }
    
    @Transactional
    private final User createUserIfNotFound(Set<Role> roles) {
        User user = userRepo.findByEmail(this.adminEmail);
        if (user == null) {
            user = new User();
            user.setDisplayName("Trịnh Quý Công");
            user.setEmail(this.adminEmail);
            user.setPassword(passwordEncoder.encode(this.adminPassword)); // password 
            user.setRoles(roles);
            user.setProvider(SocialProvider.LOCAL.getProviderType());
            user.setEnabled(true);
            Date now = Calendar.getInstance().getTime();
            user.setCreatedDate(now);
            user.setModifiedDate(now);
            user = userRepo.save(user);
        }
        return user;
    }
 
    @Transactional
    private final Role createRoleIfNotFound(final String name) {
        Role role = roleRepo.findByName(name);
        if (role == null) {
            role = roleRepo.save(new Role(name));
        }
        return role;
    }

}
