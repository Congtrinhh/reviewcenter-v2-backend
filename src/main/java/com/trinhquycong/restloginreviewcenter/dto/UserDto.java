package com.trinhquycong.restloginreviewcenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class UserDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;

	@NotBlank
	private String email;

	private String displayName;
	
	private String password;

	private String provider;

	private String avatarUrl;

	private Boolean enabled;

	private Date createdDate;
	
	private Date modifiedDate;

	private Set<String> roleNames;
	
	private Set<Long> roleIds;
}
