package com.trinhquycong.restloginreviewcenter.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank
	private Long centerId;
	
	@NotBlank
	private Long userId;
	
	@NotBlank
	private Integer rate;
	
	@NotBlank
	private String comment;
	
	private Boolean enabled;
	
	private Date createdDate;
	
	private Date modifiedDate;

	
}
