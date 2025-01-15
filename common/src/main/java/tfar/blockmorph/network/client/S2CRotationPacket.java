package tfar.blockmorph.network.client;

import net.minecraft.network.FriendlyByteBuf;
import tfar.blockmorph.BlockRotation;
import tfar.blockmorph.client.ClientPacketHandler;
import java.util.UUID;

public record S2CRotationPacket(UUID uuid, BlockRotation rotation) implements S2CModPacket {
    public S2CRotationPacket(FriendlyByteBuf buf) {
        this(buf.readUUID(), BlockRotation.values()[buf.readInt()]);
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.handleRotation(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUUID(uuid);
        to.writeInt(rotation.ordinal());
    }
}

