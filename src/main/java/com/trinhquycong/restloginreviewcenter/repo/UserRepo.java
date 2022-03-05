package com.trinhquycong.restloginreviewcenter.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.trinhquycong.restloginreviewcenter.model.User;

public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	User findByEmail(String email);
	
	User findUserByEmailEqualsAndEnabledEquals(String email, boolean enabled);

	boolean existsByEmail(String email);

	@Transactional
	@Query(
			nativeQuery = true, 
			value = "select u.* \r\n"
					+ "from reviewcenter.users u inner join reviewcenter.ratings r on u.id=r.user_id inner join reviewcenter.centers c on c.id=r.center_id where c.id=:centerId and u.enabled=true and r.enabled=true and c.enabled=true order by r.created_date desc"
	)
	Page<User> getAllUsersHaveCommentOnACenter(@Param("centerId") Long centerId, Pageable pageable);
}
