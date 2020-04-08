package quimufu.custom_text_colors.mixin.gui_window;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(SignEditScreen.class)
public class SignEditScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/SignEditScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 0))
    private void drawCenteredStringHeadingChangeColor(SignEditScreen SignEditScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            SignEditScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("SignEditScreen.textColor.heading", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        SignEditScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/client/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I",
                    ordinal = 0))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int backgroundColor, int light) {
        if (color == 0) {
            return textRenderer.draw(text, x, y, tcm.getColor("SignEditScreen.textColor.content", color), shadow, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color, shadow, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/client/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I",
                    ordinal = 1))
    private int drawTextCursorChangeColor(TextRenderer textRenderer, String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int backgroundColor, int light) {
        if (color == 0) {
            return textRenderer.draw(text, x, y, tcm.getColor("SignEditScreen.textColor.cursor", color), shadow, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color, shadow, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
    }
}

