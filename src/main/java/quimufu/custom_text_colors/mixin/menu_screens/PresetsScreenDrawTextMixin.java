package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.PresetsScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(PresetsScreen.class)
public class PresetsScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/PresetsScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(PresetsScreen presetsScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            presetsScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("PresetsScreen.textColor.heading", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        presetsScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/PresetsScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 0))
    private void drawStringShareChangeColor(PresetsScreen presetsScreen, TextRenderer textRenderer, String str, int x, int y, int color) {
        if (color == 10526880) {
            presetsScreen.drawString(textRenderer, str, x, y, tcm.getColor("PresetsScreen.textColor.share", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        presetsScreen.drawString(textRenderer, str, x, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/PresetsScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 1))
    private void drawStringListChangeColor(PresetsScreen presetsScreen, TextRenderer textRenderer, String str, int x, int y, int color) {
        if (color == 10526880) {
            presetsScreen.drawString(textRenderer, str, x, y, tcm.getColor("PresetsScreen.textColor.list", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        presetsScreen.drawString(textRenderer, str, x, y, color);
    }
}

