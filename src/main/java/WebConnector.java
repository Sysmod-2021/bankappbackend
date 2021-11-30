import model.Bank;

import static spark.Spark.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import model.serializer.TransactionSerializer;
import org.json.JSONObject;

import model.*;
import model.serializer.CustomerSerializer;
import utils.StandardResponse;
import utils.StatusResponse;

public class WebConnector {
    public static void run(Bank root) {
        port(40080);
        get("/", (request, response) -> {
            return "Open Demo model.Bank".toString();
        });

        // Administrator
        put("/transactions/:transactionId/revocation", (request, response) -> {
            try {
                String transactionId = request.params(":transactionId");
                String reason = request.queryParams("reason");
                root.getAdministrator().revokeTransaction(transactionId, reason);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Administrator freeze account
        post("/accounts/:accountId/frozen", (request, response) -> {
            try {
                String accountId = request.params(":accountId");
                root.getAdministrator().setAccountStatus(accountId, "FROZEN");

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Administrator activate account
        post("/accounts/:accountId/active", (request, response) -> {
            try {
                String accountId = request.params(":accountId");
                root.getAdministrator().setAccountStatus(accountId, "ACTIVE");

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });
        
        // Administrator creates customer account
        post("/accounts/create", (request, response) -> {
        	try {        		
        		String administratorId = request.params(":administratorId");
        		
        		Customer newCustomer = root.createCustomer(
        			request.queryParams("firstName"),
        			request.queryParams("lastName"),
        			request.queryParams("email"),
        			request.queryParams("password"),
        			Double.parseDouble(request.queryParams("amount")), // initial balance
        			Currency.valueOf(request.queryParams("currency"))
        		);
    
        		ObjectMapper mapper = new ObjectMapper();

                SimpleModule module = new SimpleModule();
                module.addSerializer(Customer.class, new CustomerSerializer());
                mapper.registerModule(module);

                String serializedCustomer = mapper.writeValueAsString(newCustomer);
                
                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedCustomer));

                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());

                return new JSONObject(resp);
            }
        });

        // Administator
        post("/administrators/:administratorId/transactions/create", (request, response) -> {
            try {
                String administratorId = request.params(":administratorId");
                String senderAccountId = request.queryParams("senderAccountId");
                String receiverAccountId = request.queryParams("receiverAccountId");
                double amount = Double.parseDouble(request.queryParams("amount"));
                String description = request.queryParams("description");

                Transaction transaction = root.getAdministrator(administratorId).createTransaction(senderAccountId, receiverAccountId, amount, description);


                ObjectMapper mapper = new ObjectMapper();

                SimpleModule module = new SimpleModule();
                module.addSerializer(Transaction.class, new TransactionSerializer());
                mapper.registerModule(module);

                String serializedTransaction = mapper.writeValueAsString(transaction);
                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedTransaction));
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        post("/administrators/:administratorId/transactions/seed", (request, response) -> {
            try {
                String administratorId = request.params(":administratorId");
                String receiverAccountId = request.queryParams("receiverAccountId");
                double amount = Double.parseDouble(request.queryParams("amount"));
                String description = request.queryParams("description");
                Currency currency = Currency.valueOf(request.queryParams("currency"));

                Transaction transaction = root.getAdministrator(administratorId).createSeedTransaction(receiverAccountId, amount, currency, description);

                ObjectMapper mapper = new ObjectMapper();

                SimpleModule module = new SimpleModule();
                module.addSerializer(Transaction.class, new TransactionSerializer());
                mapper.registerModule(module);

                String serializedTransaction = mapper.writeValueAsString(transaction);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedTransaction));
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });


        // Customer
        post("/customers/:customerId/transactions/create", (request, response) -> {
            try {
                String customerId = request.params(":customerId");
                String receiverAccountId = request.queryParams("receiverAccountId");
                double amount = Double.parseDouble(request.queryParams("amount"));
                String description = request.queryParams("description");

                Transaction transaction = root.getCustomer(customerId).createTransaction(receiverAccountId, amount, description);

                ObjectMapper mapper = new ObjectMapper();

                SimpleModule module = new SimpleModule();
                module.addSerializer(Transaction.class, new TransactionSerializer());
                mapper.registerModule(module);

                String serializedTransaction = mapper.writeValueAsString(transaction);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedTransaction));
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });
        // Customer
        get("/customers/:customerEmail/details", (request, response) -> {
            try {
                String customerEmail = request.params(":customerEmail");
                Customer customer = root.getCustomerByEmail(customerEmail);

                ObjectMapper mapper = new ObjectMapper();

                SimpleModule module = new SimpleModule();
                module.addSerializer(Customer.class, new CustomerSerializer());
                mapper.registerModule(module);

                String serializedCustomer = mapper.writeValueAsString(customer);
    
                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedCustomer));
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        after((req, res) -> {
            res.type("application/json");
        });
    }
}
