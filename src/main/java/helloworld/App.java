package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Object, Object> {

    static double staticRandom;

    static {
        staticRandom = Math.random();
    }

    private final double constructorRandom;

    public App() {
        constructorRandom = Math.random();
    }

    public Object handleRequest(final Object input, final Context context) {
        double invocationRandom = Math.random();

        String output = String.format(
                "{ \"static random\": \"%f\", \"constructorRandom\": \"%f\", \"invocationRandom\": \"%f\"}",
                staticRandom, constructorRandom, invocationRandom);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        return new GatewayResponse(output, headers, 200);
    }
}
