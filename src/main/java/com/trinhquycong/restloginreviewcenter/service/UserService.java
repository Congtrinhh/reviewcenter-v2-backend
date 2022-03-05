package com.trinhquycong.restloginreviewcenter.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import com.trinhquycong.restloginreviewcenter.dto.LocalUser;
import com.trinhquycong.restloginreviewcenter.dto.SignUpRequest;
import com.trinhquycong.restloginreviewcenter.dto.UserDto;
import com.trinhquycong.restloginreviewcenter.exception.UserAlreadyExistAuthenticationException;
import com.trinhquycong.restloginreviewcenter.model.User;

public interface UserService {
	
	public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;
	
	User findUserByEmail(String email);
	
	Optional<User> findUserById(Long id);
	
	LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
	
	String deleteById(Long id);
	
	UserDto toDto(User user);
	
	User toEntity(UserDto dto);
}
