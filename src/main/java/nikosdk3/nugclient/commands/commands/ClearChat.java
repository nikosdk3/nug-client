package nikosdk3.nugclient.commands.commands;

import nikosdk3.nugclient.commands.Command;

public class ClearChat extends Command {

    public ClearChat() {
        super("clear-chat", "Clears your chat.");
    }

    @Override
    public void run(String[] args) {
        mc.inGameHud.getChatHud().clear(false);
    }
}
