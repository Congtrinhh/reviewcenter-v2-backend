package com.trinhquycong.restloginreviewcenter.service;

import java.util.Base64;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.trinhquycong.restloginreviewcenter.dto.CenterDto;
import com.trinhquycong.restloginreviewcenter.model.Category;
import com.trinhquycong.restloginreviewcenter.model.Center;
import com.trinhquycong.restloginreviewcenter.repo.CategoryRepo;
import com.trinhquycong.restloginreviewcenter.repo.CenterRepo;
import com.trinhquycong.restloginreviewcenter.util.Constants;
import com.trinhquycong.restloginreviewcenter.util.GeneralUtils;

@Service
public class CenterServiceImpl implements CenterService {

	@Autowired
	private CenterRepo centerRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public String deleteById(Long id) {
		try {
			centerRepo.deleteById(id);
		}catch (Exception e) {
			return e.getMessage();
		}
		return Constants.DELETE_SUCCESSFULLY_MSG;
	}
	
	@Override
	public Center toEntity(CenterDto dto) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Center center = modelMapper.map(dto, Center.class);
		
		if (center.getSlug()==null || center.getSlug().length()==0) {
			center.setSlug(GeneralUtils.createSlug(dto.getName()));
		}
		
		Category category = categoryRepo.getById(dto.getCategoryId());

		if (category == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "category mustn't be null");
		}

		center.setCategory(category);
		return center;
	}
	
	@Override
	public CenterDto toDto(Center center, boolean getAverageRateNumber) {
		CenterDto dto = modelMapper.map(center, CenterDto.class);
		dto.setCategoryId(center.getCategory().getId());
		dto.setCategoryName(center.getCategory().getName());
		
		if (getAverageRateNumber) {
			float avg = GeneralUtils.calcAverageRateNumber(center.getRatings());
			dto.setAverageRateNumber(avg);
		}
		
		byte[] imageBytes = center.getAvatar();
		
		String imageString = null;
		if (imageBytes!=null && imageBytes.length>0) {
			imageString = Base64.getEncoder().encodeToString(imageBytes);
			dto.setImageString(imageString);
		}
		
		return dto;
	}
}
