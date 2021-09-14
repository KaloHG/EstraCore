package net.estra.EstraCore.model.citadel;

public class ChunkCoord {

    private int x;
    private int z;

    public ChunkCoord(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public boolean isEqual(ChunkCoord coord) {
        if(coord.getX() == x && coord.getZ() == z) {
            return true;
        }
        return false;
    }
}
