package com.trinhquycong.restloginreviewcenter.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.trinhquycong.restloginreviewcenter.dto.RatingSearchDto;
import com.trinhquycong.restloginreviewcenter.model.Center;
import com.trinhquycong.restloginreviewcenter.model.Rating;
import com.trinhquycong.restloginreviewcenter.model.User;
import com.trinhquycong.restloginreviewcenter.util.GeneralUtils;

public class RatingSpecification implements Specification<Rating> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RatingSearchDto search;
	
	public RatingSpecification(RatingSearchDto search) {
		this.search = search;
	}

	@Override
	public Predicate toPredicate(Root<Rating> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		
		Join<Rating, Center> center = root.join("center");
		
		Join<Rating, User> user = root.join("user");
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (search.getCenterId()!=null) {
			predicates.add(cb.equal(center.get("id"), search.getCenterId()));
		}
		if (search.getUserId()!=null) {
			predicates.add(cb.equal(user.get("id"), search.getUserId()));
		}
		if (search.getRate()!=null) {
			predicates.add(cb.equal(root.get("rate"), GeneralUtils.getRateNumber(search.getRate())));
		}
		if (search.getEnabled()!=null) {
			predicates.add(cb.equal(root.get("enabled"), search.getEnabled()));
		}
		if (search.getSortByCreatedDateDesc()!=null && search.getSortByCreatedDateDesc()==true) {
			query.orderBy(cb.desc(root.get("createdDate")));
		} 
		
		Predicate[] p = new Predicate[predicates.size()];
		
		return cb.and(predicates.toArray(p));
	}

}
