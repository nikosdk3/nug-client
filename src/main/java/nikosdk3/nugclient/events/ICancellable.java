package nikosdk3.nugclient.events;

public interface ICancellable {
    void setCancelled(boolean cancelled);

    default void cancel() {
        setCancelled(true);
    }

    boolean isCancelled();
}
