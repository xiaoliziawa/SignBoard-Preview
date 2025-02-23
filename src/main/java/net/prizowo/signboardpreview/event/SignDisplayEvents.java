package net.prizowo.signboardpreview.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.prizowo.signboardpreview.api.ISignDisplay;
import net.prizowo.signboardpreview.util.SignDisplayManager;

public class SignDisplayEvents {
    
    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            if (event.getChunk() instanceof LevelChunk chunk) {
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    if (blockEntity instanceof SignBlockEntity && blockEntity instanceof ISignDisplay display) {
                        display.updateDisplay(level, blockEntity.getBlockPos());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            int viewDistance = level.getServer().getPlayerList().getViewDistance();
            int playerChunkX = player.chunkPosition().x;
            int playerChunkZ = player.chunkPosition().z;
            
            for (int x = -viewDistance; x <= viewDistance; x++) {
                for (int z = -viewDistance; z <= viewDistance; z++) {
                    int chunkX = playerChunkX + x;
                    int chunkZ = playerChunkZ + z;
                    
                    if (level.hasChunk(chunkX, chunkZ)) {
                        LevelChunk chunk = level.getChunk(chunkX, chunkZ);
                        for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                            if (blockEntity instanceof SignBlockEntity && blockEntity instanceof ISignDisplay display) {
                                display.updateDisplay(level, blockEntity.getBlockPos());
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            SignDisplayManager.clearDisplays(level);
        }
    }
} 