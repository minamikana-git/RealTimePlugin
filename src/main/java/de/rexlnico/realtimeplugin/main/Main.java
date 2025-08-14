package de.rexlnico.realtimeplugin.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.rexlnico.realtimeplugin.commands.Commands;
import de.rexlnico.realtimeplugin.methodes.Messages;
import de.rexlnico.realtimeplugin.methodes.WorldManager;
import de.rexlnico.realtimeplugin.util.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static Metrics metrics;
    private static WorldManager worldManager;

    public static WorldManager getWorldManager() {
        return worldManager;
    }

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        Messages.load();
        Commands commands = new Commands();
        if (getCommand("realtime") != null) {
            getCommand("realtime").setExecutor(commands);
            getCommand("realtime").setTabCompleter(commands);
        }

        metrics = new Metrics(this, 11510);

        try {
            worldManager = new WorldManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentVersion() {
        return getDescription().getVersion();
    }

    public String getNewestVersion() {
        try {

            String body = new Scanner(new URL("https://api.spiget.org/v2/resources/69545/versions/latest")
                    .openStream(), "UTF-8").useDelimiter("\\A").next();
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            return json.get("name").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return getCurrentVersion();
        }
    }

    public static Metrics getMetrics() {
        return metrics;
    }

    @Override
    public void onDisable() {
        if (worldManager != null) {
            worldManager.disable();
        }
    }
}
