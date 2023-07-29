package nikosdk3.nugclient.commands;

import net.minecraft.client.MinecraftClient;
import nikosdk3.nugclient.CommandDispatcher;

public abstract class Command extends CommandDispatcher {
    protected static MinecraftClient mc;

    public final String name;
    public final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        mc = MinecraftClient.getInstance();
    }

    public abstract void run(String[] args);
}
