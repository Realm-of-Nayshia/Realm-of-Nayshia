package com.stelios.cakenaysh.AbilityCreation;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.ClickType;

public enum CustomAbilities {


    SPARTAN_WRATH(Component.text("Spartan Wrath"), Component.text("THIS IS SPARTA!"),
            ClickType.RIGHT,false,false),
    GRADUAL_SET_DAY(Component.text("Gradual Set Day"), Component.text("Gradually sets the time to day."),
            ClickType.RIGHT,false,true),



    ;


    private final Component name;
    private final Component description;
    private final ClickType clickType;
    private final Boolean hasSpecialCases;
    private final Boolean removeItem;

    CustomAbilities(Component name, Component description, ClickType clickType, Boolean hasSpecialCases, Boolean removeItem){
        this.name = name;
        this.description = description;
        this.clickType = clickType;
        this.hasSpecialCases = hasSpecialCases;
        this.removeItem = removeItem;
    }

    //getters
    public Component getName(){return this.name;}
    public Component getDescription(){return this.description;}
    public ClickType getClickType(){return this.clickType;}
    public Boolean getHasSpecialCases(){return this.hasSpecialCases;}
    public Boolean getRemoveItem(){return this.removeItem;}




}
