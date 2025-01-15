package tfar.blockmorph.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Direction;
import tfar.blockmorph.client.ClientPacketHandler;
import java.util.UUID;

public record S2CDirectionPacket(UUID uuid, Direction direction) implements S2CModPacket {
    public S2CDirectionPacket(FriendlyByteBuf buf) {
        this(buf.readUUID(), Direction.values()[buf.readInt()]);
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.handleDirection(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUUID(uuid);
        to.writeInt(direction.ordinal());
    }
}