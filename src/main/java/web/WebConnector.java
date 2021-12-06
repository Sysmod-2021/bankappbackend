package web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import model.*;
import web.dto.*;
import model.serializer.CustomerSerializer;
import model.serializer.CustomerAndAccountSerializer;
import model.serializer.TransactionSerializer;
import org.json.JSONObject;

import java.util.Map;

import static spark.Spark.*;

public class WebConnector {
    public static final int BAD_REQUEST = 400;
    private static ObjectMapper objMapper = new ObjectMapper();

    public static void run(Bank root) {
        port(40080);
        get("/", (request, response) -> {
            return "Open Demo model.Bank".toString();
        });

        //Auth
        post("/authenticate", (request, response) -> {
            try {
                Map<String, String> body = objMapper.readValue(request.body(), new TypeReference<Map<String, String>>() {
                });

                String email = body.get("email");
                String password = body.get("password");
                AuthResponse authResponse = root.authenticate(email, password);

                request.session(true).attribute("user_id", authResponse.getUserId());
                request.session(true).attribute("user_type", authResponse.getUserType());

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);

                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());

                return new JSONObject(resp);
            }
        });
        post("/logout", (request, response) -> {
            try {
                request.session().invalidate();
                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);

                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());

                return new JSONObject(resp);
            }
        });

        before("/administrators/*", (request, response) -> {
            String user_type = request.session(true).attribute("user_type");
            if (user_type == null || !user_type.equals("administrator"))
                halt(401, "Access Denied");
        });
        before("/customers/*", (request, response) -> {
            String user_type = request.session(true).attribute("user_type");
            if (user_type == null || !user_type.equals("customer"))
                halt(401, "Access Denied");
        });

        // Administrator
        put("/administrators/transactions/:transactionId/revocation", (request, response) -> {
            try {
                Map<String, String> map = objMapper.readValue(request.body(), new TypeReference<Map<String, String>>() {
                });

                String reason = map.get("reason");
                String transactionId = request.params(":transactionId");

                String administratorId = request.session().attribute("user_id");
                root.getAdministrator(administratorId).revokeTransaction(transactionId, reason);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Get account status
        get("/customers/accounts/status", (request, response) -> {
            try {
                String customerId = request.session().attribute("user_id");
                Customer customer = root.getCustomer(customerId);
                Account account = customer.getAccount();

                String status = account.getStatus();
                String message = "{'result':'" + status + "'}";

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, message);
                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Administrator freeze account
        post("/administrators/accounts/frozen", (request, response) -> {
            try {
                Map<String, String> requestBody = objMapper.readValue(request.body(), new TypeReference<Map<String, String>>() {
                });

                String accountId = requestBody.get("accountId");
                String administratorId = request.session().attribute("user_id");

                root.getAdministrator(administratorId).setAccountStatus(accountId, "FROZEN");

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Administrator activate account
        post("/administrators/accounts/:accountId/active", (request, response) -> {
            try {
                Map<String, String> requestBody = objMapper.readValue(request.body(), new TypeReference<Map<String, String>>() {
                });

                String accountId = requestBody.get("accountId");

                String administratorId = request.session().attribute("user_id");
                root.getAdministrator(administratorId).setAccountStatus(accountId, "ACTIVE");

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Administrator creates customer account
        post("/administrators/accounts/create", (request, response) -> {
            try {
                CreateAccountRequest accRequest = objMapper.readValue(request.body(), CreateAccountRequest.class);
                String administratorId = request.session().attribute("user_id");

                Customer customer = root
                    .getAdministrator(administratorId)
                    .createCustomerAndAccount(
                        accRequest.getFirstName(),
                        accRequest.getLastName(),
                        accRequest.getEmail(),
                        accRequest.getCurrency());

                SimpleModule module = new SimpleModule();
                module.addSerializer(Customer.class, new CustomerAndAccountSerializer());
                objMapper.registerModule(module);

                String serializedCustomer = objMapper.writeValueAsString(customer);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedCustomer));

                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());

                return new JSONObject(resp);
            }
        });

        // Administator
        post("/administrators/transactions/create", (request, response) -> {
            try {
                String administratorId = request.session().attribute("user_id");

                CreateTransactionRequest transactionRequest = objMapper.readValue(request.body(), CreateTransactionRequest.class);
                Transaction transaction = root
                    .getAdministrator(administratorId)
                    .createTransaction(
                        transactionRequest.getSenderAccountId(),
                        transactionRequest.getReceiverAccountId(), 
                        transactionRequest.getAmount(),
                        transactionRequest.getDescription());

                SimpleModule module = new SimpleModule();
                module.addSerializer(Transaction.class, new TransactionSerializer());
                objMapper.registerModule(module);

                String serializedTransaction = objMapper.writeValueAsString(transaction);
                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedTransaction));
                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        post("/administrators/transactions/seed", (request, response) -> {
            try {
                String administratorId = request.session().attribute("user_id");

                CreateTransactionRequest transactionRequest = objMapper.readValue(request.body(), CreateTransactionRequest.class);
                Transaction transaction = root
                    .getAdministrator(administratorId)
                    .createSeedTransaction(
                        transactionRequest.getReceiverAccountId(),
                        transactionRequest.getAmount(), 
                        transactionRequest.getCurrency(),
                        transactionRequest.getDescription());

                SimpleModule module = new SimpleModule();
                module.addSerializer(Transaction.class, new TransactionSerializer());
                objMapper.registerModule(module);

                String serializedTransaction = objMapper.writeValueAsString(transaction);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedTransaction));
                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });


        // Customer
        post("/customers/transactions/create", (request, response) -> {
            try {
                String customerId = request.session().attribute("user_id");

                CreateTransactionRequest transactionRequest = objMapper.readValue(request.body(), CreateTransactionRequest.class);
                Transaction transaction = root.getCustomer(customerId).createTransaction(
                    transactionRequest.getReceiverAccountId(),
                    transactionRequest.getAmount(),
                    transactionRequest.getDescription());

                SimpleModule module = new SimpleModule();
                module.addSerializer(Transaction.class, new TransactionSerializer());
                objMapper.registerModule(module);

                String serializedTransaction = objMapper.writeValueAsString(transaction);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedTransaction));
                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
                StandardResponse resp = new StandardResponse(StatusResponse.ERROR, e.getMessage());
                return new JSONObject(resp);
            }
        });

        // Customer
        get("/customers/details", (request, response) -> {
            try {
                String customerId = request.session().attribute("user_id");
                Customer customer = root.getCustomer(customerId);

                SimpleModule module = new SimpleModule();
                module.addSerializer(Customer.class, new CustomerSerializer());
                objMapper.registerModule(module);

                String serializedCustomer = objMapper.writeValueAsString(customer);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS, new JSONObject(serializedCustomer));
                return new JSONObject(resp);
            } catch (Exception e) {
                response.status(BAD_REQUEST);
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
                response.status(BAD_REQUEST);
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