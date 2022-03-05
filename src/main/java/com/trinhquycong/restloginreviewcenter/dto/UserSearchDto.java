package com.trinhquycong.restloginreviewcenter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSearchDto {

	private String displayName;
	
	private String provider;
	
	private String roleName;
	
	private Long roleId;
	
	private Boolean enabled;
	
	private Boolean sortByCreatedDateDesc;
	
	private String keyword;
	
	private String email;
}
