package tfar.blockmorph.network;

import net.minecraft.resources.ResourceLocation;
import tfar.blockmorph.BlockMorph;
import tfar.blockmorph.network.client.S2CDirectionPacket;
import tfar.blockmorph.network.client.S2CMorphPacket;
import tfar.blockmorph.network.client.S2CRotationPacket;
import tfar.blockmorph.network.server.C2SDirectionPacket;
import tfar.blockmorph.network.server.C2SKeyPacket;
import tfar.blockmorph.network.server.C2SRotationPacket;
import tfar.blockmorph.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerClientPacket(S2CMorphPacket.class, S2CMorphPacket::new);
        Services.PLATFORM.registerServerPacket(C2SKeyPacket.class, C2SKeyPacket::new);
        Services.PLATFORM.registerClientPacket(S2CDirectionPacket.class, S2CDirectionPacket::new);
        Services.PLATFORM.registerServerPacket(C2SDirectionPacket.class, C2SDirectionPacket::new);
        Services.PLATFORM.registerClientPacket(S2CRotationPacket.class, S2CRotationPacket::new);
        Services.PLATFORM.registerServerPacket(C2SRotationPacket.class, C2SRotationPacket::new);

    }

    public static ResourceLocation packet(Class<?> clazz) {
        return BlockMorph.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
