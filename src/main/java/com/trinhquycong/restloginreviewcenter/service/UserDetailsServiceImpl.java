package com.trinhquycong.restloginreviewcenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.trinhquycong.restloginreviewcenter.dto.LocalUser;
import com.trinhquycong.restloginreviewcenter.exception.ResourceNotFoundException;
import com.trinhquycong.restloginreviewcenter.model.Role;
import com.trinhquycong.restloginreviewcenter.model.User;
import com.trinhquycong.restloginreviewcenter.util.Constants;
import com.trinhquycong.restloginreviewcenter.util.GeneralUtils;

@Service("localUserDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
    private UserService userService;
	
	@Override
	@Transactional
	public LocalUser loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.findUserByEmail(email);
		if (user == null) {
            throw new UsernameNotFoundException("User " + email + " was not found in the database");
        }
		
		// if user has administrative role like ADMIN, MOD
		if (!GeneralUtils.containsRole(user.getRoles(), Role.ROLE_USER)) {
			if (!StringUtils.hasText(user.getAvatarUrl())) {
				user.setAvatarUrl(Constants.DEFAULT_AVATAR_URL_ADMIN);
			}			
		}
		
		return createLocalUser(user);
	}

	@Transactional
    public LocalUser loadUserById(Long id) {
        User user = userService.findUserById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return createLocalUser(user);
    }
 
    private LocalUser createLocalUser(User user) {
        return new LocalUser(user.getEmail(), user.getPassword(), user.getEnabled(), true, true, true, GeneralUtils.buildSimpleGrantedAuthorities(user.getRoles()), user);
    }
}
