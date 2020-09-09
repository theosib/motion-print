package eigencraft.motionprint.mixin;

import com.mojang.brigadier.CommandDispatcher;
import eigencraft.motionprint.command.GrantConsentCommand;
import eigencraft.motionprint.command.MotionPrintCommand;
import eigencraft.motionprint.command.RevokeConsentCommand;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class MixinCommandManager {
    @Inject(method = "<init>(Z)V", at = @At("RETURN"))
    private void onInit(boolean isDedicatedServer, CallbackInfo ci) {
        CommandDispatcher dispatcher = ((CommandManager) (Object) this).getDispatcher();

        GrantConsentCommand.registerServerCommand(dispatcher);
        RevokeConsentCommand.registerServerCommand(dispatcher);
        MotionPrintCommand.registerServerCommand(dispatcher);
    }
}
