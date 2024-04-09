package com.imageapiplugin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ImageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            Map<String, String> params = parsePostParameters(exchange);

            String imageUrl = params.get("image_url");
            String coordinates = params.get("coordinates"); // coordinates should be in "X,Y" format

            // Todo: handle the imageUrl and coordinates with paint method

            // Dummy response
            String response = "Received image URL: " + imageUrl + " and coordinates: " + coordinates;
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private Map<String, String> parsePostParameters(HttpExchange exchange) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        String[] pairs = requestBody.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            parameters.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return parameters;
    }
}
