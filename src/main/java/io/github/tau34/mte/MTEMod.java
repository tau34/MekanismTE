package io.github.tau34.mte;

import com.mojang.logging.LogUtils;
import io.github.tau34.mte.common.registries.MTECreativeTabs;
import io.github.tau34.mte.common.registries.MTEItems;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MTEMod.MOD_ID)
public class MTEMod {
    public static final String MOD_ID = "mte";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MTEMod() {
        //MMCConfig.registerConfig(ModLoadingContext.get());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MTEItems.ITEMS.register(modEventBus);
        MTECreativeTabs.CREATIVE_TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation rl(String path){
        return new ResourceLocation(MTEMod.MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
