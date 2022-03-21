package com.yugabyte.app.yugastore.util;

import javax.persistence.AttributeConverter;
import java.util.*;

public class StringSetConverter  implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> list) {
        return String.join(",", list);

    }

    @Override
    public Set<String> convertToEntityAttribute(String joined) {
        if(joined == null || joined.isEmpty()){
            return  new HashSet<>();
        }
        joined = joined.replaceAll("\\{","");
        joined = joined.replaceAll("\\}","");
        joined = joined.replaceAll("\\[","");
        joined = joined.replaceAll("\\]","");


        return new HashSet<>(Arrays.asList(joined.split(",")));
    }


}
