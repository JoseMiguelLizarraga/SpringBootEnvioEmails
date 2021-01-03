package com.proyecto;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DeserializarFechas extends JsonDeserializer<Date>
{
		
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException 
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = jsonParser.getText();
        
        try {
            return format.parse(date);  // Asi, las fechas recibidas tendran el formato yyyy-MM-dd
        } 
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
