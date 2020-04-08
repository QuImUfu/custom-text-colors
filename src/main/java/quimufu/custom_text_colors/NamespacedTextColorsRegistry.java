package quimufu.custom_text_colors;


import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import org.apache.logging.log4j.Level;
import quimufu.custom_text_colors.jankson.*;

import java.util.Map;
import java.util.Random;

import static quimufu.custom_text_colors.TextColorsResourceManager.normalize;


public class NamespacedTextColorsRegistry {


    protected Map<String, Integer> colors;
    protected Map<String, ChangingColor> changingColors;
    protected Default defaults;
    protected String namespace;
    protected final CustomTextColorsConfig conf = AutoConfig.getConfigHolder(CustomTextColorsConfig.class).getConfig();
    protected final Random random = new Random();
    protected ChangingColor eyesoreChangingColor = new ChangingColorEyesore();

    public NamespacedTextColorsRegistry(String ns, Map<String, Integer> cm, Map<String, ChangingColor> ccm, Default aDefault) {
        namespace = ns;
        reset(cm, ccm, aDefault);
    }

    public ChangingColor getColorChange(String s) {
        s = normalize(s);
        if (conf.eyesoreRandom) {
            return eyesoreChangingColor;
        }
        if (changingColors.get(s) == null || changingColors.get(s).isInvalid()) {
            CustomTextColors.log(Level.ERROR, "unset/invalid ChangingColor: " + s);
            if (defaults == null) {
                CustomTextColors.log(Level.ERROR, "no default set for namespace " + namespace);
                CustomTextColors.log(Level.ERROR, "if you use a changing color, you must provide a Default");
                CustomTextColors.log(Level.ERROR, "using dummy default");
                defaults = new DummyDefault();
                changingColors.put(s, defaults.getDefault(s));
            } else {
                ChangingColor aDefault = defaults.getDefault(s);
                CustomTextColors.log(Level.ERROR, "default is:\n" + aDefault.serialize());
                CustomTextColors.log(Level.ERROR, "Using Default");
                changingColors.put(s, aDefault);
            }
        }
        return changingColors.get(s);
    }



    public int getColor(String s, int color) {
        s = normalize(s);
        if (conf.eyesoreRandom) {
            return ((random.nextInt() | random.nextInt()) & 0xFFFFFF) | (color & 0xFF000000);
        }
        if (colors.get(s) == null) {
            CustomTextColors.log(Level.ERROR, "unset color: " + s);
            CustomTextColors.log(Level.ERROR, "default is:");
            JsonObject colorO = new JsonObject();
            colorO.put("red", new JsonPrimitive((0b11111111 & (color >> 16))));
            colorO.put("green", new JsonPrimitive((0b11111111 & (color >> 8))));
            colorO.put("blue", new JsonPrimitive((0b11111111 & color)));
            CustomTextColors.log(Level.ERROR, "\n\"" + s + "\": " + colorO.toJson(JsonGrammar.JANKSON));
            int color1 = color;
            if (conf.randomMissingColors) {
                color1 = random.nextInt() | random.nextInt();
                CustomTextColors.log(Level.ERROR, "Using Random Color:");
                colorO = new JsonObject();
                colorO.put("red", new JsonPrimitive((0b11111111 & (color1 >> 16))));
                colorO.put("green", new JsonPrimitive((0b11111111 & (color1 >> 8))));
                colorO.put("blue", new JsonPrimitive((0b11111111 & color1)));
                CustomTextColors.log(Level.ERROR, "\n\"" + s + "\": " + colorO.toJson(JsonGrammar.JANKSON));
            } else {
                CustomTextColors.log(Level.ERROR, "Using Default");
            }
            colors.put(s, 0xFFFFFF & color1);

        }
        return colors.get(s) | (color & 0xFF000000);
    }

    public void reset(Map<String, Integer> cm, Map<String, ChangingColor> ccm, Default aDefault) {
        colors = cm;
        changingColors = ccm;
        defaults = aDefault;
    }

}
