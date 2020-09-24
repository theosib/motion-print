package eigencraft.motionprint.api;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import eigencraft.motionprint.data.LoggingManager;
import eigencraft.motionprint.data.PlayerDataLogger;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;

public abstract class MotionPrintPlugin {

    public static void register(MotionPrintPlugin plugin){
        LoggingManager.INSTANCE.addPlugin(plugin);
    }

    private boolean enabled;
    private String name;

    public MotionPrintPlugin(String name){
        this.name = name;
    }

    public abstract void onEnabled();
    public abstract void onDisabled();
    public abstract void startLogPlayer(PlayerDataLogger newLogger);
    public abstract void stopLogPlayer(PlayerDataLogger logger);
    public abstract void tick(Collection<PlayerDataLogger> players);
    public CommandNode<ServerCommandSource> registerConfigCommand(CommandDispatcher<ServerCommandSource> dispatcher){
        return null;
    }

    public void enable(){
        enabled = true;
        onEnabled();
    }
    public void disable(){
        enabled = false;
        onDisabled();
    }
    public boolean isEnabled(){
        return enabled;
    }

    public String getName(){
        return name;
    }
}
