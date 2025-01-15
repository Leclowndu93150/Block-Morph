package tfar.blockmorph.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Direction;
import tfar.blockmorph.PlayerDuck;

public record C2SDirectionPacket(Direction direction) implements C2SModPacket {
    public C2SDirectionPacket(FriendlyByteBuf buf) {
        this(Direction.values()[buf.readInt()]);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        PlayerDuck.of(player).setBlockDirection(direction);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(direction.ordinal());
    }
}
