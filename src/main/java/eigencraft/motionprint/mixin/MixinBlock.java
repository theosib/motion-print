package eigencraft.motionprint.mixin;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock {
    @Shadow @Final private float slipperiness;

    @Inject(method = "getSlipperiness", at = @At("HEAD"), cancellable = true)
    public void setCustomSlipperiness(CallbackInfoReturnable<Float> cir) {
        float custom = 100f; // TODO I have no idea how configs are handled in here (bagatelle)
        if(custom != this.slipperiness) {
            cir.setReturnValue(custom);
            cir.cancel();
        }
    }
}