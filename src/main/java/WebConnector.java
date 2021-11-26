import model.Bank;

import static spark.Spark.*;

public class WebConnector {
    public static void run(Bank root) {
        port(40080);
        get("/", (request, response) -> {
            return "Open Demo model.Bank".toString();
        });
    }
}
