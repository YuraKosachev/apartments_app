package com.apartment.notification.core.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class JsonStringArrayConverter implements AttributeConverter<String[], String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(String[] attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing String[]", e);
        }
    }

    @Override
    public String[] convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? new String[0] : objectMapper.readValue(dbData, String[].class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializing String[]", e);
        }
    }
}
