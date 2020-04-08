package quimufu.custom_text_colors;

import org.apache.logging.log4j.Level;
import quimufu.custom_text_colors.jankson.*;

import java.util.ArrayList;
import java.util.Map.Entry;

import static net.minecraft.util.math.MathHelper.hsvToRgb;
import static quimufu.custom_text_colors.CustomTextColors.log;
import static quimufu.custom_text_colors.TextColorsResourceManager.checkValidColor;

public class ChangingColorImpl implements ChangingColor {
    private String typeCc = "";

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
    private ArrayList<float[]> colorListHsv = new ArrayList<float[]>();
    private ArrayList<int[]> colorListRgb = new ArrayList<int[]>();
    private boolean valid = false;


    public String getTypeCc() {
        return typeCc;
    }

    public ArrayList<float[]> getColorListHsv() {
        return colorListHsv;
    }

    public ArrayList<int[]> getColorListRgb() {
        return colorListRgb;
    }


    public void setTypeCc(String typeCc) {
        this.typeCc = typeCc;
        testValidity();
    }

    public void setColorListHsv(ArrayList<float[]> colorListHsv) {
        this.colorListHsv = colorListHsv;
        testValidity();
    }

    public void setColorListRgb(ArrayList<int[]> colorListRgb) {
        this.colorListRgb = colorListRgb;
        testValidity();
    }

    public ChangingColorImpl() {
    }

    public ChangingColorImpl(Entry<String, JsonElement> entry) {
        key = entry.getKey();
        JsonObject colorsO = (JsonObject) entry.getValue();
        for (Entry<String, JsonElement> elementEntry : colorsO.entrySet()) {
            if (elementEntry.getKey().equals("colorType")) {
                if (deserializeColorType(elementEntry)) {
                    reset();
                    break;
                }
            } else if (elementEntry.getKey().equals("colors")) {
                if (deserializeColorArray(entry, elementEntry)) {
                    reset();
                    break;
                }
            } else {
                log(Level.ERROR, "Error parsing colors.json file");
                log(Level.ERROR, entry.getKey() + " includes unknown key: " + elementEntry.getKey());
                log(Level.ERROR, "Ignoring!");
            }

        }
        testValidity();
    }

    private boolean deserializeColorArray(Entry<String, JsonElement> entry, Entry<String, JsonElement> elementEntry) {
        JsonElement colorArray = elementEntry.getValue();
        if (!(colorArray instanceof JsonArray)) {
            log(Level.ERROR, "Error parsing colors.json file");
            log(Level.ERROR, elementEntry.getKey() + " is not a Array");
            log(Level.ERROR, "For a single color write only that color in the Array");
            return true;
        }
        JsonArray colors = (JsonArray) colorArray;
        for (JsonElement colorE : colors) {
            if (!(colorE instanceof JsonObject)) {
                log(Level.ERROR, "Error parsing colors.json file");
                log(Level.ERROR, "The color in the color list regarding "
                        + entry.getKey() + " is not a JsonObject");
                log(Level.ERROR, "Ignoring that color");
                break;
            }
            JsonObject colorO = (JsonObject) colorE;
            float h = 0, s = 0, v = 0;
            int r = 0, g = 0, b = 0;
            for (Entry<String, JsonElement> color : colorO.entrySet()) {

                if (!(color.getValue() instanceof JsonPrimitive) || !(((JsonPrimitive) color.getValue()).getValue() instanceof Long)) {
                    log(Level.ERROR, color.getKey() + " is not a Number");
                    continue;
                }
                switch (color.getKey()) {
                    case "red":
                        r = (int) (long) ((JsonPrimitive) color.getValue()).getValue();
                        r = checkValidColor(r);
                        break;
                    case "green":
                        g = (int) (long) ((JsonPrimitive) color.getValue()).getValue();
                        g = checkValidColor(g);
                        break;
                    case "blue":
                        b = (int) (long) ((JsonPrimitive) color.getValue()).getValue();
                        b = checkValidColor(b);
                        break;
                    case "hue":
                        h = (int) (long) ((JsonPrimitive) color.getValue()).getValue();
                        break;
                    case "saturation":
                        s = (int) (long) ((JsonPrimitive) color.getValue()).getValue();
                        break;
                    case "value":
                        v = (int) (long) ((JsonPrimitive) color.getValue()).getValue();
                        break;
                    default:
                        log(Level.ERROR, "Error parsing colors.json file");
                        log(Level.ERROR, entry.getKey() + " includes unknown key: " + color.getKey());
                        log(Level.ERROR, "Ignoring!");
                }
            }
            colorListHsv.add(new float[]{h, s, v});
            colorListRgb.add(new int[]{r, g, b});
        }
        return colorListHsv.size() == 0 || colorListRgb.size() == 0;
    }

    private void reset() {
        key = null;
        colorListHsv = new ArrayList<float[]>();
        colorListRgb = new ArrayList<int[]>();
        typeCc = "";
    }

    private void testValidity() {
        valid = (typeCc.equals("hsv") && colorListHsv.size() > 0)
                || typeCc.equals("rgb") && colorListRgb.size() > 0;
    }

    private boolean deserializeColorType(Entry<String, JsonElement> elementEntry) {
        JsonElement type = elementEntry.getValue();
        if (!(type instanceof JsonPrimitive) || !(((JsonPrimitive) type).getValue() instanceof String)) {
            log(Level.ERROR, "Error parsing colors.json file");
            log(Level.ERROR, elementEntry.getKey() + " is not a String");
            return true;
        }
        typeCc = (String) ((JsonPrimitive) type).getValue();
        if (!typeCc.equals("rgb") && !typeCc.equals("hsv")) {
            log(Level.ERROR, "Error parsing colors.json file");
            log(Level.ERROR, elementEntry.getKey() + " is nigher \"hsv\" nor \"rgb\"");
            return true;
        }
        return false;
    }

    public boolean isInvalid() {
        return !valid;
    }

    public String serialize() {
        if (isInvalid()) {
            return "invalid Changing Color";
        }
        JsonObject container = new JsonObject();
        JsonObject entry = new JsonObject();
        container.put(key, entry);
        entry.put("colorType", new JsonPrimitive(typeCc));
        JsonArray colorList = new JsonArray();
        entry.put("colors", colorList);
        if (typeCc.equals("rgb")) {
            for (int[] color : colorListRgb) {
                JsonObject colorO = new JsonObject();
                colorO.put("red", new JsonPrimitive(color[0]));
                colorO.put("green", new JsonPrimitive(color[1]));
                colorO.put("blue", new JsonPrimitive(color[2]));
                colorList.add(colorO);
            }
        } else if (typeCc.equals("hsv")) {
            for (float[] color : colorListHsv) {
                JsonObject colorO = new JsonObject();
                colorO.put("red", new JsonPrimitive(color[0]));
                colorO.put("green", new JsonPrimitive(color[1]));
                colorO.put("blue", new JsonPrimitive(color[2]));
                colorList.add(colorO);
            }
        }
        return container.toJson(JsonGrammar.JANKSON);
    }

    @Override
    public Integer getCurrentColor(float time) {
        if (getTypeCc().equals("rgb")) {
            ArrayList<int[]> colorListRgb = getColorListRgb();
            if (colorListRgb.size() == 1) {
                return colorListRgb.get(0)[0] << 16 | colorListRgb.get(0)[1] << 8 | colorListRgb.get(0)[2];
            }
            int part = (int) (time * (float) (colorListRgb.size() - 1)) % (colorListRgb.size() - 1);
            float progress = time * (float) (colorListRgb.size() - 1) % 1.0F;
            int r = (int) ((colorListRgb.get(part)[0] * (1.F - progress)) + (colorListRgb.get(part + 1)[0] * (progress)));
            int g = (int) ((colorListRgb.get(part)[1] * (1.F - progress)) + (colorListRgb.get(part + 1)[1] * (progress)));
            int b = (int) ((colorListRgb.get(part)[2] * (1.F - progress)) + (colorListRgb.get(part + 1)[2] * (progress)));
            return r << 16 | g << 8 | b;
        } else if (getTypeCc().equals("hsv")) {
            ArrayList<float[]> colorListHsv = getColorListHsv();
            if (colorListHsv.size() == 1) {
                return hsvToRgb(colorListHsv.get(0)[0], colorListHsv.get(0)[1], colorListHsv.get(0)[2]);
            }
            int part = (int) (time * (float) (colorListHsv.size() - 1)) % (colorListHsv.size() - 1);
            float progress = time * (float) (colorListHsv.size() - 1) % 1.0F;
            float h = colorListHsv.get(part)[0] * (1.F - progress) + colorListHsv.get(part + 1)[0] * (progress);
            float s = colorListHsv.get(part)[1] * (1.F - progress) + colorListHsv.get(part + 1)[1] * (progress);
            float v = colorListHsv.get(part)[2] * (1.F - progress) + colorListHsv.get(part + 1)[2] * (progress);
            return hsvToRgb(h, s, v);
        }
        return null;
    }
}
