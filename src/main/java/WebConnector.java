import static spark.Spark.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.json.JSONObject;

import model.*;
import model.serializer.CustomerSerializer;
import utils.StandardResponse;
import utils.StatusResponse;

public class WebConnector {
    public static void run(Bank root) {
        port(40080);
        get("/", (request, response) -> {
            return "Open Demo Bank".toString();
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
