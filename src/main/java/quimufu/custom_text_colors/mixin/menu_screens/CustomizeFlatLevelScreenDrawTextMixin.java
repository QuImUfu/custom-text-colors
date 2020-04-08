package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.CustomizeFlatLevelScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(CustomizeFlatLevelScreen.class)
public class CustomizeFlatLevelScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/CustomizeFlatLevelScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(CustomizeFlatLevelScreen customizeFlatLevelScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            customizeFlatLevelScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("CustomizeFlatLevelScreen.textColor.heading", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        customizeFlatLevelScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/CustomizeFlatLevelScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(CustomizeFlatLevelScreen customizeFlatLevelScreen, TextRenderer textRenderer, String str, int x, int y, int color) {
        if (color == 16777215) {
            customizeFlatLevelScreen.drawString(textRenderer, str, x, y, tcm.getColor("CustomizeFlatLevelScreen.textColor.fields", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        customizeFlatLevelScreen.drawString(textRenderer, str, x, y, color);
    }
}

