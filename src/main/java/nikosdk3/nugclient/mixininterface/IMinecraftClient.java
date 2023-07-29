package nikosdk3.nugclient.mixininterface;

public interface IMinecraftClient {
    void leftClick();
    void rightClick();
    int getCurrentFps();
    void setItemUseCooldown(int cooldown);
}
