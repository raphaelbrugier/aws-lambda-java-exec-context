package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Object, Object> {

    static double staticRandom = Math.random();

    static {
        System.out.println("Evaluating static bloc. static random = " + staticRandom);
    }

    private final double randomFromConstructor;

    public App() {
        randomFromConstructor = Math.random();
        System.out.println("Evaluating constructor. static random = " + staticRandom + "     randomFromConstructor = " + this.randomFromConstructor);
    }

    public Object handleRequest(final Object input, final Context context) {
        double randomFromHandler = Math.random();
        System.out.println("Evaluating handler. static random=" + staticRandom + "     randomFromConstructor=" + this.randomFromConstructor + "     randomFromHandler=" + randomFromHandler);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        String output = String.format("{ \"static random\": \"%f\", \"randomFromConstructor\": \"%f\", \"randomFromHandler\": \"%f\" }", staticRandom, randomFromConstructor, randomFromHandler);
        return new GatewayResponse(output, headers, 200);
    }
}
