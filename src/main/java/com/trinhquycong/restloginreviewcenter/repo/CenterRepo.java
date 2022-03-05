package com.trinhquycong.restloginreviewcenter.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.trinhquycong.restloginreviewcenter.model.Center;

public interface CenterRepo extends JpaRepository<Center, Long>, JpaSpecificationExecutor<Center> {

	Page<Center> findByNameContainsAndAddressContainsAndCategoryIdEqualsAndEnabledEquals(
			String name, String address, Long categoryId, Boolean enabled, Pageable pageable);
	
	Page<Center> findByNameContainsIgnoreCaseOrAddressContainsIgnoreCaseAndEnabledEqualsOrderBySizeMaxDesc(String name, String address, boolean enabled, Pageable pageable);
	
	Center findBySlug(String slug);
	
}
