package com.trinhquycong.restloginreviewcenter.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "reviewcenter", name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seq_user",sequenceName="seq_user_optional", allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_user")
	private Long id;
	
	private String displayName;
	
	private String email;
	
    private String password;
    
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;
    
    private Boolean enabled;
    
    private String provider;
    
    private String providerUserId;
    
    private String avatarUrl;
    
 // bi-directional many-to-many association to Role
    @JsonIgnore
    @ManyToMany
    @JoinTable(schema = "reviewcenter", name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles;
}
