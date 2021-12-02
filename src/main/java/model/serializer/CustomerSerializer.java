package model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import model.Customer;

public class CustomerSerializer extends StdSerializer<Customer> {

    public CustomerSerializer() {
        this(null);
    }
    
    public CustomerSerializer(Class<Customer> t) {
        super(t);
    }

    @Override
    public void serialize(
        Customer value, JsonGenerator jgen, SerializerProvider provider) 
        throws IOException, JsonProcessingException {

        //TODO: add transaction list
    
        jgen.writeStartObject();
        jgen.writeStringField("customerId", value.getId());
        jgen.writeStringField("accountId", value.getAccount().getId());
        jgen.writeStringField("firstName", value.getFirstName());
        jgen.writeStringField("lastName", value.getLastName());
        jgen.writeNumberField("balance", value.getAccount().getBalance());
        jgen.writeStringField("curency", value.getAccount().getCurrency().toString());
        jgen.writeEndObject();
    }
} 
