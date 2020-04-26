package quimufu.custom_text_colors;


import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.util.Util;
import org.apache.logging.log4j.Level;
import quimufu.custom_text_colors.jankson.JsonGrammar;
import quimufu.custom_text_colors.jankson.JsonObject;
import quimufu.custom_text_colors.jankson.JsonPrimitive;

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
    private Map<String, Integer> currentPRColors = Maps.newHashMap();
    private Map<String, Integer> nextPRColors = Maps.newHashMap();
    private long time = -1;
    private boolean preparedFixedRandom = false;
    protected Map<String, Integer> colorsSave;

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
        String keyString = normalize(s);
        if (conf.eyesoreRandom) {
            return getRandomColor() | (color & 0xFF000000);
        }
        if (conf.pulsatingRandom) {
            long now = Util.getMeasuringTimeMs();
            if (time == -1) {
                time = now;
            }
            int timeDelta = (int) (now - time);
            if (timeDelta >= conf.pulseSpeedMs || timeDelta < 0) {
                time = now - (timeDelta - conf.pulseSpeedMs);
                if (timeDelta > 2 * conf.pulseSpeedMs) {
                    timeDelta = 0;
                } else {
                    timeDelta = (timeDelta - conf.pulseSpeedMs);
                }
                Map<String, Integer> tmp = currentPRColors;
                currentPRColors = nextPRColors;
                nextPRColors = tmp;
                nextPRColors.clear();
            }
            if (!currentPRColors.containsKey(keyString)) {
                currentPRColors.put(keyString, getRandomColor());
            }
            if (!nextPRColors.containsKey(keyString)) {
                nextPRColors.put(keyString, (getRandomColor()));
            }
            return mix(currentPRColors.get(keyString), nextPRColors.get(keyString), ((float) timeDelta) / ((float) conf.pulseSpeedMs)) | (color & 0xFF000000);
        }
        if (conf.groupedRandom && !preparedFixedRandom) {
            prepaireFixedRandom();
        } else if (!conf.groupedRandom && preparedFixedRandom) {
            reverseFixedRandom();
        }
        if (colors.get(keyString) == null) {
            CustomTextColors.log(Level.ERROR, "unset color: " + keyString);
            CustomTextColors.log(Level.ERROR, "default is:");
            JsonObject colorO = new JsonObject();
            colorO.put("red", new JsonPrimitive((0b11111111 & (color >> 16))));
            colorO.put("green", new JsonPrimitive((0b11111111 & (color >> 8))));
            colorO.put("blue", new JsonPrimitive((0b11111111 & color)));
            CustomTextColors.log(Level.ERROR, "\n\"" + s + "\": " + colorO.toJson(JsonGrammar.JANKSON));
            int color1 = color;
            if (conf.randomMissingColors || conf.groupedRandom) {
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
            colors.put(keyString, 0xFFFFFF & color1);

        }
        return colors.get(keyString) | (color & 0xFF000000);
    }

    private int getRandomColor() {
        return (random.nextInt() | random.nextInt()) & 0xFFFFFF;
    }

    private void reverseFixedRandom() {
        colors = colorsSave;
        preparedFixedRandom = false;
    }

    private void prepaireFixedRandom() {
        colorsSave = colors;
        colors = Maps.newHashMap();
        Multimap<String, String> cgr = TextColorsResourceManager.getInstance().getColorGroupRegistry(this);
        for (String group : cgr.keys()) {
            int color = getRandomColor();
            for (String entry : cgr.get(group)) {
                colors.put(entry, color);
            }
        }
        for (String entry : colorsSave.keySet()) {
            colors.putIfAbsent(entry, getRandomColor());
        }
        preparedFixedRandom = true;
    }

    private int mix(int c1, int c2, float ratio) {
        int r1, g1, b1, r2, g2, b2;
        r1 = (int) ((c1 & 0xFF0000) * (1. - ratio)) & 0xFF0000;
        g1 = (int) ((c1 & 0xFF00) * (1. - ratio)) & 0xFF00;
        b1 = (int) ((c1 & 0xFF) * (1. - ratio)) & 0xFF;
        r2 = (int) ((c2 & 0xFF0000) * (ratio)) & 0xFF0000;
        g2 = (int) ((c2 & 0xFF00) * (ratio)) & 0xFF00;
        b2 = (int) ((c2 & 0xFF) * (ratio)) & 0xFF;
        return r1 + g1 + b1 + r2 + g2 + b2;
    }

    public void reset(Map<String, Integer> cm, Map<String, ChangingColor> ccm, Default aDefault) {
        if (preparedFixedRandom) {
            colorsSave = cm;
        } else {
            colors = cm;
        }
        changingColors = ccm;
        defaults = aDefault;
    }

}
