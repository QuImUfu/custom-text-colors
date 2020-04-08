package quimufu.custom_text_colors.mixin.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.toast.AdvancementToast;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(AdvancementToast.class)
public class AdvancementToastDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "draw",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        switch (color & 0xffffff) {
            case 16777215:
                return textRenderer.draw(text, x, y, tcm.getColor("AdvancementToast.textColor.title", color));
            case 16746751:
                return textRenderer.draw(text, x, y, tcm.getColor("AdvancementToast.textColor.challengeNotice", color));
            case 16776960:
                return textRenderer.draw(text, x, y, tcm.getColor("AdvancementToast.textColor.normalNotice", color));
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }
}

