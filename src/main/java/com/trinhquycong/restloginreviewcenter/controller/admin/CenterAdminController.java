package com.trinhquycong.restloginreviewcenter.controller.admin;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.trinhquycong.restloginreviewcenter.dto.CenterDto;
import com.trinhquycong.restloginreviewcenter.dto.CenterSearchDto;
import com.trinhquycong.restloginreviewcenter.model.Category;
import com.trinhquycong.restloginreviewcenter.model.Center;
import com.trinhquycong.restloginreviewcenter.repo.CategoryRepo;
import com.trinhquycong.restloginreviewcenter.repo.CenterRepo;
import com.trinhquycong.restloginreviewcenter.service.CenterService;
import com.trinhquycong.restloginreviewcenter.specification.CenterSpecification;

@RestController
@RequestMapping("/api/admin")
public class CenterAdminController {

	@Autowired
	private CenterService centerService;

	@Autowired
	private CenterRepo centerRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@PostMapping(value = "centers" , consumes = {"multipart/form-data"})
	public ResponseEntity<CenterDto> createOne(
			@RequestPart("center") CenterDto centerDto,
			@RequestPart(name = "imageFile", required = false) MultipartFile file
			) throws RuntimeException, IOException {

		if (centerDto == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "center mustn't be null");
		}

		Category category = categoryRepo.getById(centerDto.getCategoryId());
		if (category==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "category mustn't be null");
		}
		
		Center center = centerService.toEntity(centerDto);
		//
		center = addImageFileToEntity(center, file);
		//
		center.setEnabled(true);
		center.setCreatedDate(Calendar.getInstance().getTime());

		center = centerRepo.save(center);

		centerDto = centerService.toDto(center, false);

		ResponseEntity<CenterDto> responseEntity = new ResponseEntity<CenterDto>(centerDto, HttpStatus.CREATED);
		return responseEntity;
	}

	private Center addImageFileToEntity(Center center, MultipartFile file) throws IOException {
		if (file==null || file.isEmpty()) {
			return center;
		}
	    byte[] bytes = file.getBytes(); // maybe test if the length is greater than 0
	    center.setAvatar(bytes);
	    return center;
	}

	@GetMapping("centers/{id}")
	public ResponseEntity<CenterDto> getOne(@PathVariable Long id) {

		Center center = centerRepo.getById(id);

		if (center == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "center mustn't be null");
		}

		// convert to dto
		CenterDto centerDto = centerService.toDto(center, false);

		ResponseEntity<CenterDto> responseEntity = new ResponseEntity<CenterDto>(centerDto, HttpStatus.OK);
		return responseEntity;
	}

	@PutMapping(value = "centers" , consumes = {"multipart/form-data"})
	public ResponseEntity<CenterDto> updateOne(
			@RequestPart("center") CenterDto centerDto,
			@RequestPart(name = "imageFile", required = false) MultipartFile file) throws IOException {

		Center center = centerService.toEntity(centerDto);
		
		// 
		center = addImageFileToEntity(center, file);
		center.setModifiedDate(Calendar.getInstance().getTime());

		center = centerRepo.save(center);

		// để trả về client
		centerDto = centerService.toDto(center, false);

		return new ResponseEntity<CenterDto>(centerDto, HttpStatus.OK);
	}

	@DeleteMapping("centers/{id}")
	public ResponseEntity<String> deleteOne(@PathVariable("id") Long id) {
		String resultMsg = centerService.deleteById(id);
		return new ResponseEntity<String>(resultMsg, HttpStatus.OK);
	}

	@GetMapping("centers")
	@ResponseBody
	public Map<String, Object> getAll(
			@RequestParam(name = "page", defaultValue = "0") Integer pageIndex,
			@RequestParam(name = "size", defaultValue = "10") Integer size,
			@Valid CenterSearchDto filter) {

		Pageable pageable = PageRequest.of(pageIndex, size);
		
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
	
	@DeleteMapping("center/{id}/deactivate")
	@ResponseBody
	public void deactivate(@PathVariable Long id) {
		Center center = centerRepo.getById(id);
		if (center!=null) {
			center.setEnabled(false);
			center.setModifiedDate(Calendar.getInstance().getTime());
			centerRepo.save(center);
		}
	}
	
	@PutMapping("center/{id}/activate")
	@ResponseBody
	public CenterDto activate(@PathVariable Long id) {
		Center center = centerRepo.getById(id);
		if (center!=null) {
			center.setEnabled(true);
			center.setModifiedDate(Calendar.getInstance().getTime());
			center=  centerRepo.save(center);
			return centerService.toDto(center, false);
		}
		return null;
	}
	
}
