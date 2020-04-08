package quimufu.custom_text_colors.mixin.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.ChangingColor;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(SubtitlesHud.class)
public class SubtitlesHudDrawTextMixin {
    private static double time = 0.D;
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;clampedLerp(DDD)D"))
    private double getPreciseTime(double first, double second, double delta) {
        time = 1 - MathHelper.clamp(1.D, 0.D, delta);
        return MathHelper.clampedLerp(first, second, delta);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;

        if (r == b && b == g) {
            ChangingColor cc = tcm.getColorChange("SubtitlesHud.textColor.ChangingColor.fadingOut");
            return textRenderer.draw(text, x, y, cc.getCurrentColor((float) time) + -16777216);
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }
}

