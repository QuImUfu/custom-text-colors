package quimufu.custom_text_colors.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.OutOfMemoryScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;

/*TODO: Test*/
@Mixin(OutOfMemoryScreen.class)
public class OutOfMemoryScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/OutOfMemoryScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(OutOfMemoryScreen OutOfMemoryScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            OutOfMemoryScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("OutOfMemoryScreen.textColor.heading", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        OutOfMemoryScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/OutOfMemoryScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(OutOfMemoryScreen OutOfMemoryScreen, TextRenderer textRenderer, String str, int x, int y, int color) {
        if (color == 10526880) {
            OutOfMemoryScreen.drawString(textRenderer, str, x, y, tcm.getColor("OutOfMemoryScreen.textColor.fields", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        OutOfMemoryScreen.drawString(textRenderer, str, x, y, color);
    }
}

