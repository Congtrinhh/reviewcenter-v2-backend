package com.trinhquycong.restloginreviewcenter.controller.admin;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.trinhquycong.restloginreviewcenter.dto.RoleDto;
import com.trinhquycong.restloginreviewcenter.model.Role;
import com.trinhquycong.restloginreviewcenter.repo.RoleRepo;

@RestController
@RequestMapping("api/admin")
public class RoleAdminController {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private RoleRepo roleRepo;
	
	
	@PostMapping("roles")
	@ResponseBody
	public RoleDto createOne(@RequestBody @Valid RoleDto roleDto) {
		Role role = mapper.map(roleDto, Role.class);
		
		role.setCreatedDate(Calendar.getInstance().getTime());
		role = roleRepo.save(role);
		
		return mapper.map(role, RoleDto.class);
	}
	
	@GetMapping("roles/{id}")
	@ResponseBody
	public RoleDto getOne(@PathVariable Long id) {
		Role role = roleRepo.getById(id);
		
		return mapper.map(role, RoleDto.class);
	}
	
	@PutMapping("roles")
	@ResponseBody
	public RoleDto updateOne(@RequestBody @Valid RoleDto roleDto) {
		Role role = mapper.map(roleDto, Role.class);
		role.setModifiedDate(Calendar.getInstance().getTime());
		
		role = roleRepo.save(role);
		
		return mapper.map(role, RoleDto.class);
	}
	
	@DeleteMapping("roles/{id}")
	public ResponseEntity<?> deleteOne(@PathVariable Long id) {
		try {
			roleRepo.deleteById(id);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can't delete");
		}
	}
	
	@GetMapping("roles")
	@ResponseBody
	public Map<String, Object> getAll(
			@RequestParam(name = "page", defaultValue = "0") Integer pageIndex,
			@RequestParam(name = "size", defaultValue = "10") Integer size,
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "enabled", required = false) Boolean enabled){
		
		Page<RoleDto> dtoPage = roleRepo.findAll(PageRequest.of(pageIndex, size))
				.map(role->mapper.map(role, RoleDto.class));
		
		Map<String, Object> rs = new HashMap<String, Object>();
		
		rs.put("roles", dtoPage.getContent());
		rs.put("itemsPerPage", dtoPage.getSize());
		rs.put("currentPage", dtoPage.getNumber());
		rs.put("totalItems", dtoPage.getTotalElements());
		
		return rs;
	}
}
