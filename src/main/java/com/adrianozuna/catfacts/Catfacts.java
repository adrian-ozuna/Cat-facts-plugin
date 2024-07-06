package com.adrianozuna.catfacts;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public final class Catfacts extends JavaPlugin implements Listener {

    public String getFact() {

        String fact = "";

        try {
            // Create a new HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create a GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://catfact.ninja/fact"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the response body as JSON
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(response.body());

            // Extract the "fact" field
            fact = (String) jsonObject.get("fact");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fact;
    }

    @Override
    public void onEnable() {
        BukkitScheduler scheduler = this.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

                if (!players.isEmpty()) {
                    Bukkit.broadcast(
                            Component.text("< Mr. Katt > ")
                                    .color(TextColor.color(NamedTextColor.YELLOW))
                                    .append(
                                            Component.text("Did you know that " + getFact())
                                                    .color(TextColor.color(NamedTextColor.WHITE))
                                    )
                    );
                }
            }
        }, 0L, 18000L);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(
                Component.text("HEHEHELLOOO " + event.getPlayer().getName() + "!!!")
        );

        event.getPlayer().sendMessage(
                Component.text("Did you know that " + getFact())
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
