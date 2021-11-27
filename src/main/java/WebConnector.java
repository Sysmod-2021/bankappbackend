import static spark.Spark.*;

import org.json.JSONObject;

import utils.StandardResponse;
import utils.StatusResponse;

public class WebConnector {
    public static void run(Bank root) {
        port(40080);
        get("/", (request, response) -> {
            return "Open Demo Bank".toString();
        });

        // Administrator
        post("/revokeTransaction", (request, response) -> {
            try {
                String transactionId = request.queryParams("transactionId");
                String reason = request.queryParams("reason");
                root.getAdministrator().revokeTransaction(transactionId, reason);

                StandardResponse resp = new StandardResponse(StatusResponse.SUCCESS);
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
