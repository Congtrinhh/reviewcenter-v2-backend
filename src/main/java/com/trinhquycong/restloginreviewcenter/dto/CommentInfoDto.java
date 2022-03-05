package com.trinhquycong.restloginreviewcenter.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * for guest
 * @author trinh
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentInfoDto implements Serializable {
	private static final long serialVersionUID = -5023044007779748201L;

	private String avatarUrl;
	
	@NotBlank
	private String displayName;
	
	@NotBlank
	private Integer rate; //1->5
	
	@NotBlank
	private String comment;
	
	@NotBlank
	private Date createdDate;
}
