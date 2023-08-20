package com.stelios.cakenaysh.MenuCreation;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

public class MenuButton {

    private ItemStack itemStack;
    private Consumer<Player> whenClicked;

    //menu button constructor
    //@param itemStack: the itemStack of the button
    public MenuButton(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    //getters
    public ItemStack getItemStack(){
        return itemStack;
    }
    public Consumer<Player> getWhenClicked(){
        return whenClicked;
    }

    //setters
    public void setWhenClicked(Consumer<Player> whenClicked){
        this.whenClicked = whenClicked;
    }
}
