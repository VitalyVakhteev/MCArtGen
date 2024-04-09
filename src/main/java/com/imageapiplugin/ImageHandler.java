package com.imageapiplugin;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ImageHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(java.util.stream.Collectors.joining("\n"));

            Map<String, String> params = gson.fromJson(body, Map.class);

            String imageUrl = params.get("image_url");
            String coordinates = params.get("coordinates"); // Assuming coordinates are in "X,Y" format

            // Todo: handle the imageUrl and coordinates accordingly

            // Dummy response for demonstration
            String response = "Received image URL: " + imageUrl + " and coordinates: " + coordinates;
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            // Respond with Method Not Allowed if not a POST request
            exchange.sendResponseHeaders(405, -1);
        }
    }
}
