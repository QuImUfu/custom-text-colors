package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(TitleScreen.class)
public class TitleScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(TitleScreen TitleScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if ((color & 16777215) == 16776960) {
            TitleScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("TitleScreen.textColor.splash", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        TitleScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(TitleScreen TitleScreen, TextRenderer textRenderer, String str, int x, int y, int color) {
        if ((color & 16777215) == 16777215) {
            TitleScreen.drawString(textRenderer, str, x, y, tcm.getColor("TitleScreen.textColor.notice", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        TitleScreen.drawString(textRenderer, str, x, y, color);
    }
}

