package com.bidhaa.utils;

import com.bidhaa.dto.ProductDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static ProductDto productDtoParser(String dtoString) {
        Pattern pattern = Pattern.compile("ProductDto\\[name=(.*?), description=(.*?), price=(.*?), quantity=(.*?), category=(.*?), tags=(.*?)\\]");
        Matcher matcher = pattern.matcher(dtoString);
        if (matcher.matches()) {
            String name = matcher.group(1);
            String description = matcher.group(2);
            String price = matcher.group(3);
            String quantity = matcher.group(4);
            String category = matcher.group(5);
            String tags = matcher.group(6);
            return new ProductDto(name,description,price,quantity,category,tags);
        } else {
            throw new IllegalArgumentException("Invalid input string format");
        }
    }
}
