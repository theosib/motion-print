package eigencraft.motionprint.mixin;

import java.util.function.BooleanSupplier;

import eigencraft.motionprint.command.MotionPrintCommand;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.MinecraftServer;
import eigencraft.motionprint.config.Configs;
import eigencraft.motionprint.data.ConsentTracker;
import eigencraft.motionprint.data.LoggingManager;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Shadow private int ticks;

    @Shadow @Final private CommandManager commandManager;

    @Inject(method = "tickWorlds", at = @At("RETURN"))
    private void onPostTickWorlds(BooleanSupplier supplier, CallbackInfo ci) {
        LoggingManager.INSTANCE.onTick((MinecraftServer) (Object) this, this.ticks);
    }

    @Inject(method = "run", at = @At("HEAD"))
    private void onServerStart(CallbackInfo ci) {
        MotionPrintCommand.registerServerCommand(commandManager.getDispatcher());
        Configs.readConfigsFromFile(); // note: this creates the config directory if it doesn't exist yet
        ConsentTracker.INSTANCE.readFromFile();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void onShutdown(CallbackInfo ci) {
        Configs.writeConfigsToFile();

        LoggingManager.INSTANCE.clear();
        ConsentTracker.INSTANCE.clear();
    }
}
