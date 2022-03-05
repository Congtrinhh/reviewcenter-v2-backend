package com.trinhquycong.restloginreviewcenter.controller.guest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinhquycong.restloginreviewcenter.config.CurrentUser;
import com.trinhquycong.restloginreviewcenter.dto.LocalUser;
import com.trinhquycong.restloginreviewcenter.util.GeneralUtils;

@RestController
@RequestMapping("/api")
public class UserController {

	/**
	 * for social login, because only with social login does spring security store authenticated user into context
	 * @param user
	 * @return
	 */
	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getCurrentUser(@CurrentUser LocalUser user) {
		return ResponseEntity.ok(GeneralUtils.buildUserInfo(user));
	}
	
}
