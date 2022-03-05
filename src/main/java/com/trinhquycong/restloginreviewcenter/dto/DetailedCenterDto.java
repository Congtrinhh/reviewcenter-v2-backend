package com.trinhquycong.restloginreviewcenter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetailedCenterDto {
	
	private String name;
	
	private String categoryName;
	
	private Integer size;
	
	private String address;
	
	// total of reviews
	private Integer totalRatedTimes;
	
	// 0 to 100%, for calculating star (1 to 5)
	private Float averageRatePercent;
}
