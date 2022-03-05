package com.trinhquycong.restloginreviewcenter.controller.guest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trinhquycong.restloginreviewcenter.dto.CategoryDto;
import com.trinhquycong.restloginreviewcenter.dto.CenterDto;
import com.trinhquycong.restloginreviewcenter.dto.CenterSearchDto;
import com.trinhquycong.restloginreviewcenter.dto.RatingNewsDto;
import com.trinhquycong.restloginreviewcenter.model.Center;
import com.trinhquycong.restloginreviewcenter.repo.CategoryRepo;
import com.trinhquycong.restloginreviewcenter.repo.CenterRepo;
import com.trinhquycong.restloginreviewcenter.service.CenterService;
import com.trinhquycong.restloginreviewcenter.service.RatingService;
import com.trinhquycong.restloginreviewcenter.specification.CenterSpecification;
import com.trinhquycong.restloginreviewcenter.util.Constants;

@RequestMapping("/api/home")
@RestController
public class HomeGuestController {
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private CenterRepo centerRepo;
	
	@Autowired
	private CenterService centerService;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private RatingService ratingService;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAll(){

		// category
		List<CategoryDto> categories =  categoryRepo.findByEnabledEqualsOrderByCreatedDateAsc(true)
				.stream().map(category->mapper.map(category, CategoryDto.class))
				.collect(Collectors.toList());
		
		// center
		CenterSearchDto centerSearchDto = new CenterSearchDto();
		centerSearchDto.setEnabled(true);
		centerSearchDto.setSortByCreatedDateDesc(true);
		Specification<Center> centerSpecification = new CenterSpecification(centerSearchDto);
		
		Pageable centerPageable = PageRequest.of(0, Constants.SIZE_PAGE_USER);
		Page<CenterDto> centerPage = centerRepo.findAll(centerSpecification, centerPageable)
				.map(center -> centerService.toDto(center, true));
		
		// rating news
		List<RatingNewsDto> ratingNewsDtos = ratingService.getTodayRatingNews();
		
		// collect results
		Map<String, Object> result1 = new HashMap<>();
		result1.put("categories", categories);
		
		Map<String, Object> result2 = new HashMap<>();
		result2.put("centers", centerPage.getContent());
		result2.put("itemsPerPage", centerPage.getSize());
		result2.put("currentPage", centerPage.getNumber());
		result2.put("totalItems", centerPage.getTotalElements());
		
		Map<String, Object> result3 = new HashMap<>();
		result3.put("todayRatings", ratingNewsDtos);
		
		Map<String, Object> result = new HashMap<>();
		result.put("category", result1);
		result.put("center", result2);
		result.put("news", result3);
		
		return ResponseEntity.ok(result);
	}
	
	
	
	@GetMapping("centers")
	@ResponseBody
	public Map<String, Object> getAll(
			@RequestParam(name = "page", defaultValue = "0") Integer pageIndex,
			@RequestParam(name = "size", defaultValue = "10") Integer size,
			@Valid CenterSearchDto filter) {

		Pageable pageable = PageRequest.of(pageIndex, size);
		
		filter.setEnabled(true);
		filter.setSortByCreatedDateDesc(true);
		Specification<Center> specs = new CenterSpecification(filter);
		Page<Center> page = centerRepo.findAll(specs, pageable);
		
		Page<CenterDto> dtoPage = page.map(center -> centerService.toDto(center, true));
		
		Map<String, Object> rs = new HashMap<String, Object>();

		rs.put("centers", dtoPage.getContent());
		rs.put("itemsPerPage", dtoPage.getSize());
		rs.put("currentPage", dtoPage.getNumber());
		rs.put("totalItems", dtoPage.getTotalElements());
		
		return rs;
	}

}
