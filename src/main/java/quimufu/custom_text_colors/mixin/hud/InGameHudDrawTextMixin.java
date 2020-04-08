package quimufu.custom_text_colors.mixin.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.ChangingColor;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;

import static net.minecraft.util.math.MathHelper.hsvToRgb;


@Mixin(InGameHud.class)
public class InGameHudDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);
    @Shadow
    private boolean overlayTinted;

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;hsvToRgb(FFF)I"))
    private int drawTextChangeColorChange(float hue, float saturation, float value) {
        float time = 1.F - (hue * (5.F / 6.F));
        ChangingColor cc = tcm.getColorChange("InGameHud.textColor.ChangingColor");
        Integer color = cc.getCurrentColor(time);
        if (color != null) return color;
        CustomTextColors.log(Level.ERROR, "something went wrong changing color! (type was neither hsv nor rgb)");
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        return hsvToRgb(hue, saturation, value);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextRenderChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (this.overlayTinted) {
            return textRenderer.draw(text, x, y, color);
        }
        if ((color & 16777215) == 16777215) {
            return (color & -16777216) | textRenderer.draw(text, x, y, tcm.getColor("InGameHud.textColor.overlay", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + (color & 16777215));
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawTextRenderShadowedChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if ((color & 16777215) == 16777215) {
            return textRenderer.draw(text, x, y, tcm.getColor("InGameHud.textColor.title", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + (color & 16777215));
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "renderExperienceBar",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextExBChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        switch (color) {
            case 0:
                return textRenderer.draw(text, x, y, tcm.getColor("InGameHud.textColor.expShadow", color));
            case 8453920:
                return textRenderer.draw(text, x, y, tcm.getColor("InGameHud.textColor.exp", color));
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "renderHeldItemTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawTextHldItmTtShadowedChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if ((color & 16777215) == 16777215) {
            //all HldItmTt are item titles should have a color coding first
            Formatting f = Formatting.byCode(text.charAt(1));
            int colorN = tcm.getColor("InGameHud.textColor.Tooltip.rarityItem." + f.getName(), f.getColorValue());
            return textRenderer.draw(text.substring(2), x, y, colorN);
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "renderDemoTimer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawTextShadowedChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 16777215) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("InGameHud.textColor.demo", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }
}

