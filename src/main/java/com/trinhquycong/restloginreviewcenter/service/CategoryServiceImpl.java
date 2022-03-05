package com.trinhquycong.restloginreviewcenter.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trinhquycong.restloginreviewcenter.dto.CategoryDto;
import com.trinhquycong.restloginreviewcenter.model.Category;
import com.trinhquycong.restloginreviewcenter.util.GeneralUtils;
@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryDto toDto(Category category) {
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public Category toEntity(CategoryDto dto) {
		Category c = mapper.map(dto, Category.class);
		c.setSlug(GeneralUtils.createSlug(dto.getName()));
		
		if (c.getSlug()==null || c.getSlug().length()==0) {
			c.setSlug(GeneralUtils.createSlug(dto.getName()));
		}
		return c;
	}

}
