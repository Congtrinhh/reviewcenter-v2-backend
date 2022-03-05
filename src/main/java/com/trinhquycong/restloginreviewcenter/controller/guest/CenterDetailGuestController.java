package com.trinhquycong.restloginreviewcenter.controller.guest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ibm.icu.util.Calendar;
import com.trinhquycong.restloginreviewcenter.dto.CommentInfoDto;
import com.trinhquycong.restloginreviewcenter.dto.DetailedRate;
import com.trinhquycong.restloginreviewcenter.dto.RateInfoBoxDto;
import com.trinhquycong.restloginreviewcenter.dto.RatingDto;
import com.trinhquycong.restloginreviewcenter.model.Center;
import com.trinhquycong.restloginreviewcenter.model.Rating;
import com.trinhquycong.restloginreviewcenter.model.User;
import com.trinhquycong.restloginreviewcenter.repo.CenterRepo;
import com.trinhquycong.restloginreviewcenter.repo.RatingRepo;
import com.trinhquycong.restloginreviewcenter.repo.UserRepo;
import com.trinhquycong.restloginreviewcenter.service.CenterService;
import com.trinhquycong.restloginreviewcenter.service.RatingService;
import com.trinhquycong.restloginreviewcenter.util.Constants;

@RestController
@RequestMapping("/api")
public class CenterDetailGuestController {

	@Autowired
	private CenterRepo centerRepo;
	
	@Autowired
	private RatingRepo ratingRepo;
	
	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CenterService centerService;
	
	@GetMapping("/centers/{slug}")
	public ResponseEntity<?> getAll(@PathVariable("slug") String slug){
		
		Center center = centerRepo.findBySlug(slug);
		if (center == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't find the center");
		}
		
		Pageable pageable = PageRequest.of(0, Constants.SIZE_PAGE_USER);
		
		// all ratings (without pagination)
		Page<RatingDto> ratingDtoPage = ratingRepo.findByCenterIdEqualsOrderByCreatedDateDesc(center.getId(), null)
				.map(rating->ratingService.toDto(rating)); 
		
		
		// users with pagination
		Page<User> users = userRepo.getAllUsersHaveCommentOnACenter(center.getId(), pageable); 
		
		// first page of ratings
		int sizeOfCurrentSlice = ratingDtoPage.getNumberOfElements(); 
		int endIndex = sizeOfCurrentSlice>=Constants.SIZE_PAGE_USER
				? Constants.SIZE_PAGE_USER : sizeOfCurrentSlice;
				
		List<RatingDto> ratingDtosFirstPage = ratingDtoPage.getContent().subList(0, endIndex);
		
				
		// comments - with username, avatar, comment, rate, created date 
		List<CommentInfoDto> commentInfoDtos = toDto(ratingDtosFirstPage, users.getContent());
		
		
		// rate calc
		long totalRatingTimes = ratingDtoPage.getTotalElements();
		List<DetailedRate> rateDetailList = calcDetailRate(ratingDtoPage);
		RateInfoBoxDto rateInfoBox = new RateInfoBoxDto(rateDetailList, totalRatingTimes);
		rateInfoBox.calcAveragePercentRate();
		
		// results
		Map<String, Object> resultComment = new HashMap<String, Object>();
		resultComment.put("comments", commentInfoDtos);
		resultComment.put("itemsPerPage", users.getSize());
		resultComment.put("currentPage", users.getNumber());
		resultComment.put("totalItems", users.getTotalElements());
		
		Map<String, Object> resultCenter = new HashMap<String, Object>();
		resultCenter.put("center", centerService.toDto(center, false));
		
		Map<String, Object> resultRating = new HashMap<String, Object>();
		resultRating.put("rate", rateInfoBox);
		
		Map<String, Object> finalResult = new HashMap<String, Object>();
		finalResult.put("comment", resultComment);
		finalResult.put("center", resultCenter);
		finalResult.put("rate", resultRating);
		
		
		return ResponseEntity.ok(finalResult);
	}
	
	@GetMapping("/centers/comments")
	public ResponseEntity<?> getComments(
			@RequestParam(name = "page", defaultValue = "0") Integer pageIndex,
			@RequestParam(name = "size", defaultValue = "10") Integer size,
			@RequestParam(name = "centerId") Long centerId) {
		
		Pageable pageable = PageRequest.of(pageIndex, size);
		
		Page<RatingDto> pageRatingDto = ratingRepo.findByCenterIdEqualsOrderByCreatedDateDesc(centerId, pageable)
				.map(ratingService::toDto);
		Page<User> pageUser = userRepo.getAllUsersHaveCommentOnACenter(centerId, pageable);
		
		List<CommentInfoDto> commentInfoDtos = toDto(pageRatingDto.getContent(), pageUser.getContent());
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("comments", commentInfoDtos);
		result.put("itemsPerPage", pageRatingDto.getSize());
		result.put("currentPage", pageRatingDto.getNumber());
		result.put("totalItems", pageRatingDto.getTotalElements());
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/review")
	public ResponseEntity<?> processReview(@RequestBody RatingDto dto){
		if (dto==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must include payload");
		}
		Rating rating = ratingService.toEntity(dto);
		rating.setCreatedDate(Calendar.getInstance().getTime());
		rating.setEnabled(true);
		
		try {
			rating = ratingRepo.save(rating);
			return ResponseEntity.ok(ratingService.toDto(rating));
		}catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you have already rated the center (bạn đã đánh giá trung tâm này rồi) ", e);
		} 
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error occured", e);
		}
	}

	private List<DetailedRate> calcDetailRate(Page<RatingDto> ratingDtoPage) {
		long count1star = 0L;
		long count2star = 0L;
		long count3star = 0L;
		long count4star = 0L;
		long count5star = 0L;
		for (RatingDto r: ratingDtoPage.getContent()) {
			if (r.getRate().equals(1)) {
				count1star++;
			}
			if (r.getRate().equals(2)) {
				count2star++;
			}
			if (r.getRate().equals(3)) {
				count3star++;
			}
			if (r.getRate().equals(4)) {
				count4star++;
			}
			if (r.getRate().equals(5)) {
				count5star++;
			}
		}
		DetailedRate detailedRate1Star = new DetailedRate(1, count1star);
		DetailedRate detailedRate2Star = new DetailedRate(2, count2star);
		DetailedRate detailedRate3Star = new DetailedRate(3, count3star);
		DetailedRate detailedRate4Star = new DetailedRate(4, count4star);
		DetailedRate detailedRate5Star = new DetailedRate(5, count5star);
		return List.of(detailedRate1Star,detailedRate2Star,detailedRate3Star,detailedRate4Star,detailedRate5Star);
	}

	private List<CommentInfoDto> toDto(List<RatingDto> ratingDtos, List<User> users) {
		List<CommentInfoDto> commentInfoDtos = new ArrayList<>();	
		
		commentInfoDtos = ratingDtos.stream()
				.map(rating->mapper.map(rating, CommentInfoDto.class))
				.collect(Collectors.toList());
		
		for (int i=0; i<users.size(); i++) {
			for (int j=0; j<commentInfoDtos.size(); j++) {
				if (i==j) {
					commentInfoDtos.get(i).setAvatarUrl(users.get(i).getAvatarUrl());
					commentInfoDtos.get(i).setDisplayName(users.get(i).getDisplayName());
				}
			}
		}
		
		return commentInfoDtos;
	}
	
}
