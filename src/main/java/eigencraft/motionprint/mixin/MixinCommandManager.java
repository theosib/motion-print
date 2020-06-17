package eigencraft.motionprint.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.command.CommandManager;
import eigencraft.motionprint.command.MotionPrintCommand;

@Mixin(CommandManager.class)
public class MixinCommandManager {
    @Inject(method = "<init>(Z)V", at = @At("RETURN"))
    private void onInit(boolean isDedicatedServer, CallbackInfo ci) {
        MotionPrintCommand.registerServerCommand(((CommandManager) (Object) this).getDispatcher());
    }
}
