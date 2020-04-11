package quimufu.custom_text_colors;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Map;

import static quimufu.custom_text_colors.TextColorsResourceManager.normalize;

public class CustomTextColorsDefaults implements Default {
    protected final Map<String, ChangingColor> changingColors = Maps.newHashMap();

    public CustomTextColorsDefaults() {
        ArrayList<int[]> colorListRgb = new ArrayList<int[]>();
        ArrayList<float[]> colorListHsv = new ArrayList<float[]>();

        ChangingColorImpl cc1 = new ChangingColorImpl();
        cc1.setTypeCc("hsv");
        colorListHsv = new ArrayList<float[]>();
        colorListHsv.add(new float[]{1.2F, 0.7F, 0.6F});
        colorListHsv.add(new float[]{0.F, 0.7F, 0.6F});
        cc1.setColorListHsv(colorListHsv);
        cc1.setKey("InGameHud.textColor.ChangingColor");
        changingColors.put(normalize("InGameHud.textColor.ChangingColor"), cc1);


        ChangingColorImpl cc2 = new ChangingColorImpl();
        cc2.setTypeCc("rgb");
        colorListRgb = new ArrayList<int[]>();
        colorListRgb.add(new int[]{0, 255, 0});
        colorListRgb.add(new int[]{255, 0, 0});
        cc2.setColorListRgb(colorListRgb);
        cc2.setKey("PlayerListHud.textColor.ChangingColor.health");
        changingColors.put(normalize("PlayerListHud.textColor.ChangingColor.health"), cc2);

        ChangingColorImpl cc3 = new ChangingColorImpl();
        cc3.setTypeCc("rgb");
        colorListRgb = new ArrayList<int[]>();
        colorListRgb.add(new int[]{0, 0, 0});
        colorListRgb.add(new int[]{255, 255, 255});
        cc3.setColorListRgb(colorListRgb);
        cc3.setKey("SubtitlesHud.textColor.ChangingColor.fadingOut");
        changingColors.put(normalize("SubtitlesHud.textColor.ChangingColor.fadingOut"), cc3);

        ChangingColorImpl cc4 = new ChangingColorImpl();
        cc4.setTypeCc("rgb");
        colorListRgb = new ArrayList<int[]>();
        colorListRgb.add(new int[]{255, 255, 255});
        cc4.setColorListRgb(colorListRgb);
        cc4.setKey("ItemStack.ChangingColor.amount");
        changingColors.put(normalize("ItemStack.ChangingColor.amount"), cc4);

    }


    @Override
    public ChangingColor getDefault(String s) {
        if (!changingColors.containsKey(s)) {
            CustomTextColors.log(Level.ERROR, "No default provided by " + this.getClass().getSimpleName());
            CustomTextColors.log(Level.ERROR, "using DummyChangingColor");
            changingColors.put(s, new ChangingColorDummy());
        }
        return changingColors.get(s);
    }
}
