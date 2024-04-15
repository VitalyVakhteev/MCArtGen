package com.imageapiplugin;

import org.bukkit.plugin.java.JavaPlugin;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ImageAPIPlugin extends JavaPlugin {
    private HttpServer server;

    @Override
    public void onEnable() {
        int port = 8001;
        startServer(port); // start
        if (server != null) {
            getLogger().info("ImageAPIPlugin has been enabled and HTTP server started on port " + port + ".");
        } else {
            getLogger().severe("Failed to start HTTP server.");
        }
    }

    @Override
    public void onDisable() {
        if (server != null) {
            server.stop(0); // stop
            getLogger().info("com.imageapiplugin.ImageAPIPlugin and its HTTP server have been disabled.");
        }
    }

    private void startServer(int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/upload", new ImageHandler(this));
            server.setExecutor(null); // default executor
            server.start();
        } catch (IOException e) {
            getLogger().severe("Could not start HTTP server: " + e.getMessage());
        }
    }
}
