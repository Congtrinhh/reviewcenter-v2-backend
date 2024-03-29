package com.trinhquycong.restloginreviewcenter.model;
// Generated Dec 11, 2021, 7:48:03 AM by Hibernate Tools 4.3.5.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trinhquycong.restloginreviewcenter.validator.CenterSizeRange;

import lombok.Getter;
import lombok.Setter;

/**
 * Center generated by hbm2java
 */
@Entity
@Table(schema = "reviewcenter", name = "centers")
@Getter
@Setter
@CenterSizeRange // custom size range validator
public class Center implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="seq_center",sequenceName="seq_center_optional", allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_center")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "name", nullable = false, length = 200)
	private String name;
	
	private String slug;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "address", nullable = false, length = 200)
	private String address;
	
	@Column(name = "size_min", nullable = false)
	private int sizeMin;
	
	@Column(name = "size_max", nullable = false)
	private int sizeMax;
	
	@Column(name = "avatar")
	private byte[] avatar;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "center")
	private Set<Rating> ratings = new HashSet<Rating>(0);
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

}
