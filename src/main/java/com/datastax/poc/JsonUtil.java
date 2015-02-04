package com.datastax.poc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by patrick on 03/02/15.
 */
public final class JsonUtil {

    public static ObjectNode parseJson(String str) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;
        try {
            json = mapper.readTree(str);
        } catch (IOException e) {
            throw new RuntimeException("Could not parse json :" + str, e);
        }
        return (ObjectNode) json;
    }

    public static void mergeJson(ObjectNode primary, ObjectNode backup) {
        Iterator<String> fieldNames = backup.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode primaryValue = primary.get(fieldName);
            if (primaryValue == null) {
                JsonNode backupValue = backup.get(fieldName).deepCopy();
                primary.set(fieldName, backupValue);
            } else if (primaryValue.isObject()) {
                JsonNode backupValue = backup.get(fieldName);
                if (backupValue.isObject()) {
                    mergeJson((ObjectNode) primaryValue, (ObjectNode) backupValue.deepCopy());
                }
            }
        }
    }


}
