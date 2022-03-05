package com.trinhquycong.restloginreviewcenter.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class CenterDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank
	private Long categoryId;
	
	@NotBlank
	private String name;
	
	private String description;
	
	@NotBlank
	private String address;
	
	private Integer sizeMin;
	
	private Integer sizeMax;
	
	private Boolean enabled;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private MultipartFile imageFile;
	
	private String imageString;
	
	private String slug;
	
	private String categoryName;
	
	private Float averageRateNumber;
}
