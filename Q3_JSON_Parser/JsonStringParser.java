package com.company;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;

public class JsonStringParser {

    // Main method to test JSON string parsing
    public static void main(String[] args) {
        try {
            String jsonString = """
                    {
                        "name": "Shivam Singh",
                        "role": "developer",
                        "age": "28 years",
                        "contact": 7985648963,
                        "isPermanent": true,
                        "dateOfJoining": "12-08-2021",
                        "salary": 1400000,
                        "skills": ["Java", "Spring", "JSON", "Angular", "Linux", "PostgreSQL"],
                        "address": {
                            "city": "Mumbai",
                            "zip": 400079
                        }
                    }
                    """;

            Object result = parseJsonString(jsonString);

            System.out.println(result); // Outputs: Java representation of JSON
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Parse JSON string into an Object
    private static Object parseJsonString(String jsonString) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper(JsonFactory.builder().enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS).build());

        // Arbitrary precision for numeric values
        //objectMapper.enable(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS);



        // Parse JSON string into JsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Convert JsonNode to corresponding Java Object
        return convertJsonNodeToObject(jsonNode);
    }

    private static Object convertJsonNodeToObject(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            // Handle JSON Object
            ObjectNode objectNode = (ObjectNode) jsonNode;
            return new ObjectMapper().convertValue(objectNode, Map.class);
        } else if (jsonNode.isArray()) {
            // Handle JSON Array
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            return new ObjectMapper().convertValue(arrayNode, List.class);
        } else if (jsonNode.isBigInteger()) {
            // Handle Arbitrary Precision Integer
            return jsonNode.bigIntegerValue();
        } else if (jsonNode.isFloatingPointNumber()) {
            // Handle Arbitrary Precision Floating Point
            return jsonNode.decimalValue();
        } else if (jsonNode.isTextual()) {
            // Handle String
            return jsonNode.asText();
        } else if (jsonNode.isBoolean()) {
            // Handle Boolean
            return jsonNode.asBoolean();
        } else if (jsonNode.isNull()) {
            // Handle Null
            return null;
        } else {
            throw new IllegalArgumentException("Unsupported JSON node type: " + jsonNode.getNodeType());
        }
    }
}
