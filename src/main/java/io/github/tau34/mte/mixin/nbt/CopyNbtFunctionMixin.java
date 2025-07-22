package io.github.tau34.mte.mixin.nbt;

import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(CopyNbtFunction.class)
public class CopyNbtFunctionMixin {
    @Shadow @Final
    List<CopyNbtFunction.CopyOperation> operations;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addOps(LootItemCondition[] p_165175_, NbtProvider p_165176_, List<CopyNbtFunction.CopyOperation> p_165177_, CallbackInfo ci) {
        for (CopyNbtFunction.CopyOperation op : p_165177_) {
            if (((CopyOperationMixin) op).getSource().equals("componentUpgrade")) {
                List<CopyNbtFunction.CopyOperation> res = new ArrayList<>(operations);
                JsonObject obj = new JsonObject();
                obj.addProperty("op", "replace");
                obj.addProperty("source", "componentEnhancement");
                obj.addProperty("target", "mekData.componentEnhancement");
                res.add(CopyNbtFunction.CopyOperation.fromJson(obj));
                ((CopyNbtFunctionAccessor) this).setOperations(res);
                break;
            }
        }
    }
}
