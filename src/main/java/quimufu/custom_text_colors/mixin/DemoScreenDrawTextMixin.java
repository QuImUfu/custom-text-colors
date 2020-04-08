package quimufu.custom_text_colors.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.DemoScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(DemoScreen.class)
public class DemoScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawTrimmed(Ljava/lang/String;IIII)V"))
    private void drawTextTrimmedChangeColor(TextRenderer textRenderer, String text, int x, int y, int maxWidth, int color) {
        if (color == 2039583) {
            textRenderer.drawTrimmed(text, x, y, maxWidth, tcm.getColor("DemoScreen.textColor.description", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        textRenderer.drawTrimmed(text, x, y, maxWidth, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        switch (color) {
            case 5197647:
                return textRenderer.draw(text, x, y, tcm.getColor("DemoScreen.textColor.instructions", color));
            case 2039583:
                return textRenderer.draw(text, x, y, tcm.getColor("DemoScreen.textColor.heading", color));
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }
}

