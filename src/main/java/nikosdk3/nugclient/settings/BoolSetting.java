package nikosdk3.nugclient.settings;

import java.util.function.Consumer;

public class BoolSetting extends Setting<Boolean> {
    private BoolSetting(String name, String description, Boolean defaultValue, Consumer<Boolean> onChanged) {
        super(name, description, defaultValue, onChanged);
    }

    @Override
    protected Boolean parseImpl(String str) {
        if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("1")) return true;
        else if (str.equalsIgnoreCase("false") || str.equalsIgnoreCase("0")) return false;
        return null;
    }

    @Override
    protected boolean isValidValue(Boolean value) {
        return true;
    }

    @Override
    public String genUsage() {
        return "#bluetrue #grayor #bluefalse";
    }

    public static class Builder {
        private String name = "undefined", description = "";
        private boolean defaultValue;
        private Consumer<Boolean> onChanged;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(Boolean defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder onChanged(Consumer<Boolean> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public BoolSetting build() {
            return new BoolSetting(name, description, defaultValue, onChanged);
        }
    }
}
