package com.stelios.cakenaysh.Items;

import com.stelios.cakenaysh.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;

public enum Recipes {

    GRUBULOUSLY_GRUBBY_GRUSTARD(new ShapedRecipe(new NamespacedKey(Main.getPlugin(Main.class),
            "grubulously_grubby_grustard"), CustomItems.GRUBULOUSLY_GRUBBY_GRUSTARD.getItem().build())
            .shape("GGG",
                    "G G",
                    "GGG")
            .setIngredient('G', CustomItems.GRUBBY_GUSTARD.getItem().build())),

    WRATH_OF_SPARTA(new ShapedRecipe(new NamespacedKey(Main.getPlugin(Main.class),
            "wrath_of_sparta"), CustomItems.WRATH_OF_SPARTA.getItem().build())
            .shape("T  ",
                    "   ",
                    "   ")
            .setIngredient('T', CustomItems.TEST_ITEM.getItem().build())),

    HELM_OF_SPARTA(new ShapedRecipe(new NamespacedKey(Main.getPlugin(Main.class),
            "helm_of_sparta"), CustomItems.HELM_OF_SPARTA.getItem().build())
            .shape("TTT",
                    "T T",
                    "   ")
            .setIngredient('T', CustomItems.INGOT_OF_SPARTA.getItem().build())),

    CHEST_OF_SPARTA(new ShapedRecipe(new NamespacedKey(Main.getPlugin(Main.class),
            "chest_of_sparta"), CustomItems.CHEST_OF_SPARTA.getItem().build())
            .shape("T T",
                    "TTT",
                    "TTT")
            .setIngredient('T', CustomItems.INGOT_OF_SPARTA.getItem().build())),

    LEGS_OF_SPARTA(new ShapedRecipe(new NamespacedKey(Main.getPlugin(Main.class),
            "legs_of_sparta"), CustomItems.LEGS_OF_SPARTA.getItem().build())
            .shape("TTT",
                    "T T",
                    "T T")
            .setIngredient('T', CustomItems.INGOT_OF_SPARTA.getItem().build())),

    BOOTS_OF_SPARTA(new ShapedRecipe(new NamespacedKey(Main.getPlugin(Main.class),
            "boots_of_sparta"), CustomItems.BOOTS_OF_SPARTA.getItem().build())
            .shape("   ",
                    "T T",
                    "T T")
            .setIngredient('T', CustomItems.INGOT_OF_SPARTA.getItem().build())),

    ;

    private final ShapedRecipe recipe;

    Recipes(ShapedRecipe recipe) {
        this.recipe = recipe;
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }
    public NamespacedKey getKey() { return recipe.getKey(); }
    public ItemStack getResult() { return recipe.getResult(); }
    public String[] getShape() { return recipe.getShape(); }
    public Map<Character, ItemStack> getIngredients() { return recipe.getIngredientMap(); }


}
