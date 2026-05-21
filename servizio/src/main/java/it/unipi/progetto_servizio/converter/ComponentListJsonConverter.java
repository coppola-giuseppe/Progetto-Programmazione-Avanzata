/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio.converter;

/**
 *
 * @author coppo
 */
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.lang.reflect.Type;
import java.util.List;

@Converter
public class ComponentListJsonConverter implements AttributeConverter<List<String>, String> {

    private final Gson gson = new Gson();
    
    //specify to JSON that i want a List of Strings
    private final Type type = new TypeToken<List<String>>(){}.getType();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null) {
            return null;
        }
        return gson.toJson(attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return gson.fromJson(dbData, type);
        } catch (JsonSyntaxException jse) {
            throw new RuntimeException("Error during conversion from JSON to List<String>.", jse);
        }
    }
}
