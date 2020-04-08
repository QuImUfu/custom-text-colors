package quimufu.custom_text_colors;

public class ChangingColorDummy implements ChangingColor {
    @Override
    public Integer getCurrentColor(float time) {
        return 0xFFFFFF;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public String serialize() {
        return "DummyChangingColor, used when no default was set, always returns white.";
    }
}
