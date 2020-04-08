package quimufu.custom_text_colors.mixin.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.DebugHud;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(DebugHud.class)
public class DebugHudDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "renderLeftText",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextLeftChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 14737632) {
            return textRenderer.draw(text, x, y, tcm.getColor("DebugHud.textColor.left", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "renderRightText",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextRightChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 14737632) {
            return textRenderer.draw(text, x, y, tcm.getColor("DebugHud.textColor.right", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }
}

