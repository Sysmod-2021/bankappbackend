package model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import model.Customer;

public class CustomerAndAccountSerializer extends StdSerializer<Customer> {
 
    public CustomerAndAccountSerializer() {
        this(null);
    }
    
    public CustomerAndAccountSerializer(Class<Customer> t) {
        super(t);
    }

    @Override
    public void serialize(
        Customer value, JsonGenerator jgen, SerializerProvider provider) 
        throws IOException, JsonProcessingException {

        jgen.writeStartObject();
            jgen.writeStringField("customerId", value.getId());
            jgen.writeStringField("firstName", value.getFirstName());
            jgen.writeStringField("lastName", value.getLastName());
            jgen.writeStringField("password", value.getPassword());
            jgen.writeStringField("accountId", value.getAccount().getId());
            jgen.writeNumberField("accountBalance", value.getAccount().getBalance());
            jgen.writeStringField("accountCurrency", value.getAccount().getCurrency().toString());
        jgen.writeEndObject();
    }   
}
