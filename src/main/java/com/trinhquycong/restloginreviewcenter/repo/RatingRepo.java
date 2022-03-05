package com.trinhquycong.restloginreviewcenter.repo;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.trinhquycong.restloginreviewcenter.model.Rating;

public interface RatingRepo extends JpaRepository<Rating, Long>, JpaSpecificationExecutor<Rating> {

	@Transactional
	@Modifying
	@Query(value = "update ratings set rate=:rate, comment=:comment, center_id=:centerId, user_id=:userId, updated_at=:updatedAt  where id=:id", nativeQuery = true)
	void update(@Param("id") Long id, @Param("rate") Integer rate, @Param("comment") String comment,
			@Param("userId") Long userId, @Param("centerId") Long centerId, @Param("updatedAt") Date updatedAt);
	
	Page<Rating> findByUserIdEquals(Long userId, Pageable pageable);
	
	Page<Rating> findByUserIdEqualsAndRateIn(Long userId, Collection<?> rates, Pageable pageable);
	
	Page<Rating> findByCenterIdEqualsOrderByCreatedDateDesc(Long centerId, Pageable pageable);
	
	// get 10 latest today's ratings provided that they are of today and enabled
	@Transactional
	@Query(nativeQuery = true, value = "select * from reviewcenter.ratings where DATE(created_date) = CURRENT_DATE and enabled = true order by created_date DESC limit 10")
	List<Rating> getTodayRatings();
}
