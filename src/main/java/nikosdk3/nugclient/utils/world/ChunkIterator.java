package nikosdk3.nugclient.utils.world;

import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import nikosdk3.nugclient.utils.Utils;

import java.util.Iterator;

import static nikosdk3.nugclient.utils.Utils.mc;

public class ChunkIterator implements Iterator<Chunk> {
    private final int px, pz;
    private final int r;

    private int x, z;
    private Chunk chunk;

    public ChunkIterator() {
        px = ChunkSectionPos.getSectionCoord(mc.player.getBlockX());
        pz = ChunkSectionPos.getSectionCoord(mc.player.getBlockZ());
        r = Utils.getRenderDistance();

        x = px - r;
        z = pz - r;

        nextChunk();
    }

    private void nextChunk() {
        chunk = null;

        while (true) {
            z++;
            if (z > pz + r) {
                z = pz - r;
                x++;
            }

            if (x > px + r || z > pz + r) break;

            chunk = mc.world.getChunk(x, z, ChunkStatus.FULL, false);
            if (chunk != null) break;
        }
    }

    @Override
    public boolean hasNext() {
        return chunk != null;
    }

    @Override
    public Chunk next() {
        Chunk chunk = this.chunk;

        nextChunk();

        return chunk;
    }
}