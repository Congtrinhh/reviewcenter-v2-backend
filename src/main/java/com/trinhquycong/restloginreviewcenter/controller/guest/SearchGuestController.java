package com.trinhquycong.restloginreviewcenter.controller.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trinhquycong.restloginreviewcenter.dto.CenterDto;
import com.trinhquycong.restloginreviewcenter.model.Center;
import com.trinhquycong.restloginreviewcenter.repo.CenterRepo;
import com.trinhquycong.restloginreviewcenter.service.CenterService;

@RestController
@RequestMapping("/api/search")
public class SearchGuestController {
	
	@Autowired
	private CenterRepo centerRepo;
	
	@Autowired
	private CenterService centerService;
	
	@GetMapping
	public ResponseEntity<?> getAll(
			@RequestParam(name = "key") String key,
			@RequestParam(name = "page", defaultValue = "0") Integer pageIndex,
			@RequestParam(name = "size", defaultValue = "10") Integer size) {

		Pageable pageable = PageRequest.of(pageIndex, size);
		
		key = key.toLowerCase();
		
		Page<Center> page = centerRepo.findByNameContainsIgnoreCaseOrAddressContainsIgnoreCaseAndEnabledEqualsOrderBySizeMaxDesc(key, key,true, pageable);
		
		Page<CenterDto> dtoPage = page.map(center->centerService.toDto(center, false));

		
		return ResponseEntity.ok(dtoPage.getContent());
	}

}
