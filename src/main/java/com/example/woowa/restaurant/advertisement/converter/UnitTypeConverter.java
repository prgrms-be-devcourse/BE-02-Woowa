package com.example.woowa.restaurant.advertisement.converter;

import com.example.woowa.restaurant.advertisement.enums.UnitType;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UnitTypeConverter implements AttributeConverter<UnitType, String> {

    @Override
    public String convertToDatabaseColumn(UnitType attribute) {
        return attribute.getName();
    }

    @Override
    public UnitType convertToEntityAttribute(String dbData) {
        return UnitType.getUnitType(dbData);
    }

}
