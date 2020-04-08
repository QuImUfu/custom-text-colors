package quimufu.custom_text_colors.mixin.gui_window;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(BookEditScreen.class)
public abstract class BookEditScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Shadow
    protected abstract int getStringWidth(String text);

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawTrimmed(Ljava/lang/String;IIII)V"))
    private void drawTextTrimmedChangeColor(TextRenderer textRenderer, String text, int x, int y, int maxWidth, int color) {
        if (color == 0) {
            textRenderer.drawTrimmed(text, x, y, maxWidth, tcm.getColor("BookEditScreen.textColor.content", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        textRenderer.drawTrimmed(text, x, y, maxWidth, color);
    }


    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 0))
    private int drawTextChangeColor0(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 0) {
            return textRenderer.draw(text, x, y, tcm.getColor("BookEditScreen.textColor.heading.title", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 1))
    private int drawTextChangeColor1(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 0) {
            String texti = text.substring(0, text.lastIndexOf('§'));
            String cursor = text.substring(text.lastIndexOf('§'));
            int i = 0;
            if (!texti.isEmpty()) {
                i += textRenderer.draw(texti, x, y, tcm.getColor("BookEditScreen.textColor.sign.title", color));
            }
            if (cursor.contains(Formatting.BLACK.toString())) {
                int cursorColor = tcm.getColor("BookEditScreen.textColor.sign.darkCursor", Formatting.BLACK.getColorValue());
                return i + textRenderer.draw(cursor.substring(2), x + getStringWidth(texti), y, cursorColor);
            } else if (cursor.contains(Formatting.GRAY.toString())) {
                int cursorColor = tcm.getColor("BookEditScreen.textColor.sign.lightCursor", Formatting.GRAY.getColorValue());
                return i + textRenderer.draw(cursor.substring(2), x + getStringWidth(texti), y, cursorColor);
            } else {
                CustomTextColors.log(Level.ERROR, "unknown formatting: " + text);
                CustomTextColors.log(Level.ERROR, "we know: " + Formatting.BLACK.toString() + " and " + Formatting.BLACK.toString());
            }
        }
        CustomTextColors.log(Level.ERROR, "unknown value: " + color);
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        return textRenderer.draw(text, x, y, color);

    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 2))
    private int drawTextChangeColor2(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 0) {
            String textNew = text.replaceFirst(Formatting.DARK_GRAY.toString(), "");
            return textRenderer.draw(textNew, x, y, tcm.getColor("BookEditScreen.textColor.sign.playerName", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 3))
    private int drawTextChangeColor3(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 0) {
            return textRenderer.draw(text, x, y, tcm.getColor("BookEditScreen.textColor.page", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 4))
    private int drawTextChangeColor4(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 0) {
            return textRenderer.draw(text, x, y, tcm.getColor("BookEditScreen.textColor.cursor", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawableHelper;fill(IIIII)V"))
    private void drawCursorChangeColor(int x1, int y1, int x2, int y2, int color) {
        if (color == -16777216) {
            DrawableHelper.fill(x1, y1, x2, y2, tcm.getColor("BookEditScreen.textColor.cursor", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        DrawableHelper.fill(x1, y1, x2, y2, color);
    }
}

