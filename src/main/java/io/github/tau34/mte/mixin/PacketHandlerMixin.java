package io.github.tau34.mte.mixin;

import io.github.tau34.mte.common.network.MTEPacketGuiInteract;
import mekanism.common.network.BasePacketHandler;
import mekanism.common.network.PacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class PacketHandlerMixin extends BasePacketHandler {
    @Inject(method = "initialize", at = @At("HEAD"))
    private void modifyInitialize(CallbackInfo ci) {
        this.registerClientToServer(MTEPacketGuiInteract.class, MTEPacketGuiInteract::decode);
    }
}
