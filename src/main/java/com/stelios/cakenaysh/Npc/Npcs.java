package com.stelios.cakenaysh.Npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public enum Npcs {

    TEST_NPC(new NpcBuilder("Test NPC",null, EntityType.PLAYER,"world"));

    private final NpcBuilder npcBuilder;

    Npcs(NpcBuilder npcBuilder){
        this.npcBuilder = npcBuilder;
    }

    //getters
    public NpcBuilder getNpcBuilder(){return this.npcBuilder;}


    //create/save a npc to the npc registry
    public void createNpc(Location location){

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(this.getNpcBuilder().getType(), this.getNpcBuilder().getName(), this.getNpcBuilder().getLocation());

        npc.spawn(location);
        this.getNpcBuilder().setLocation(location);

        this.getNpcBuilder().setUuid(npc.getUniqueId());
        System.out.println(npc.getUniqueId());

    }



}
