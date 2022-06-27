package com.example.woowa.restaurant.advertisement.converter;

import com.example.woowa.restaurant.advertisement.enums.RateType;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RateTypeConverter implements AttributeConverter<RateType, String> {

    @Override
    public String convertToDatabaseColumn(RateType attribute) {
        return attribute.getName();
    }

    @Override
    public RateType convertToEntityAttribute(String dbData) {
        return RateType.getRateType(dbData);
    }

}
