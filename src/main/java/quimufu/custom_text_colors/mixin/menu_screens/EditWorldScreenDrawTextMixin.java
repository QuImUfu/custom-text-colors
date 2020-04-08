package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(EditWorldScreen.class)
public class EditWorldScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/EditWorldScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(EditWorldScreen editWorldScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            editWorldScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("EditWorldScreen.textColor.heading", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        editWorldScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/EditWorldScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(EditWorldScreen editWorldScreen, TextRenderer textRenderer, String str, int x, int y, int color) {
        if (color == 10526880) {
            editWorldScreen.drawString(textRenderer, str, x, y, tcm.getColor("EditWorldScreen.textColor.field", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        editWorldScreen.drawString(textRenderer, str, x, y, color);
    }
}

