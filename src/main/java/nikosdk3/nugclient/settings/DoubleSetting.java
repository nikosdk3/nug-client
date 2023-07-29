package nikosdk3.nugclient.settings;

import java.util.function.Consumer;

public class DoubleSetting extends Setting<Double>{
    private final Double min, max;

    public DoubleSetting(String name, String description, Double defaultValue, Consumer<Double> onChanged, Consumer<Setting<Double>> onModuleActivated, Double min, Double max) {
        super(name, description, defaultValue, onChanged, onModuleActivated);
        this.min = min;
        this.max = max;
    }

    @Override
    protected Double parseImpl(String str) {
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException ignored){
            return null;
        }
    }

    @Override
    protected boolean isValidValue(Double value) {
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
        private Double defaultValue;
        private Consumer<Double> onChanged;
        private Consumer<Setting<Double>> onModuleActivated;
        private Double min, max;

        public DoubleSetting.Builder name(String name) {
            this.name = name;
            return this;
        }

        public DoubleSetting.Builder description(String description) {
            this.description = description;
            return this;
        }

        public DoubleSetting.Builder defaultValue(Double defaultValue){
            this.defaultValue = defaultValue;
            return this;
        }

        public DoubleSetting.Builder onChanged(Consumer<Double> onChanged){
            this.onChanged = onChanged;
            return this;
        }

        public DoubleSetting.Builder onModuleActivated(Consumer<Setting<Double>> onModuleActivated){
            this.onModuleActivated = onModuleActivated;
            return this;
        }

        public DoubleSetting.Builder min(double min){
            this.min = min;
            return this;
        }

        public DoubleSetting.Builder max(double max){
            this.max = max;
            return this;
        }

        public DoubleSetting build(){
            return new DoubleSetting(name, description, defaultValue, onChanged, onModuleActivated, min, max);
        }
    }
}
