package com.trinhquycong.restloginreviewcenter.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.trinhquycong.restloginreviewcenter.dto.UserSearchDto;
import com.trinhquycong.restloginreviewcenter.model.Role;
import com.trinhquycong.restloginreviewcenter.model.User;

public class UserSpecification implements Specification<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserSearchDto search;

	public UserSpecification(UserSearchDto search) {
		super();
		this.search = search;
	}

	@Override
	public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		Join<User, Role> roles = root.join("roles");

		if (search.getEnabled() != null) {
			predicates.add(cb.equal(root.get("enabled"), search.getEnabled()));
		}
		if (search.getKeyword()!=null) {
			predicates.add(
					cb.or(
							cb.like(root.get("displayName"), "%" + search.getKeyword() + "%"), 
							cb.like(root.get("email"), "%" + search.getKeyword() + "%")
					));
		} else {
			if (search.getDisplayName() != null) {
				predicates.add(cb.like(root.get("displayName"), "%" + search.getDisplayName() + "%")); 
			} else if (search.getEmail() !=null) {
				predicates.add(cb.like(root.get("email"), "%" + search.getEmail() + "%"));
			}
		}
		if (search.getProvider() != null) {
			predicates.add(cb.equal(cb.upper(root.get("provider")), search.getProvider().toUpperCase()));
		}
		if (search.getRoleId() != null) {
			predicates.add(cb.equal(roles.get("id"), search.getRoleId()));
		}
		if (search.getRoleName() != null) {
			predicates.add(cb.equal(cb.upper(roles.get("name")), search.getRoleName().toUpperCase()));
		}
		// sort by created time
		if (search.getSortByCreatedDateDesc() != null && search.getSortByCreatedDateDesc() == true) {
			query.orderBy(cb.desc(root.get("createdDate")));
		}
		
		// distinct admin
		query.distinct(true);

		Predicate[] p = new Predicate[predicates.size()];

		return cb.and(predicates.toArray(p));
	}

}
