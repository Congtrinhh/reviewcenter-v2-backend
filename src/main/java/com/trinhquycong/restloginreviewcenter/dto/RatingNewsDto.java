package com.trinhquycong.restloginreviewcenter.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingNewsDto {
	
	@NotBlank
	private String userName;
	
	@NotBlank
	private String avatarUrl;
	
	@NotBlank
	private String centerName;
	
	@NotBlank
	private String centerSlug;
}
