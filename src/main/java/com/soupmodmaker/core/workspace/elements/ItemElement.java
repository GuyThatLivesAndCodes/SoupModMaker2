package com.soupmodmaker.core.workspace.elements;

import com.soupmodmaker.core.workspace.WorkspaceElement;

/**
 * Represents an item element in the workspace.
 * Items are basic Minecraft items that can be held, crafted, etc.
 */
public class ItemElement extends WorkspaceElement {

    public ItemElement() {
        super("item");
    }

    public ItemElement(String id, String name) {
        super("item", id, name);
    }

    // Convenience methods for common item properties

    public int getMaxStackSize() {
        Object value = getProperty("maxStackSize");
        return value != null ? ((Number) value).intValue() : 64;
    }

    public void setMaxStackSize(int maxStackSize) {
        setProperty("maxStackSize", maxStackSize);
    }

    public String getRarity() {
        Object value = getProperty("rarity");
        return value != null ? value.toString() : "COMMON";
    }

    public void setRarity(String rarity) {
        setProperty("rarity", rarity);
    }

    public boolean isGlowing() {
        Object value = getProperty("glows");
        return value != null && (Boolean) value;
    }

    public void setGlowing(boolean glowing) {
        setProperty("glows", glowing);
    }

    @Override
    public String toString() {
        return "ItemElement{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", maxStackSize=" + getMaxStackSize() +
                ", rarity='" + getRarity() + '\'' +
                '}';
    }
}
