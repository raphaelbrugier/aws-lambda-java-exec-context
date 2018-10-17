package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class Handler implements RequestHandler<Object, Object> {

    static double staticRandom;

    static {
        staticRandom = Math.random(); // (1) Value generated in the static block
    }

    private final double constructorRandom;

    public Handler() {
        constructorRandom = Math.random(); // (2) Value generated in the Handler constructor
    }

    public Object handleRequest(final Object input, final Context context) {
        double invocationRandom = Math.random(); // (3) Value generated in the Handler method for each invocation

        String output = String.format(
                "{ \"1 - static value\": \"%f\", \" 2 - constructor value\": \"%f\", \"3 - invocation value\": \"%f\"}",
                staticRandom, constructorRandom, invocationRandom);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        return new GatewayResponse(output, headers, 200);
    }
}
