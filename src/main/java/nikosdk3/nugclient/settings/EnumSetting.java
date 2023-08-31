package nikosdk3.nugclient.settings;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public class EnumSetting<T extends Enum<?>> extends Setting<T> {
    private final T[] values;

    private EnumSetting(String name, String description, T defaultValue, Consumer<T> onChanged) {
        super(name, description, defaultValue, onChanged);

        try {
            values = (T[]) defaultValue.getClass().getMethod("values").invoke(null);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected T parseImpl(String str) {
        for (T possibleValue : values) {
            if (str.equalsIgnoreCase(possibleValue.toString()))
                return possibleValue;
        }
        return null;
    }

    @Override
    protected boolean isValidValue(T value) {
        return true;
    }

    @Override
    public String genUsage() {
        String usage = "";

        for (int i = 0; i < values.length; i++) {
            if (i > 0) usage += " #grayor ";
            usage += "#blue" + values[i];
        }

        return usage;
    }


    public static class Builder<T extends Enum<?>> {
        private String name = "undefined", description = "";
        private T defaultValue;
        private Consumer<T> onChanged;

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> description(String description) {
            this.description = description;
            return this;
        }

        public Builder<T> defaultValue(T defaultValue){
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> onChanged(Consumer<T> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public EnumSetting<T> build() {
            return new EnumSetting<>(name, description, defaultValue, onChanged);
        }
    }
}