package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.options.LanguageOptionsScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(LanguageOptionsScreen.class)
public class LanguageOptionsScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/options/LanguageOptionsScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(LanguageOptionsScreen LanguageOptionsScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        switch (color) {
            case 16777215:
                LanguageOptionsScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("LanguageOptionsScreen.textColor.heading", color));
                return;
            case 8421504:
                LanguageOptionsScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("LanguageOptionsScreen.textColor.warning", color));
                return;
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        LanguageOptionsScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

}

