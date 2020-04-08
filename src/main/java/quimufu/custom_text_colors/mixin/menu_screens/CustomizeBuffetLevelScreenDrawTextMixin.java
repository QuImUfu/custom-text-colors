package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(CustomizeBuffetLevelScreen.class)
public class CustomizeBuffetLevelScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/CustomizeBuffetLevelScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(CustomizeBuffetLevelScreen CustomizeBuffetLevelScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        switch (color) {
            case 16777215:
                CustomizeBuffetLevelScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("CustomizeBuffetLevelScreen.textColor.heading", color));
                return;
            case 10526880:
                CustomizeBuffetLevelScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("CustomizeBuffetLevelScreen.textColor.fields", color));
                return;
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        CustomizeBuffetLevelScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

}

