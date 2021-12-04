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

        // Administrator
        put("/transactions/:transactionId/revocation", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

                String reason = map.get("reason");
                String transactionId = request.params(":transactionId");
                root.getAdministrator().revokeTransaction(transactionId, reason);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Get account status
        get("/accounts/:accountId/status", (request, response) -> {
            try {
                String accountId = request.params(":accountId");
                Account account = root.getAccountById(accountId);

                String status = account.getStatus();
                String message = "{'result':'" + status + "'}";

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, message);
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Administrator freeze account
        post("/accounts/frozen", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> requestBody = mapper.readValue(request.body(), new TypeReference<>() {});

                String accountId = requestBody.get("accountId");
                root.getAdministrator().setAccountStatus(accountId, "FROZEN");

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Administrator activate account
        post("/accounts/active", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> requestBody = mapper.readValue(request.body(), new TypeReference<>() {});

                String accountId = requestBody.get("accountId");
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
        post("/administrators/:administratorId/transactions/create", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> body = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

                String senderAccountId = body.get("senderAccountId");
                String receiverAccountId = body.get("receiverAccountId");
                double amount = Double.parseDouble(body.get("amount"));
                String description = body.get("description");

                String administratorId = request.params(":administratorId");

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

        post("/administrators/:administratorId/transactions/seed", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> body = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

                String receiverAccountId = body.get("receiverAccountId");
                double amount = Double.parseDouble(body.get("amount"));
                String description = body.get("description");
                Currency currency = Currency.valueOf(body.get("currency"));

                String administratorId = request.params(":administratorId");

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
        post("/customers/:customerId/transactions/create", (request, response) -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> body = mapper.readValue(request.body(), new TypeReference<Map<String, String>>(){});

                String receiverAccountId = body.get("receiverAccountId");
                double amount = Double.parseDouble(body.get("amount"));
                String description = body.get("description");

                String customerId = request.params(":customerId");

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

        // Administrator/customer saves bank
        post("/administrators/bank/save", (request, response) -> {
            try {
                root.saveData();
    
                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
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
