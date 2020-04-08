package quimufu.custom_text_colors;

public interface ChangingColor {
    Integer getCurrentColor(float time);

    boolean isInvalid();

    String serialize();
}
