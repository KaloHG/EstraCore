package net.estra.EstraCore.manager;

import net.estra.EstraCore.EstraCorePlugin;
import net.estra.EstraCore.model.citadel.ChunkCoord;
import net.estra.EstraCore.model.citadel.Reinforcement;
import net.estra.EstraCore.model.citadel.ReinforcementType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReinforcementManager {

    private List<ReinforcementType> activeTypes = new ArrayList<>();
    private HashMap<Location, Reinforcement> reinCache = new HashMap<>();
    private HashMap<ChunkCoord, Chunk> activeChunks = new HashMap<ChunkCoord, Chunk>();

    private World world;

    public ReinforcementManager(World world) {
        if(world == null) {
            EstraCorePlugin.instance.getLogger().severe("Failed to initialize reinworld. Shutting down!");
            EstraCorePlugin.instance.getServer().shutdown();
            return;
        }
        this.world = world;
    }

    /**
     * Below code is ran almost immediately after the server starts, therefore any chunks loaded likely won't be unloaded,
     * listeners are also created immediately after to catch any chunk unloading shenanigans.
     *
     * We also want to load all reins within these chunks immediately after.
     */
    public void initializeChunks() {
        EstraCorePlugin.instance.getLogger().info("Initializing ReinWorld Chunks.");
        for(Chunk chunk : getWorld().getLoadedChunks()) {
            ChunkCoord coord = new ChunkCoord(chunk.getX(), chunk.getZ());
            activeChunks.put(coord, chunk);
            EstraCorePlugin.instance.getLogger().info("Loading Chunk (x,z) " + coord.getX() + " " + coord.getZ());
        }
    }

    /**
     * Below chunks are being loaded, so we want to fetch reins within those chunks at the same time.
     * @param chunk - Chunk being loaded
     */
    public void addChunk(Chunk chunk) {
        ChunkCoord coord = new ChunkCoord(chunk.getX(), chunk.getZ());
        if(activeChunks.containsKey(coord)) {
            //Odd that its loaded, but we replace it anyway in-case of modification.
            activeChunks.replace(coord, chunk);
        }
        activeChunks.put(coord, chunk);
        //EstraCorePlugin.instance.getLogger().info("Loading Chunk (x,z) " + coord.getX() + " " + coord.getZ());
    }

    /**
     * These chunks are being removed, so we want to unload reins from cache as they're unloaded.
     * @param chunk - Chunk being unloaded
     */
    public void removeChunk(Chunk chunk) {
        ChunkCoord coord = new ChunkCoord(chunk.getX(), chunk.getZ());
        activeChunks.remove(coord);
        //EstraCorePlugin.instance.getLogger().info("Unloading Chunk (x,z) " + coord.getX() + " " + coord.getZ());
    }

    public void setActiveTypes(List<ReinforcementType> types) {
        activeTypes = types;
    }

    public World getWorld() {
        return world;
    }
}
