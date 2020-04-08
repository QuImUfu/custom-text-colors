package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.StatsScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(StatsScreen.class)
public class StatsScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/StatsScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 0))
    private void drawCenteredStringLoTChangeColor(StatsScreen StatsScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            StatsScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("StatsScreen.textColor.downloading.title", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        StatsScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/StatsScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 1))
    private void drawCenteredStringLoPChangeColor(StatsScreen StatsScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            StatsScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("StatsScreen.textColor.downloading.progress", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        StatsScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/StatsScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 2))
    private void drawCenteredStringChangeColor(StatsScreen StatsScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            StatsScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("StatsScreen.textColor.heading", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        StatsScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

}

