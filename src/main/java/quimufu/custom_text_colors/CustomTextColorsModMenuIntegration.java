package quimufu.custom_text_colors;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

public class CustomTextColorsModMenuIntegration implements ModMenuApi {
    @Override
    public String getModId() {
        return CustomTextColors.MOD_ID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(CustomTextColorsConfig.class, parent).get();
    }
}
