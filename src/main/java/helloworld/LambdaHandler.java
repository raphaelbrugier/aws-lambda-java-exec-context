package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * LambdaHandler for requests to Lambda function.
 */
public class LambdaHandler implements RequestHandler<Object, Object> {

    static double STATIC_RANDOM;

    static {
        STATIC_RANDOM = Math.random(); // (1) Value generated in the static block
    }

    private final double constructorRandom;

    public LambdaHandler() {
        this.constructorRandom = Math.random(); // (2) Value generated in the LambdaHandler constructor
    }

    public Object handleRequest(final Object input, final Context context) {
        double invocationRandom = Math.random(); // (3) Value generated in the LambdaHandler method for each invocation

        String output = String.format(
                "{ \"1 - static value\": \"%f\", \" 2 - constructor value\": \"%f\", \"3 - invocation value\": \"%f\"}",
                STATIC_RANDOM, this.constructorRandom, invocationRandom);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        return new GatewayResponse(output, headers, 200);
    }
}
