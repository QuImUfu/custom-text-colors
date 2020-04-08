package quimufu.custom_text_colors;

public class DummyDefault implements Default {
    final ChangingColor dummy = new ChangingColorDummy();
    @Override
    public ChangingColor getDefault(String s) {
        return dummy;
    }
}
