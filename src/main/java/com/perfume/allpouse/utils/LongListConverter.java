package com.perfume.allpouse.utils;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<Long> longList) {

        return longList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<Long> convertToEntityAttribute(String string) {

        if (string == null || string.equals("")) {
            return new ArrayList<Long>();
        } else {
            return  Arrays.stream(string.split(SPLIT_CHAR)).map(Long::valueOf).collect(Collectors.toList());
        }
    }
}
