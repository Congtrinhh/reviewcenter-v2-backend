package com.trinhquycong.restloginreviewcenter.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinhquycong.restloginreviewcenter.dto.JwtAuthenticationResponse;
import com.trinhquycong.restloginreviewcenter.dto.LocalUser;
import com.trinhquycong.restloginreviewcenter.dto.LoginRequest;
import com.trinhquycong.restloginreviewcenter.security.jwt.TokenProvider;
import com.trinhquycong.restloginreviewcenter.service.UserService;
import com.trinhquycong.restloginreviewcenter.util.GeneralUtils;


@RestController
@RequestMapping("/api/auth")
public class LoginAdminController {
	
	@Autowired
    AuthenticationManager authenticationManager;
	
	@Autowired
    UserService userService;
	
	@Autowired
    TokenProvider tokenProvider;
	
	@PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication); 
        String jwt = tokenProvider.createToken(authentication);
        LocalUser localUser = (LocalUser) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, GeneralUtils.buildUserInfo(localUser)));
    }
	
	//Redirect
		
}
