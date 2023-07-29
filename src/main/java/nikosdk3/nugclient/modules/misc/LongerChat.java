package nikosdk3.nugclient.modules.misc;

import nikosdk3.nugclient.NugClient;
import nikosdk3.nugclient.events.EventStore;
import nikosdk3.nugclient.modules.Category;
import nikosdk3.nugclient.modules.Module;
import nikosdk3.nugclient.settings.Setting;
import nikosdk3.nugclient.settings.IntSetting;

public class LongerChat extends Module {

    public Setting<Integer> lines = addSetting(new IntSetting.Builder()
            .name("lines")
            .description("Chat lines.")
            .defaultValue(1000)
            .min(1)
            .onChanged(integer -> {
                if (isActive())
                    NugClient.eventBus.post(EventStore.changeChatLengthEvent(integer));
            })
            .build()
    );

    public LongerChat() {
        super(Category.Misc, "longer-chat", "Makes chat longer.");
    }

    @Override
    public void onActivate() {
        NugClient.eventBus.post(EventStore.changeChatLengthEvent(lines.get()));
    }

    @Override
    public void onDeactivate() {
        NugClient.eventBus.post(EventStore.changeChatLengthEvent(100));
    }
}
