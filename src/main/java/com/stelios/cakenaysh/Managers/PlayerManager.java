package com.stelios.cakenaysh.Managers;

import com.stelios.cakenaysh.Util.CustomPlayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private final HashMap<UUID, CustomPlayer> customPlayers = new HashMap<>();

    //gets the custom player's UUID
    public CustomPlayer getCustomPlayer(UUID uuid){
        return customPlayers.get(uuid);
    }

    //adds a custom player to the hashmap
    //@param uuid: the UUID of the player
    //@param customPlayer: the custom player object
    public void addCustomPlayer(UUID uuid, CustomPlayer customPlayer){
        customPlayers.put(uuid, customPlayer);
    }

    //removes a custom player from the hashmap
    public void removeCustomPlayer(UUID uuid){
        customPlayers.remove(uuid);
    }

}
