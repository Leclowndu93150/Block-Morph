package tfar.blockmorph.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tfar.blockmorph.PlayerDuck;
import tfar.blockmorph.network.server.C2SKeyPacket;
import tfar.blockmorph.platform.Services;

public class ModClientForge {

    public static void init(IEventBus bus) {
        bus.addListener(ModClientForge::keybind);
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::renderBlock);
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::clientTick);
    }

    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            while (ClientPacketHandler.TOGGLE.consumeClick()) {
                Services.PLATFORM.sendToServer(new C2SKeyPacket());
            }
        }
    }

    public static void keybind(RegisterKeyMappingsEvent event) {
        event.register(ClientPacketHandler.TOGGLE);
    }

    public static void renderBlock(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        PlayerDuck playerDuck = PlayerDuck.of(player);
        ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!stack.isEmpty() && playerDuck.isMorphed()) {
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();

            BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
            BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
            BakedModel model = blockRenderer.getBlockModel(state);

            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.solid());

            float scale = 2;
            poseStack.translate(-0.5, 0, -0.5);
            //poseStack.scale(scale, scale, scale);

            blockRenderer.renderBatched(state, player.blockPosition(), player.level(),
                    poseStack, buffer, true, RandomSource.create(),
                    ModelData.EMPTY, null);

            bufferSource.endBatch(RenderType.solid());
            poseStack.popPose();
            event.setCanceled(true);
        }
    }
}
