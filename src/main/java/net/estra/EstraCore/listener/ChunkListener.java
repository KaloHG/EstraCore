package net.estra.EstraCore.listener;

import net.estra.EstraCore.EstraCorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * Yes i understand this is not the best way of doing this.
 * no, i'm not changing it.
 */
public class ChunkListener implements Listener {

    /**
     * Fired whenever a chunk is loaded, we want to pass this to our reinManager that way
     * the reinManager can load any reins associated to said chunk from DAO.
     * @param event - ChunkLoadEvent
     */
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if(!event.getChunk().getWorld().equals(EstraCorePlugin.instance.getReinManager().getWorld())) {
            //Chunk is not in active reinWorld, we don't want to load it into memory, as reins will not be in this world.
            return;
        }
        EstraCorePlugin.instance.getReinManager().addChunk(event.getChunk());
    }

    /**
     * Fired whenever a chunk is unloaded, we pass this to our reinManager that way
     * the reinManager can remove any remaining reins associated to this chunk from cache.
     * @param event - ChunkUnloadEvent
     */
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if(!event.getChunk().getWorld().equals(EstraCorePlugin.instance.getReinManager().getWorld())) {
            //Chunk is not in active reinWorld, we don't want to load it into memory, as reins will not be in this world.
            return;
        }
        EstraCorePlugin.instance.getReinManager().removeChunk(event.getChunk());
    }
}
