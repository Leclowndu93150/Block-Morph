package tfar.blockmorph.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tfar.blockmorph.BlockRotation;
import tfar.blockmorph.PlayerDuck;

public record C2SRotationPacket(BlockRotation rotation) implements C2SModPacket {
    public C2SRotationPacket(FriendlyByteBuf buf) {
        this(BlockRotation.values()[buf.readInt()]);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        PlayerDuck.of(player).setRotation(rotation);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(rotation.ordinal());
    }
}
