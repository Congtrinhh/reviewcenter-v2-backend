package com.trinhquycong.restloginreviewcenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trinhquycong.restloginreviewcenter.repo.RoleRepo;
import com.trinhquycong.restloginreviewcenter.util.Constants;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public String deleteById(Long id) {
		// TODO Auto-generated method stub
		try {
			roleRepo.deleteById(id);
		} catch (Exception e) {
			return e.getMessage();
		}
		return Constants.DELETE_SUCCESSFULLY_MSG;
	}

}
