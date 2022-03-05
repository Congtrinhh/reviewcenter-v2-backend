package com.trinhquycong.restloginreviewcenter.model;
// Generated Dec 11, 2021, 7:48:03 AM by Hibernate Tools 4.3.5.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * Category generated by hbm2java
 */
@Entity
@Table(schema = "reviewcenter", name = "categories")
@Getter
@Setter
public class Category implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="seq_category",sequenceName="seq_category_optional", allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_category")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "name", nullable = false, length = 200, unique = true)
	private String name;
	
	private String slug;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
	private Set<Center> centers = new HashSet<Center>(0);

}
