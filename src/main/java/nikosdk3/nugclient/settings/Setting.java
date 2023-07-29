package nikosdk3.nugclient.settings;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class Setting<T> {
    public String name;
    public String title;
    public String description;
    public String usage;

    private T value;
    private T defaultValue;

    private final Consumer<T> onChanged;
    public final Consumer<Setting<T>> onModuleActivated;

    public Setting(String name, String description, T defaultValue, Consumer<T> onChanged, Consumer<Setting<T>> onModuleActivated) {
        this.name = name;
        this.title = Arrays.stream(name.split("-")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        this.description = description;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.onChanged = onChanged;
        this.onModuleActivated = onModuleActivated;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        if (!isValidValue(value)) return;
        this.value = value;
        if (onChanged != null)
            onChanged.accept(value);
    }

    public void reset() {
        value = defaultValue;
        if (onChanged != null) {
            onChanged.accept(value);
        }
    }

    public boolean parse(String str) {
        T newValue = parseImpl(str);

        if (newValue != null) {
            if (isValidValue(newValue)) {
                value = newValue;
                if (onChanged != null) {
                    onChanged.accept(value);
                }
            }
        }
        return newValue != null;
    }

    protected abstract T parseImpl(String str);

    protected abstract boolean isValidValue(T value);

    public String getUsage() {
        if (usage == null) usage = genUsage();
        return usage;
    }

    protected abstract String genUsage();

    @Override
    public String toString() {
        return value.toString();
    }
}
