package tfar.blockmorph.mixin;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.blockmorph.BlockRotation;
import tfar.blockmorph.PlayerDuck;
import tfar.blockmorph.network.client.S2CDirectionPacket;
import tfar.blockmorph.network.client.S2CMorphPacket;
import tfar.blockmorph.network.client.S2CRotationPacket;
import tfar.blockmorph.platform.Services;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {
    boolean morphed;
    private Direction blockDirection = Direction.NORTH;
    private BlockRotation rotation = BlockRotation.NORTH;

    @Override
    public boolean isMorphed() {
        return morphed;
    }

    @Override
    public void setMorphed(boolean morphed) {
        if (this.morphed != morphed) {
            Player player = (Player)(Object)this;
            if (!player.level().isClientSide()) {
                Services.PLATFORM.sendToTracking(player, new S2CMorphPacket(player.getUUID(), morphed));
            }
            this.morphed = morphed;
            ((Entity)(Object)this).refreshDimensions();
            if (morphed && !player.level().isClientSide()) {
                Vec3 pos = player.blockPosition().getCenter();
                ((ServerPlayer)player).connection.teleport(pos.x, pos.y-.5, pos.z, player.getYRot(), player.getXRot());
            }
        }
    }

    @Override
    public Direction getBlockDirection() {
        return blockDirection;
    }

    @Override
    public void setBlockDirection(Direction direction) {
        if (this.blockDirection != direction) {
            Player player = (Player)(Object)this;
            if (!player.level().isClientSide()) {
                Services.PLATFORM.sendToTracking(player, new S2CDirectionPacket(player.getUUID(), direction));
            }
            this.blockDirection = direction;
        }
    }

    @Override
    public BlockRotation getRotation() {
        return rotation;
    }

    @Override
    public void setRotation(BlockRotation rotation) {
        if (this.rotation != rotation) {
            Player player = (Player)(Object)this;
            if (!player.level().isClientSide()) {
                Services.PLATFORM.sendToTracking(player, new S2CRotationPacket(player.getUUID(), rotation));
            }
            this.rotation = rotation;
        }
    }

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    @Unique
    private void blockmorph$getDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (morphed) {
            cir.setReturnValue(EntityDimensions.scalable(1f, 1f));
        }
    }

    @Inject(method = "setItemSlot", at = @At("TAIL"))
    @Unique
    private void blockmorph$onItemSlotChange(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        if (slot == EquipmentSlot.HEAD && stack.isEmpty() && morphed) {
            setMorphed(false);
        }
    }
}