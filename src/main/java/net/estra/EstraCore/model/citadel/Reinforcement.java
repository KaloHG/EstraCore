package net.estra.EstraCore.model.citadel;

import net.estra.EstraCore.model.Group;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Reinforcement {

    private ReinforcementType type;
    private Block block;
    private Location location;
    private Chunk chunk;
    private Group group;
    private int chunkX;
    private int chunkZ;

    private int health;
    private long timeRein;

    /**
     * Initial Instantiation of a Rein, when rein'd by a player.
     * @param block
     * @param type
     * @param location
     */
    public Reinforcement(Block block, Group group, ReinforcementType type, Location location) {
        this.type = type;
        this.block = block;
        this.group = group;
        this.health = type.getHealth();
        this.location = location;
        this.timeRein = System.currentTimeMillis();
        this.chunkX = location.getChunk().getX();
        this.chunkZ = location.getChunk().getZ();
        this.chunk = location.getChunk();
    }

    public int getHealth() {
        return health;
    }

    public Block getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }

    public ReinforcementType getType() {
        return type;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public boolean isMature() {
        long timeSince = System.currentTimeMillis() - timeRein;
        long timeTillMat = (long) type.getMatureTime() * 60 * 1000;
        return timeSince > timeTillMat;
    }

    /**
     * A basic multiplier for damage if the rein is immature.
     * @return - Multiplier to damage the rein by.
     *
     * @Inspired - By Citadel
     * TODO
     */
    public double getMatureMultiplier() {
        double timeExisted = (double) (System.currentTimeMillis() - timeRein);
        double progress = timeExisted / (double) getType().getMatureTime();
        return 0.0;
    }
}
