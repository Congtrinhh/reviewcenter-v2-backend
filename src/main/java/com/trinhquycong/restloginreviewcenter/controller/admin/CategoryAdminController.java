package com.trinhquycong.restloginreviewcenter.controller.admin;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.trinhquycong.restloginreviewcenter.dto.CategoryDto;
import com.trinhquycong.restloginreviewcenter.model.Category;
import com.trinhquycong.restloginreviewcenter.repo.CategoryRepo;
import com.trinhquycong.restloginreviewcenter.service.CategoryService;

@RestController
@RequestMapping("api/admin")
public class CategoryAdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryRepo categoryRepo;

	@PostMapping("categories")
	@ResponseBody
	public CategoryDto createOne(@RequestBody CategoryDto categoryDto) {
		Category category = categoryService.toEntity(categoryDto);

		category.setCreatedDate(Calendar.getInstance().getTime());
		category = categoryRepo.save(category);

		return categoryService.toDto(category);
	}

	@GetMapping("categories/{id}")
	@ResponseBody
	public CategoryDto getOne(@PathVariable Long id) {
		Category category = categoryRepo.getById(id);

		return categoryService.toDto(category);
	}

	@PutMapping("categories")
	@ResponseBody
	public CategoryDto updateOne(@RequestBody CategoryDto categoryDto) {
		Category category = categoryService.toEntity(categoryDto);
		category.setModifiedDate(Calendar.getInstance().getTime());

		category = categoryRepo.save(category);

		return categoryService.toDto(category);
	}

	@DeleteMapping("categories/{id}")
	public ResponseEntity<?> deleteOne(@PathVariable("id") Long id) {
		try {
			categoryRepo.deleteById(id);
			return ResponseEntity.ok(null);
		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can't delete");
		}
	}

	@GetMapping("categories")
	@ResponseBody
	public Map<String, Object> getAll(@RequestParam(name = "page", defaultValue = "0") Integer pageIndex,
			@RequestParam(name = "size", defaultValue = "10") Integer size,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "enabled", required = false) Boolean enabled) {

		Pageable pageable = PageRequest.of(pageIndex, size);

		Page<Category> page;

		if (enabled != null) {
			page = categoryRepo.findByNameContainsAndEnabledEqualsOrderByCreatedDateDesc(name, enabled, pageable);
		} else {
			page = categoryRepo.findByNameContainsOrderByCreatedDateDesc(name, pageable);
		}

		Page<CategoryDto> dtoPage = page.map(categoryService::toDto);

		Map<String, Object> rs = new HashMap<String, Object>();
		rs.put("categories", dtoPage.getContent());
		rs.put("itemsPerPage", dtoPage.getSize());
		rs.put("currentPage", dtoPage.getNumber());
		rs.put("totalItems", dtoPage.getTotalElements());

		return rs;
	}

	@DeleteMapping("categories/{id}/deactivate")
	@ResponseBody
	public void deactivate(@PathVariable Long id) {
		Category category = categoryRepo.getById(id);
		if (category != null) {
			category.setEnabled(false);
			category.setModifiedDate(Calendar.getInstance().getTime());
			categoryRepo.save(category);
		}
	}

	@PutMapping("categories/{id}/activate")
	@ResponseBody
	public CategoryDto activate(@PathVariable Long id) {
		Category category = categoryRepo.getById(id);
		if (category != null) {
			category.setEnabled(true);
			category.setModifiedDate(Calendar.getInstance().getTime());
			category = categoryRepo.save(category);
			return categoryService.toDto(category);
		}
		return null;
	}

}
