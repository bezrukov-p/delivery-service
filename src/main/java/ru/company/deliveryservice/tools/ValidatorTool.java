package ru.company.deliveryservice.tools;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Component
public class ValidatorTool {
    @Autowired
    ObjectMapper mapper;

    public boolean isValid(JsonNode json, String typeOfJson){
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        JsonNode schemaNode = null;
        ClassPathResource resource = new ClassPathResource("json-schema.json");
        try {
            InputStream schema = resource.getInputStream();
            schemaNode = mapper.readTree(schema).get("definitions").get(typeOfJson);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        JsonSchema jsonSchema = schemaFactory.getSchema(schemaNode);
        Set<ValidationMessage> resValid = jsonSchema.validate(json);
        if (resValid.size() > 0) {
            /*for (ValidationMessage message: resValid){
                System.out.println(message.getMessage());
            }*/
            return false;
        }
        else
            return true;
    }
}
