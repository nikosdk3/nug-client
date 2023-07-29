package nikosdk3.nugclient;

import com.google.common.eventbus.Subscribe;
import nikosdk3.nugclient.events.event.ChangeChatLengthEvent;

public class MixinValues {
    private static int chatLength = 100;

    public static void init(){
        NugClient.eventBus.register(new MixinValues());
    }

    @Subscribe
    public void onChangeChatLength(ChangeChatLengthEvent event){
        chatLength = event.length;
    }

    public static int getChatLength() {
        return chatLength;
    }
}
