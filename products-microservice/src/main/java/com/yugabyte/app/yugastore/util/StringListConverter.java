package com.yugabyte.app.yugastore.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        return String.join(",", list);

    }

    @Override
    public List<String> convertToEntityAttribute(String joined) {
        if(joined == null || joined.isEmpty()){
            return  new ArrayList<>();
        }
        joined = joined.replaceAll("\\{","");
        joined = joined.replaceAll("\\}","");
        joined = joined.replaceAll("\\[","");
        joined = joined.replaceAll("\\]","");
        return new ArrayList<>(Arrays.asList(joined.split(",")));
    }

}
