package quimufu.custom_text_colors;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "custom-text-colors")
class CustomTextColorsConfig implements ConfigData {


    boolean randomMissingColors = false;
    boolean eyesoreRandom = false;
}