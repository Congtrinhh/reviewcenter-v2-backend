package com.trinhquycong.restloginreviewcenter.service;

import java.util.List;

import com.trinhquycong.restloginreviewcenter.dto.RatingDto;
import com.trinhquycong.restloginreviewcenter.dto.RatingNewsDto;
import com.trinhquycong.restloginreviewcenter.model.Rating;

public interface RatingService {

	String deleteById(Long id);
	
	RatingDto toDto(Rating rating);
	
	Rating toEntity(RatingDto dto);

	List<RatingNewsDto> getTodayRatingNews();
}
