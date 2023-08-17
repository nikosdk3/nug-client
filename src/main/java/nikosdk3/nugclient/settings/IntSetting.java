package nikosdk3.nugclient.settings;

import java.util.function.Consumer;

public class IntSetting extends Setting<Integer> {
    private final Integer min, max;

    public IntSetting(String name, String description, Integer defaultValue, Consumer<Integer> onChanged, Integer min, Integer max) {
        super(name, description, defaultValue, onChanged);
        this.min = min;
        this.max = max;
    }

    @Override
    protected Integer parseImpl(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException ignored){
            return null;
        }
    }

    @Override
    protected boolean isValidValue(Integer value) {
        return (min == null || value >= min) && (max == null || value <= max);
    }

    @Override
    public String genUsage() {
        String usage = "#blue";
        if (min == null) usage += "inf";
        else usage += min;

        usage += "#gray-#blue";

        if (max == null) usage += "inf";
        else usage += max;

        return usage;
    }

    public static class Builder {
        private String name = "undefined", description = "";
        private Integer defaultValue;
        private Consumer<Integer> onChanged;
        private Integer min, max;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(Integer defaultValue){
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder onChanged(Consumer<Integer> onChanged){
            this.onChanged = onChanged;
            return this;
        }

        public Builder min(int min){
            this.min = min;
            return this;
        }

        public Builder max(int max){
            this.max = max;
            return this;
        }

        public IntSetting build(){
            return new IntSetting(name, description, defaultValue, onChanged, min, max);
        }
    }
}
