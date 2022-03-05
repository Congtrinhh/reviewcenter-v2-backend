package com.trinhquycong.restloginreviewcenter.controller.admin;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.trinhquycong.restloginreviewcenter.dto.UserDto;
import com.trinhquycong.restloginreviewcenter.dto.UserSearchDto;
import com.trinhquycong.restloginreviewcenter.model.User;
import com.trinhquycong.restloginreviewcenter.repo.UserRepo;
import com.trinhquycong.restloginreviewcenter.service.UserService;
import com.trinhquycong.restloginreviewcenter.specification.UserSpecification;

@RestController
@RequestMapping("/api/admin")
public class UserAdminController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private UserService userService;
	
	/**
	 * for administrators - not user
	 * @param dto
	 * @return
	 */
	@PostMapping("users")
	public ResponseEntity<?> createOne(@Valid @RequestBody UserDto dto) throws Exception {
		User user = userService.toEntity(dto);
		
		try {
			user = userRepo.save(user);
		}catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already exists, please choose another", e);
		} 
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can't create user");
		}
		
		return ResponseEntity.ok(userService.toDto(user));
	}

	@GetMapping("users/{id}")
	@ResponseBody
	public UserDto getOne(@PathVariable Long id) {
		User user = userRepo.getById(id);
		if (user.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user mustn't be null");
		}

		UserDto dto = userService.toDto(user);

		return dto;
	}
	
	@PutMapping("users")
	public ResponseEntity<?> updateOne(@RequestBody @Valid UserDto dto) {
		User user = userService.toEntity(dto);
	
		try {
			user = userRepo.save(user);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can't update user");
		}
		
		return ResponseEntity.ok(userService.toDto(user));
	}
	
	/**
	 * page param: zero-based
	 * @param roleName
	 * @param roleName
	 * @return
	 */
	@GetMapping("users")
	@ResponseBody
	public Map<String, Object> getAll(
			@RequestParam(name = "page", defaultValue = "0") Integer pageIndex,
			@RequestParam(name = "size", defaultValue = "10") Integer size,
			UserSearchDto filter) {
		
		Pageable pageable = PageRequest.of(pageIndex, size);
		
		filter.setSortByCreatedDateDesc(true);
		Specification<User> specs = new UserSpecification(filter);
		Page<User> page = userRepo.findAll(specs, pageable);

		Page<UserDto> dtoPage = page.map(userService::toDto);
		
		Map<String, Object> rs = new HashMap<String, Object>();
		
		rs.put("users", dtoPage.getContent());
		rs.put("itemsPerPage", dtoPage.getSize());
		rs.put("currentPage", dtoPage.getNumber());
		rs.put("totalItems", dtoPage.getTotalElements());

		return rs;
	}

	/**
	 * delete completely entity from DB
	 * @param id
	 * @return
	 */
	@DeleteMapping("users/{id}")
	@ResponseBody
	public String deleteOne(@PathVariable(required = true) Long id) {
		return userService.deleteById(id);
	}
	
	/**
	 * delete (deactivate)
	 */
	@DeleteMapping("users/{id}/deactivate")
	@ResponseBody
	public void deactivate(@PathVariable Long id) {
		User user = userRepo.getById(id);
		if (user!=null) {
			user.setEnabled(false);
			user.setModifiedDate(Calendar.getInstance().getTime());
			userRepo.save(user);
		}
	}
	
	/**
	 * activate again
	 * @param id
	 * @return
	 */
	@PutMapping("user/{id}/activate")
	@ResponseBody
	public UserDto activate(@PathVariable Long id) {
		User user = userRepo.getById(id);
		if (user!=null) {
			user.setEnabled(true);
			user.setModifiedDate(Calendar.getInstance().getTime());
			user = userRepo.save(user);
			return userService.toDto(user);
		}
		return null;
	}

}
