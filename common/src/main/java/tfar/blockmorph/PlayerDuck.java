package tfar.blockmorph;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;

public interface PlayerDuck {
    boolean isMorphed();
    void setMorphed(boolean morphed);
    Direction getBlockDirection();
    void setBlockDirection(Direction direction);
    BlockRotation getRotation();
    void setRotation(BlockRotation rotation);

    default void toggleMorph() {
        setMorphed(!isMorphed());
    }

    default void cycleDirection() {
        setRotation(getRotation().next());
    }

    default Player getRotationPlayer() {
        return (Player)(Object)this;
    }

    static PlayerDuck of(Player player) {
        return (PlayerDuck) player;
    }
}