package com.imageapiplugin;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ImageHandler implements HttpHandler {
    private final Gson gson = new Gson();
    private MosaicCreator mosaicCreator;

    public ImageHandler() throws IOException {
        try {
            List<JImage> dependentImages = loadDependentImages();
            this.mosaicCreator = new MosaicCreator(dependentImages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<JImage> loadDependentImages() {
        List<JImage> images = new ArrayList<>();
        try {
            URL dirURL = getClass().getClassLoader().getResource("mc_images");
            if (dirURL != null && dirURL.getProtocol().equals("file")) {
                /* A file path: easy enough */
                return loadImagesFromDirectory(new File(dirURL.toURI()));
            }

            if (dirURL == null) {
            /* In case of a jar file, we can't actually find a directory.
               Have to assume the same jar as class. */
                String me = getClass().getName().replace(".", "/") + ".class";
                dirURL = getClass().getClassLoader().getResource(me);
            }

            if (dirURL.getProtocol().equals("jar")) {
                /* A JAR path */
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); // strip out only the JAR file
                try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
                    Enumeration<JarEntry> entries = jar.entries(); // gives ALL entries in jar
                    while (entries.hasMoreElements()) {
                        String name = entries.nextElement().getName();
                        if (name.startsWith("mc_images/") && (name.endsWith(".png"))) {
                            // Ensure it's not a directory
                            if (!name.endsWith("/")) {
                                URL fileURL = getClass().getClassLoader().getResource(name);
                                if (fileURL != null) {
                                    try {
                                        images.add(new JImage(fileURL));
                                    } catch (IOException e) {
                                        System.err.println("Error loading image: " + name + " from JAR");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    private List<JImage> loadImagesFromDirectory(File dir) throws IOException {
        List<JImage> images = new ArrayList<>();
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                        images.add(new JImage(file.toURI().toURL()));
                    }
                }
            }
        }
        return images;
    }

    private void saveImage(BufferedImage image, String imageName) throws IOException {
        File outputDir = new File("plugins/ImageAPIPlugin/output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(outputDir, imageName);
        ImageIO.write(image, "png", outputFile);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(java.util.stream.Collectors.joining("\n"));

            Map<String, String> params = gson.fromJson(body, Map.class);
            String imageUrl = params.get("image_url");

            try {
                URL url = new URL(imageUrl);
                JImage hostImage = new JImage(url);
                BufferedImage mosaic = mosaicCreator.createMosaic(hostImage);
                saveImage(mosaic, "mosaic.png");
                sendTextResponse(exchange, "Image processed successfully.", 200);
//                sendImageResponse(exchange, mosaic);
            } catch (Exception e) {
                e.printStackTrace(); // Proper error handling later
                sendTextResponse(exchange, "Error processing image.", 503);
            }
        } else {
            sendTextResponse(exchange, "Method Not Allowed", 405);
        }
    }

    private void sendTextResponse(HttpExchange exchange, String responseText, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, responseText.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseText.getBytes());
        os.close();
    }
}
