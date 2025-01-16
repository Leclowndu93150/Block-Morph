package tfar.blockmorph;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import tfar.blockmorph.client.ModClientForge;

@Mod(BlockMorph.MOD_ID)
public class BlockMorphForge {
    
    public BlockMorphForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(this::hitbox);
        MinecraftForge.EVENT_BUS.addListener(BlockMorphForge::onLivingEquipmentChange);
        if (FMLEnvironment.dist.isClient()) {
            ModClientForge.init(bus);
        }
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        BlockMorph.init();
        
    }

    void hitbox(EntityEvent.Size event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            PlayerDuck playerDuck = PlayerDuck.of(player);
            if (playerDuck.isMorphed()) {
                event.setNewSize(EntityDimensions.fixed(1, 1), true);
            } else {
                event.setNewSize(Player.STANDING_DIMENSIONS, true);
            }
        }
    }

    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player && event.getSlot() == EquipmentSlot.HEAD && event.getTo().isEmpty()) {
            PlayerDuck playerDuck = PlayerDuck.of(player);
            if (playerDuck.isMorphed()) {
                playerDuck.setMorphed(false);
            }
        }
    }
}