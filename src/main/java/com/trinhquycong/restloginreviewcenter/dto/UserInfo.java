package com.trinhquycong.restloginreviewcenter.dto;

import java.util.List;

import lombok.Value;

@Value
public class UserInfo {
	private String id, displayName, email, avatarUrl;
	private List<String> roles;
}
