import model.Bank;

import static spark.Spark.*;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.json.JSONObject;

import model.*;
import model.serializer.TransactionSerializer;
import model.serializer.CustomerSerializer;
import utils.StandardResponse;
import utils.StatusResponse;

public class WebConnector {
    public static void run(Bank root) {
        port(40080);
        get("/", (request, response) -> {
            return "Open Demo model.Bank".toString();
        });

        //Auth
        post("/authenticate", (request, response) -> {
            try{
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> body = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

                String email = body.get("email");
                String password = body.get("password");
                AuthResponse authResponse = root.authenticate(email, password);

                request.session(true).attribute("user_id", authResponse.getUserId());
                request.session(true).attribute("user_type", authResponse.getUserType());

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);

                return new JSONObject(resp);
            }
            catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());

                return new JSONObject(resp);
            }
            });
        post("/logout", (request, response) -> {
            try{
            request.session().invalidate();
                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);

                return new JSONObject(resp);
            }
            catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());

                return new JSONObject(resp);
            }
        });

        before("/administrators/*", (request, response) -> {
            String user_type = request.session(true).attribute("user_type");
            if ( user_type == null || !user_type.equals("administrator"))
                halt(401, "Access Denied");
        });
        before("/customers/*", (request, response) -> {
            String user_type = request.session(true).attribute("user_type");
            if ( user_type == null || !user_type.equals("customer"))
                halt(401, "Access Denied");
        });
        // Administrator
        put("/administrators/transactions/:transactionId/revocation", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

                String reason = map.get("reason");
                String transactionId = request.params(":transactionId");

                String administratorId = request.session().attribute("user_id");
                root.getAdministrator(administratorId).revokeTransaction(transactionId, reason);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Administrator freeze account
        post("/administrators/accounts/:accountId/frozen", (request, response) -> {
            try {
                String accountId = request.params(":accountId");

                String administratorId = request.session().attribute("user_id");
                root.getAdministrator(administratorId).setAccountStatus(accountId, "FROZEN");

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Administrator activate account
        post("/administrators/accounts/:accountId/active", (request, response) -> {
            try {
                String accountId = request.params(":accountId");

                String administratorId = request.session().attribute("user_id");
                root.getAdministrator(administratorId).setAccountStatus(accountId, "ACTIVE");

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });
        
        // Administrator creates customer account
        post("/administrators/accounts/create", (request, response) -> {
        	try {
        		ObjectMapper mapper = new ObjectMapper();
        		Map<String, String> requestBody = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

        		Customer newCustomer = root.createCustomer(
        			requestBody.get("firstName"),
        			requestBody.get("lastName"),
        			requestBody.get("email"),
        			requestBody.get("password"),
        			Double.parseDouble(requestBody.get("amount")), // initial balance
        			Currency.valueOf(requestBody.get("currency"))
        		);
                
                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);

                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());

                return new JSONObject(resp);
            }
        });

        // Administator
        post("/administrators/transactions/create", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> body = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

                String senderAccountId = body.get("senderAccountId");
                String receiverAccountId = body.get("receiverAccountId");
                double amount = Double.parseDouble(body.get("amount"));
                String description = body.get("description");

                String administratorId = request.session().attribute("user_id");

                Transaction transaction = root
                    .getAdministrator(administratorId)
                    .createTransaction(senderAccountId, receiverAccountId, amount, description);

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

        post("/administrators/transactions/seed", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> body = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

                String receiverAccountId = body.get("receiverAccountId");
                double amount = Double.parseDouble(body.get("amount"));
                String description = body.get("description");
                Currency currency = Currency.valueOf(body.get("currency"));

                String administratorId = request.session().attribute("user_id");

                Transaction transaction = root
                    .getAdministrator(administratorId)
                    .createSeedTransaction(receiverAccountId, amount, currency, description);

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
        post("/customers/transactions/create", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> body = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

                String receiverAccountId = body.get("receiverAccountId");
                double amount = Double.parseDouble(body.get("amount"));
                String description = body.get("description");

                String customerId = request.session().attribute("user_id");

                Transaction transaction = root.getCustomer(customerId).createTransaction(receiverAccountId, amount, description);

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
        // Customer
        get("/customers/details", (request, response) -> {
            try {
                String customerId = request.session().attribute("user_id");
                Customer customer = root.getCustomer(customerId);

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

        // CORS
        // https://gist.github.com/saeidzebardast/e375b7d17be3e0f4dddf
        options("/*",
        (request, response) -> {

            String accessControlRequestHeaders = request
                    .headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers",
                        accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request
                    .headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods",
                        accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        after((req, res) -> {
            res.type("application/json");
        });
    }
}
