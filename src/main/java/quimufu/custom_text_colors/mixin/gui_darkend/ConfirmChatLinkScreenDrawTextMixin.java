package quimufu.custom_text_colors.mixin.gui_darkend;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(ConfirmChatLinkScreen.class)
public class ConfirmChatLinkScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ConfirmChatLinkScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(ConfirmChatLinkScreen confirmChatLinkScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16764108) {
            confirmChatLinkScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ConfirmChatLinkScreen.textColor.heading", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        confirmChatLinkScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }
}