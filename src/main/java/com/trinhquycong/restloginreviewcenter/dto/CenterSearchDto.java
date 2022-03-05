package com.trinhquycong.restloginreviewcenter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CenterSearchDto {
	
	private Long categoryId;
	
	private String name;
	
	private String description;
	
	private String address;
	
	private Integer centerSize;
	
	private Integer sizeMin;
	
	private Integer sizeMax;
	
	private Boolean enabled;
	
	private Boolean sortByCreatedDateDesc;
}
