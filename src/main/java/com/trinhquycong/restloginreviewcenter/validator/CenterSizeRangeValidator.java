package com.trinhquycong.restloginreviewcenter.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.trinhquycong.restloginreviewcenter.model.Center;

public class CenterSizeRangeValidator implements ConstraintValidator<CenterSizeRange, Center> {

	@Override
	public boolean isValid(Center center, ConstraintValidatorContext context) {
		return center.getSizeMax() - center.getSizeMin() > 0;
	}

}
