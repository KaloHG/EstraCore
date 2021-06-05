package net.estra.EstraCore.model.citadel;

import org.bukkit.Material;

public class ReinforcementType {

    private String name;
    private Material material;
    private int health;
    private int matureTime;

    public ReinforcementType(String name, Material material, int health, int matureTime) {
        this.name = name;
        this.material = material;
        this.health = health;
        this.matureTime = matureTime;
    }

    public int getHealth() {
        return health;
    }

    public int getMatureTime() {
        return matureTime;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }
}
