package quimufu.custom_text_colors.mixin.gui_window;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(AnvilScreen.class)
public class AnvilScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawTextShadowedChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        switch (color) {
            case 16736352:
                return textRenderer.drawWithShadow(text, x, y, tcm.getColor("AnvilScreen.textColor.negative", color));
            case 8453920:
                return textRenderer.drawWithShadow(text, x, y, tcm.getColor("AnvilScreen.textColor.positive", color));
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("AnvilScreen.textColor.heading", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }
}

