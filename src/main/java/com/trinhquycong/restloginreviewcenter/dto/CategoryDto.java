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
public class CategoryDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Long id;
	
	@NotBlank
	protected String name;
	
	protected Boolean enabled;
	
	protected Date createdDate;
	
	protected Date modifiedDate;
	
	private String slug; // has private access modifier because just this class need it
}
