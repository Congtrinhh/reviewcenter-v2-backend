package com.trinhquycong.restloginreviewcenter.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trinhquycong.restloginreviewcenter.dto.RateNumber;
import com.trinhquycong.restloginreviewcenter.dto.RatingDto;
import com.trinhquycong.restloginreviewcenter.dto.RatingNewsDto;
import com.trinhquycong.restloginreviewcenter.model.Center;
import com.trinhquycong.restloginreviewcenter.model.Rating;
import com.trinhquycong.restloginreviewcenter.model.User;
import com.trinhquycong.restloginreviewcenter.repo.CenterRepo;
import com.trinhquycong.restloginreviewcenter.repo.RatingRepo;
import com.trinhquycong.restloginreviewcenter.repo.UserRepo;
import com.trinhquycong.restloginreviewcenter.util.Constants;
@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	private RatingRepo ratingRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CenterRepo centerRepo;
	
	@Autowired
	private UserRepo userRepo;

	@Override
	public String deleteById(Long id) {
		// TODO Auto-generated method stub
		try {
			ratingRepo.deleteById(id);
		} catch (Exception e) {
			return e.getMessage();
		}
		return Constants.DELETE_SUCCESSFULLY_MSG;
	}

	@Override
	public RatingDto toDto(Rating rating) {
		TypeMap<Rating, RatingDto> typeMap = mapper.getTypeMap(Rating.class, RatingDto.class);
		if (typeMap==null) {
			// skip rate field, because attempt to map get error
			mapper.addMappings(new PropertyMap<Rating, RatingDto>() {
				@Override
				protected void configure() {
					skip(destination.getRate());
				}
			});			
		}
				
		RatingDto dto = mapper.map(rating, RatingDto.class);

		switch (rating.getRate()) {
		case ONE:
			dto.setRate(1);
			break;
		case TWO:
			dto.setRate(2);
			break;
		case THREE:
			dto.setRate(3);
			break;
		case FOUR:
			dto.setRate(4);
			break;
		case FIVE:
			dto.setRate(5);
			break;
		default:
			throw new IllegalArgumentException("rating number not in range.");
		}
		return dto;
	}

	@Override
	public Rating toEntity(RatingDto dto) {
		Rating rating = new Rating();
		rating = mapper.map(dto, Rating.class);
		
		Center center = centerRepo.getById(dto.getCenterId());
		rating.setCenter(center);
		
		User user = userRepo.getById(dto.getUserId());
		rating.setUser(user);
		
		switch (dto.getRate()) {
		case 1:
			rating.setRate(RateNumber.ONE);
			break;
		case 2:
			rating.setRate(RateNumber.TWO);
			break;
		case 3:
			rating.setRate(RateNumber.THREE);
			break;
		case 4:
			rating.setRate(RateNumber.FOUR);
			break;
		case 5:
			rating.setRate(RateNumber.FIVE);
			break;
		default:
			throw new RuntimeException("rate value isn't in range (1 to 5)");
		}
		
		return rating;
	}

	@Override
	public List<RatingNewsDto> getTodayRatingNews() {
		List<Rating> ratings = ratingRepo.getTodayRatings();
		
		List<RatingNewsDto> ratingNewsDtos = new ArrayList<RatingNewsDto>();
		ratings.forEach(rating -> {
			RatingNewsDto dto = new RatingNewsDto();
			dto.setAvatarUrl(rating.getUser().getAvatarUrl());
			dto.setCenterName(rating.getCenter().getName());
			dto.setCenterSlug(rating.getCenter().getSlug());
			dto.setUserName(rating.getUser().getDisplayName());
			ratingNewsDtos.add(dto);
		});
		
		return ratingNewsDtos;
	}

}
