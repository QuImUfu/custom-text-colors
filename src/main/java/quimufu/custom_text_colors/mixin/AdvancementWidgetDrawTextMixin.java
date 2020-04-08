package quimufu.custom_text_colors.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(AdvancementWidget.class)
public class AdvancementWidgetDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "drawTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I", ordinal = 0))
    private int drawTextShadowedTitleChangeColor1(TextRenderer textRenderer, String text, float x, float y, int color) {
        return drawTextShadowedTitleChangeColor(textRenderer, text, x, y, color);
    }

    @Redirect(method = "drawTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I", ordinal = 2))
    private int drawTextShadowedTitleChangeColor2(TextRenderer textRenderer, String text, float x, float y, int color) {
        return drawTextShadowedTitleChangeColor(textRenderer, text, x, y, color);
    }

    private int drawTextShadowedTitleChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == -1) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("AdvancementWidget.textColor.title", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

    @Redirect(method = "drawTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I", ordinal = 1))
    private int drawTextShadowedProgressChangeColor1(TextRenderer textRenderer, String text, float x, float y, int color) {
        return drawTextShadowedProgressChangeColor(textRenderer, text, x, y, color);
    }

    @Redirect(method = "drawTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I", ordinal = 3))
    private int drawTextShadowedProgressChangeColor2(TextRenderer textRenderer, String text, float x, float y, int color) {
        return drawTextShadowedProgressChangeColor(textRenderer, text, x, y, color);
    }

    private int drawTextShadowedProgressChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == -1) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("AdvancementWidget.textColor.progress", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }


    @Redirect(method = "drawTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == -5592406) {
            return textRenderer.draw(text, x, y, tcm.getColor("AdvancementWidget.textColor.description", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }
}

