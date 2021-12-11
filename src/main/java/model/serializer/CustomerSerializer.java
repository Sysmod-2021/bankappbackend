package model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import model.Customer;
import model.Transaction;

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

        final ObjectMapper mapper = (ObjectMapper)jgen.getCodec();
        SimpleModule module =  new SimpleModule();
        module.addSerializer(Transaction.class, new TransactionSerializer());
        mapper.registerModule(module);

        jgen.writeStartObject();
            jgen.writeArrayFieldStart("transactions");
                for (Transaction t:value.getAccount().getAllTransactionsWithSignAndOrderedDesc()) {
                    mapper.writeValue(jgen, t);
                }
            jgen.writeEndArray();

            jgen.writeStringField("customerId", value.getId());
            jgen.writeStringField("accountId", value.getAccount().getId());
            jgen.writeStringField("accountIban", value.getAccount().getIban());
            jgen.writeStringField("firstName", value.getFirstName());
            jgen.writeStringField("lastName", value.getLastName());
            jgen.writeNumberField("balance", value.getAccount().getBalance());
            jgen.writeStringField("currency", value.getAccount().getCurrency().toString());
        jgen.writeEndObject();
    }
}
