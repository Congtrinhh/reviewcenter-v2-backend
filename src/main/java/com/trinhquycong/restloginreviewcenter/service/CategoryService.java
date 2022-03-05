package com.trinhquycong.restloginreviewcenter.service;

import com.trinhquycong.restloginreviewcenter.dto.CategoryDto;
import com.trinhquycong.restloginreviewcenter.model.Category;

public interface CategoryService {
	
	CategoryDto toDto(Category category);
	
	Category toEntity(CategoryDto dto);
}
