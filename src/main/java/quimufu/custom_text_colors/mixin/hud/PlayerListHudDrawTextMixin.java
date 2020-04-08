package quimufu.custom_text_colors.mixin.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.ChangingColor;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(PlayerListHud.class)
public class PlayerListHudDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I",
                    ordinal = 0))
    private int drawTextShadowedHeaderChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == -1) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("PlayerListHud.textColor.header", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I",
                    ordinal = 1))
    private int drawTextShadowedSpectatorChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == -1862270977) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("PlayerListHud.textColor.spectator", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I",
                    ordinal = 2))
    private int drawTextShadowedPlayerChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == -1) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("PlayerListHud.textColor.player", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I",
                    ordinal = 3))
    private int drawTextShadowedFooterChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == -1) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("PlayerListHud.textColor.footer", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

    @Redirect(method = "renderScoreboardObjective",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawTextChangeColorChange(TextRenderer textRenderer, String text, float x, float y, int color) {
        if ((color & 255) == 255) {
            String newText = text.replaceFirst(Formatting.YELLOW.toString(), "");
            return textRenderer.drawWithShadow(newText, x, y, tcm.getColor("PlayerListHud.textColor.score", 16777045));
        }
        float time = (float) (color >> 16) / 255.F;
        ChangingColor cc = tcm.getColorChange("PlayerListHud.textColor.ChangingColor.health");
        Integer colorNew = cc.getCurrentColor(time);
        if (colorNew != null) return textRenderer.drawWithShadow(text, x, y, colorNew);
        CustomTextColors.log(Level.ERROR, "something went wrong changing color! (type was neither hsv nor rgb)");
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        return color;
    }
}

