package com.trinhquycong.restloginreviewcenter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RatingSearchDto {
	
	private Long centerId;
	
	private Long userId;
	
	private Integer rate;
	
	private String comment;
	
	private Boolean enabled;
	
	private Boolean sortByCreatedDateDesc;
}
