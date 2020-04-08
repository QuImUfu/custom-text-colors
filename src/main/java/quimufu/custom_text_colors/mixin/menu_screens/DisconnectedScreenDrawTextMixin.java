package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/DisconnectedScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(DisconnectedScreen disconnectedScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        switch (color) {
            case 16777215:
                disconnectedScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("DisconnectedScreen.textColor.reason", color));
                return;
            case 11184810:
                disconnectedScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("DisconnectedScreen.textColor.heading", color));
                return;
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        disconnectedScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

}

