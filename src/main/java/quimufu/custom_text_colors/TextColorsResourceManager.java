package quimufu.custom_text_colors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import quimufu.custom_text_colors.jankson.*;
import quimufu.custom_text_colors.jankson.api.SyntaxError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static quimufu.custom_text_colors.CustomTextColors.log;

public class TextColorsResourceManager implements SimpleSynchronousResourceReloadListener {

    private static final Jankson GSON = new Jankson.Builder().build();
    private static TextColorsResourceManager instance;
    protected final Map<String, Map<String, Integer>> colorRegistries = Maps.newHashMap();
    protected final Map<String, Map<String, ChangingColor>> changingColorRegistries = Maps.newHashMap();
    protected final Map<String, NamespacedTextColorsRegistry> namespacedRegistries = Maps.newHashMap();
    protected final Map<String, Default> defaults = Maps.newHashMap();
    protected final Map<String, Multimap<String, String>> colorGroupRegistries = Maps.newHashMap();

    private TextColorsResourceManager() {
    }

    public void setDefault(String namespace, Default d) {
        defaults.put(namespace, d);
    }


    public static TextColorsResourceManager getInstance() {
        if (instance == null) {
            instance = new TextColorsResourceManager();
        }
        return instance;
    }

    public NamespacedTextColorsRegistry getNamespacedTextColorsRegistry(String ns) {
        if (!namespacedRegistries.containsKey(ns)) {
            namespacedRegistries.put(ns, new NamespacedTextColorsRegistry(
                    ns,
                    colorRegistries.get(ns) != null ? colorRegistries.get(ns) : Maps.newHashMap(),
                    changingColorRegistries.get(ns) != null ? changingColorRegistries.get(ns) : Maps.newHashMap(),
                    defaults.get(ns) != null ? defaults.get(ns) : new DummyDefault()
            ));
        }
        return namespacedRegistries.get(ns);
    }

    @Override
    public void apply(ResourceManager manager) {
        Set<String> namespacesO = manager.getAllNamespaces();
        log(Level.INFO, "(Re)Loading color_groups.json");
        for (String ns : namespacesO) {
            try {
                Multimap<String, String> tempColorGroups = HashMultimap.create(7, 14);
                for (Resource res : manager.getAllResources(new Identifier(ns, "color_groups.json"))) {
                    loadColorGroupForNamespace(res, ns, tempColorGroups);
                    colorGroupRegistries.put(ns, HashMultimap.create(7, 14));
                    colorGroupRegistries.get(ns).putAll(tempColorGroups);
                }
            } catch (FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        log(Level.INFO, "(Re)Loading colors.json");
        for (String ns : namespacesO) {
            colorRegistries.put(ns, Maps.newHashMap());
            changingColorRegistries.put(ns, Maps.newHashMap());
            defaults.putIfAbsent(ns, new DummyDefault());
            try {
                Map<String, Integer> cr = Maps.newHashMap();
                Map<String, ChangingColor> ccr = Maps.newHashMap();
                for (Resource res : manager.getAllResources(new Identifier(ns, "colors.json"))) {
                    loadForNamespace(res, ns, cr, ccr);
                    colorRegistries.get(ns).putAll(cr);
                    changingColorRegistries.get(ns).putAll(ccr);
                }
            } catch (FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!namespacedRegistries.containsKey(ns)) {
                namespacedRegistries.put(ns, new NamespacedTextColorsRegistry(ns, colorRegistries.get(ns), changingColorRegistries.get(ns), defaults.get(ns)));
            } else {
                namespacedRegistries.get(ns).reset(
                        colorRegistries.get(ns),
                        changingColorRegistries.get(ns),
                        defaults.get(ns));
            }
        }


    }

    private void loadColorGroupForNamespace(Resource res, String ns, Multimap<String, String> colorGroups) {
        log(Level.INFO, "(Re)Loading color_groups.json from resource pack " + res.getResourcePackName() + " for namespace " + ns);
        colorGroups.clear();
        try {

            InputStream is = res.getInputStream();
            JsonObject jo = GSON.load(is);
            for (Entry<String, JsonElement> entry : jo.entrySet()) {
                if (!(entry.getValue() instanceof JsonArray)) {
                    log(Level.ERROR, "Error parsing entry: " + entry.getKey());
                    log(Level.ERROR, "The entry is not a JsonArray");
                    continue;
                }
                retrieveColorGroup(entry, colorGroups);
            }

        } catch (SyntaxError | IOException e) {
            log(Level.ERROR, "Error parsing colors.json file");
            log(Level.ERROR, e.getMessage());
        }
    }

    private void retrieveColorGroup(Entry<String, JsonElement> entry, Multimap<String, String> colorGroup) {
        for (JsonElement j : (JsonArray) entry.getValue()) {
            if (!(j instanceof JsonPrimitive) || !((((JsonPrimitive) j).getValue()) instanceof String)) {
                log(Level.ERROR, j.toJson() + " is not a Number");
                continue;
            }
            colorGroup.put(entry.getKey(), ((JsonPrimitive) j).asString());
        }
    }

    private void loadForNamespace(Resource res, String namespace, Map<String, Integer> colors, Map<String, ChangingColor> changingColors) {
        log(Level.INFO, "(Re)Loading colors.json from resource pack " + res.getResourcePackName() + " for namespace " + namespace);
        colors.clear();
        changingColors.clear();
        try {

            InputStream is = res.getInputStream();
            JsonObject jo = GSON.load(is);
            for (Entry<String, JsonElement> entry : jo.entrySet()) {
                if (!(entry.getValue() instanceof JsonObject)) {
                    log(Level.ERROR, "Error parsing entry: " + entry.getKey());
                    log(Level.ERROR, "The entry is not a JsonObject");
                    continue;
                }
                if (colorGroupRegistries.get(namespace).containsKey(entry.getKey())) {
                    retrieveColorGroupValues(entry, colors, colorGroupRegistries.get(namespace));
                } else if (entry.getKey().contains("ChangingColor")) {
                    retrieveColorChanging(entry, changingColors);
                } else {
                    retrieveColorValue(entry, colors);
                }
            }

        } catch (SyntaxError | IOException e) {
            log(Level.ERROR, "Error parsing colors.json file");
            log(Level.ERROR, e.getMessage());
        }
    }

    private void retrieveColorGroupValues(Entry<String, JsonElement> entry, Map<String, Integer> colors, Multimap<String, String> groups) {
        JsonObject colorsO = (JsonObject) entry.getValue();
        int colorValue = 0;
        for (Entry<String, JsonElement> color : colorsO.entrySet()) {

            if (!(color.getValue() instanceof JsonPrimitive) || !((((JsonPrimitive) color.getValue()).getValue()) instanceof Long)) {
                log(Level.ERROR, color.getKey() + " is not a Number");
                continue;
            }
            JsonPrimitive jp = (JsonPrimitive) color.getValue();
            switch (color.getKey()) {
                case "red":
                    int red = (int) (long) jp.getValue();
                    red = checkValidColor(red);
                    colorValue |= (red << 16);
                    break;
                case "green":
                    int green = (int) (long) jp.getValue();
                    green = checkValidColor(green);
                    colorValue |= (green << 8);
                    break;
                case "blue":
                    int blue = (int) (long) jp.getValue();
                    blue = checkValidColor(blue);
                    colorValue |= blue;
                    break;
                default:
                    log(Level.ERROR, "Error parsing colors.json file");
                    log(Level.ERROR, entry.getKey() + " includes unknown key: " + color.getKey());
                    log(Level.ERROR, "Ignoring!");

            }
        }
        for (String value : groups.get(entry.getKey())) {
            if (colors.containsKey(value)) {
                continue;
            }
            colors.put(normalize(value), colorValue);
        }
    }

    private void retrieveColorChanging(Entry<String, JsonElement> entry, Map<String, ChangingColor> changingColors) {
        log(Level.INFO, "creating ChangingColor " + entry.getKey());
        changingColors.put(normalize(entry.getKey()), new ChangingColorImpl(entry));
    }

    private void retrieveColorValue(Entry<String, JsonElement> entry, Map<String, Integer> colors) {
        JsonObject colorsO = (JsonObject) entry.getValue();
        int colorValue = 0;
        for (Entry<String, JsonElement> color : colorsO.entrySet()) {

            if (!(color.getValue() instanceof JsonPrimitive) || !((((JsonPrimitive) color.getValue()).getValue()) instanceof Long)) {
                log(Level.ERROR, color.getKey() + " is not a Number");
                continue;
            }
            JsonPrimitive jp = (JsonPrimitive) color.getValue();
            switch (color.getKey()) {
                case "red":
                    int red = (int) (long) jp.getValue();
                    red = checkValidColor(red);
                    colorValue |= (red << 16);
                    break;
                case "green":
                    int green = (int) (long) jp.getValue();
                    green = checkValidColor(green);
                    colorValue |= (green << 8);
                    break;
                case "blue":
                    int blue = (int) (long) jp.getValue();
                    blue = checkValidColor(blue);
                    colorValue |= blue;
                    break;
                default:
                    log(Level.ERROR, "Error parsing colors.json file");
                    log(Level.ERROR, entry.getKey() + " includes unknown key: " + color.getKey());
                    log(Level.ERROR, "Ignoring!");

            }
        }
        colors.put(normalize(entry.getKey()), colorValue);
    }

    public static int checkValidColor(int color) {
        if (color < 0 || color > 255) {
            log(Level.ERROR, "Error parsing colors.json file");
            log(Level.ERROR, "color not between 0 and 255 (inclusive)");
            int newcolor = Math.max(0, Math.min(255, color));
            log(Level.ERROR, "truncated: " + color + " to: " + newcolor);
            return newcolor;
        } else {
            return color;
        }
    }


    @Override
    public Identifier getFabricId() {
        return new Identifier("quimufu:" + CustomTextColors.MOD_ID);
    }

    public static String normalize(String s){
        return s.toLowerCase(Locale.ENGLISH);
    }

}
