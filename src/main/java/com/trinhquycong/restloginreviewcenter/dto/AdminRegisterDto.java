package com.trinhquycong.restloginreviewcenter.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminRegisterDto implements Serializable{
	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String email;
	
	@Size(min = 8)
	private String password;

	@NotEmpty
	private String displayName;

	private String avatarUrl;

	@NotEmpty
	private List<Long> roleIds;
}
