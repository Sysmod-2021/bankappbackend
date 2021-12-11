package model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import model.Transaction;

import java.io.IOException;

public class TransactionSerializer extends StdSerializer<Transaction> {
    public TransactionSerializer() {
        this(null);
    }

    public TransactionSerializer(Class<Transaction> t) {
        super(t);
    }

    @Override
    public void serialize(
            Transaction value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeStringField("transactionId", value.getId());
        if(value.getSource() != null) {
            jgen.writeStringField("sourceID", value.getSource().getId());
            jgen.writeStringField("sourceIban", value.getSource().getIban());
        }
        jgen.writeStringField("destId", value.getDestination().getId());
        jgen.writeStringField("destIban", value.getDestination().getIban());
        jgen.writeNumberField("amount", value.getAmount());
        jgen.writeStringField("description", value.getDescription());
        jgen.writeStringField("rejectionDescription", value.getRejectionDescription());
        jgen.writeStringField("status", value.getStatus().toString());
        jgen.writeStringField("currency", value.getCurrency().toString());
        jgen.writeStringField("timestamp", value.getTimestamp().toString());
        jgen.writeEndObject();
    }
}