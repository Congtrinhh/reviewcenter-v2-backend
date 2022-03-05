package com.trinhquycong.restloginreviewcenter.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.trinhquycong.restloginreviewcenter.dto.LocalUser;
import com.trinhquycong.restloginreviewcenter.dto.SignUpRequest;
import com.trinhquycong.restloginreviewcenter.dto.SocialProvider;
import com.trinhquycong.restloginreviewcenter.dto.UserDto;
import com.trinhquycong.restloginreviewcenter.exception.OAuth2AuthenticationProcessingException;
import com.trinhquycong.restloginreviewcenter.exception.UserAlreadyExistAuthenticationException;
import com.trinhquycong.restloginreviewcenter.model.Role;
import com.trinhquycong.restloginreviewcenter.model.User;
import com.trinhquycong.restloginreviewcenter.repo.RoleRepo;
import com.trinhquycong.restloginreviewcenter.repo.UserRepo;
import com.trinhquycong.restloginreviewcenter.security.oauth2.user.OAuth2UserInfo;
import com.trinhquycong.restloginreviewcenter.security.oauth2.user.OAuth2UserInfoFactory;
import com.trinhquycong.restloginreviewcenter.util.Constants;
import com.trinhquycong.restloginreviewcenter.util.GeneralUtils;
import com.trinhquycong.restloginreviewcenter.security.oauth2.user.GithubOAuth2UserInfo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
    private UserRepo userRepo;
 
    @Autowired
    private RoleRepo roleRepo;
 
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @Override
    @Transactional(value = "transactionManager")
    public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        if (signUpRequest.getUserID() != null && userRepo.existsById(signUpRequest.getUserID())) {
            throw new UserAlreadyExistAuthenticationException("User with User id " + signUpRequest.getUserID() + " already exist");
        } else if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistAuthenticationException("User with email id " + signUpRequest.getEmail() + " already exist");
        }
        User user = buildUser(signUpRequest);
        Date now = Calendar.getInstance().getTime();
        user.setCreatedDate(now);
        user.setModifiedDate(now);
        user = userRepo.save(user);
        userRepo.flush();
        return user;
    }
 
    private User buildUser(final SignUpRequest formDTO) {
        User user = new User();
        user.setDisplayName(formDTO.getDisplayName());
        user.setEmail(formDTO.getEmail());
        user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
        final HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepo.findByName(Role.ROLE_USER));
        user.setRoles(roles);
        user.setProvider(
        		formDTO.getSocialProvider()!=null
        							?formDTO.getSocialProvider().getProviderType()
        							:SocialProvider.LOCAL.getProviderType()
        );
        user.setEnabled(true);
        user.setProviderUserId(formDTO.getProviderUserId());
        user.setAvatarUrl(formDTO.getAvatarUrl());
        return user;
    }	

    /**
     * for OAuth2 authentication
     */
    @SuppressWarnings("deprecation")
	@Override
    @Transactional
	public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken,
			OidcUserInfo userInfo) {
    	OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        if (StringUtils.isEmpty(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
if (oAuth2UserInfo instanceof GithubOAuth2UserInfo) {
        		throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider, please check github configuration and make your email public");
        	}
        	else {
        		throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider (khong tim thay email cua ban)");
        	}
        }
        SignUpRequest userDetails = toUserRegistrationObject(registrationId, oAuth2UserInfo);
        User user = findUserByEmail(oAuth2UserInfo.getEmail());
        if (user != null) {
            if (!user.getProvider().equals(registrationId) && !user.getProvider().equals(SocialProvider.LOCAL.getProviderType())) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with " + user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
            }else if (!GeneralUtils.containsRole(user.getRoles(), Role.ROLE_USER)) {
            	throw new OAuth2AuthenticationProcessingException("the account with email " + user.getEmail() + " already exists with administrative role");
            }
  
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(userDetails);
        }
 
        return LocalUser.create(user, attributes, idToken, userInfo);
	}
    
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setDisplayName(oAuth2UserInfo.getName());
        existingUser.setAvatarUrl(oAuth2UserInfo.getImageUrl());
        return userRepo.save(existingUser);
    }
 
    private SignUpRequest toUserRegistrationObject(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return SignUpRequest.getBuilder().addProviderUserID(oAuth2UserInfo.getId()).addDisplayName(oAuth2UserInfo.getName()).addEmail(oAuth2UserInfo.getEmail())
                .addSocialProvider(GeneralUtils.toSocialProvider(registrationId)).addPassword("123456").addAvatarUrl(oAuth2UserInfo.getImageUrl()).build();
    }
    
    @Override
    public UserDto toDto(User user) {
		UserDto dto = mapper.map(user, UserDto.class);
		
		Set<String> roleNames = getRoleNames(user);
		Set<Long> roleIds = getRoleIds(user);
		dto.setRoleNames(roleNames);
		dto.setRoleIds(roleIds);
		
		return dto;
	}
    
    private Set<String> getRoleNames(User user) {
		return user.getRoles()
				.stream().map(role -> role.getName())
				.collect(Collectors.toSet());
	}
	
	private Set<Long> getRoleIds(User user) {
		return user.getRoles().stream().map(role->role.getId()).collect(Collectors.toSet());
	}
    
	@Override
    public User toEntity(UserDto dto) {
		User user = mapper.map(dto, User.class);
		
		Set<Role> roles = dto.getRoleIds().stream().map(roleId->{
			Role role = roleRepo.getById(roleId);
			return role;
		}).collect(Collectors.toSet());
		
		if (roles==null || roles.size()==0) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "require at least one role to create user");
		}
		
		if (dto.getId()==null) {
			// create
			roles.forEach(role->{
				if (role.getName().equalsIgnoreCase(Role.ROLE_USER) ||
						role.getName().equalsIgnoreCase(Role.ROLE_ADMIN)) {
					throw new ResponseStatusException(HttpStatus.CONFLICT, "can't create user with ADMIN or USER role");
				}
			});
			
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
			user.setEnabled(true);
			user.setCreatedDate(Calendar.getInstance().getTime());
			user.setProvider(SocialProvider.LOCAL.getProviderType());
		} else {
			// update
			User userFromDB = userRepo.getById(dto.getId());

			Role roleAdmin = roleRepo.findByName(Role.ROLE_ADMIN);
			Role roleUser = roleRepo.findByName(Role.ROLE_USER);
			// if before contain ROLE_USER but now not
			// if before contain ROLE_ADMIN but now not
			// if before not contain ROLE_USER but now contain
			// if before not contain ROLE_ADMIN but now contain
			// if before contain ROLE_USER, now contain ROLE_USER and other ROLE
			if (userFromDB.getRoles().contains(roleUser) && !roles.contains(roleUser)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "can't change role of user already has ADMIN or USER role");
			} else if (!userFromDB.getRoles().contains(roleUser) && roles.contains(roleUser)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "can't change role of user already has ADMIN or USER role");
			} else if (userFromDB.getRoles().contains(roleAdmin) && !roles.contains(roleAdmin)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "can't change role of user already has ADMIN or USER role");
			} else if (!userFromDB.getRoles().contains(roleAdmin) && roles.contains(roleAdmin)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "can't change role of user already has ADMIN or USER role");
			} else if (userFromDB.getRoles().contains(roleUser) && roles.contains(roleUser) && roles.size()>1) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "can't change role of user already has ADMIN or USER role");
			}
			
			if (StringUtils.hasText(dto.getPassword())) {
				user.setPassword(passwordEncoder.encode(dto.getPassword()));
			} else {
				user.setPassword(userFromDB.getPassword());				
			}
			user.setModifiedDate(Calendar.getInstance().getTime());
		}
		
		
		user.setRoles(roles);
		return user;
	}
	
	@Override
	public User findUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public Optional<User> findUserById(Long id) {
		return userRepo.findById(id);
	}
	
	@Override
	public String deleteById(Long id) {
		// TODO Auto-generated method stub
		try {
			userRepo.deleteById(id);
		} catch (Exception e) {
			return e.getMessage();
		}
		return Constants.DELETE_SUCCESSFULLY_MSG;
	}
}
