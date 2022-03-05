package com.trinhquycong.restloginreviewcenter.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CenterSizeRangeValidator.class)
@Documented
public @interface CenterSizeRange {
	String message() default "Min size and max size not valid";
	 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};

}
