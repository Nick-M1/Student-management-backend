package com.tutorial.demo.yeargroup;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class YearGroupConverter implements Converter<String, YearGroupEnum> {
    @Override
    public YearGroupEnum convert(String value) {
        return YearGroupEnum.decode(Integer.parseInt(value));
    }
}