package quimufu.custom_text_colors;

import java.util.Random;

public class ChangingColorEyesore implements ChangingColor {
    protected final Random random = new Random();

    @Override
    public Integer getCurrentColor(float time) {
        return (random.nextInt() | random.nextInt()) & 0xFFFFFF;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public String serialize() {
        return "EyesoreChangingColor, returns random, likely light colors.";
    }
}
