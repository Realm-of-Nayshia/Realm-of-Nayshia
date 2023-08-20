package com.stelios.cakenaysh.Listeners.Server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;

public class ServerListPingListener implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent e){

        e.setMaxPlayers(10);
        e.motd(Component.text("This is a test server!",
                        TextColor.color(0,150,255))
                .decoration(TextDecoration.BOLD, true));

        try {
            e.setServerIcon(Bukkit.loadServerIcon(new File("icon.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
