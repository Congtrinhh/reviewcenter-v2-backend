package com.trinhquycong.restloginreviewcenter.service;

import com.trinhquycong.restloginreviewcenter.dto.CenterDto;
import com.trinhquycong.restloginreviewcenter.model.Center;

public interface CenterService {
	
	String deleteById(Long id);
	
	Center toEntity(CenterDto dto);
	
	CenterDto toDto(Center center, boolean getAverageRateNumber);
}
