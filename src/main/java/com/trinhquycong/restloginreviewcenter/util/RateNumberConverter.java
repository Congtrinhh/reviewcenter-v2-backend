package com.trinhquycong.restloginreviewcenter.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.trinhquycong.restloginreviewcenter.dto.RateNumber;

@Converter
public class RateNumberConverter implements AttributeConverter<RateNumber, Integer> {

	@Override
	public Integer convertToDatabaseColumn(RateNumber attribute) {
		// TODO Auto-generated method stub
		if (attribute == null)
			return null;
		switch (attribute) {
		case ONE:
			return 1;
		case TWO:
			return 2;
		case THREE:
			return 3;
		case FOUR:
			return 4;
		case FIVE:
			return 5;
		default:
			throw new IllegalArgumentException(attribute + " not supported.");
		}
	}

	@Override
	public RateNumber convertToEntityAttribute(Integer dbData) {
		// TODO Auto-generated method stub
		if (dbData == null)
            return null;
		switch (dbData) {
		case 1:
			return RateNumber.ONE;
		case 2:
			return RateNumber.TWO;
		case 3:
			return RateNumber.THREE;
		case 4:
			return RateNumber.FOUR;
		case 5:
			return RateNumber.FIVE;
		default:
			throw new IllegalArgumentException(dbData + " not supported.");
		}
	}

}
