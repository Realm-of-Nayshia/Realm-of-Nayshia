package com.stelios.cakenaysh.Npc;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class NpcBuilder {

    private String name;
    private UUID uuid;
    private String skin;
    private EntityType type;
    private String world;
    private Location location;


    public NpcBuilder (String name, String skin, EntityType type, String world){
        this.name = name;
        this.skin = skin;
        this.type = type;
        this.world = world;
    }

    //getters
    public String getName(){return this.name;}
    public UUID getUuid(){return this.uuid;}
    public String getSkin(){return this.skin;}
    public EntityType getType(){return this.type;}
    public String getWorld(){return this.world;}
    public Location getLocation(){return this.location;}

    //setters
    public void setName(String name){this.name = name;}
    public void setUuid(UUID uuid){this.uuid = uuid;}
    public void setSkin(String skin){this.skin = skin;}
    public void setType(EntityType type){this.type = type;}
    public void setWorld(String world){this.world = world;}
    public void setLocation(Location location){this.location = location;}

}
