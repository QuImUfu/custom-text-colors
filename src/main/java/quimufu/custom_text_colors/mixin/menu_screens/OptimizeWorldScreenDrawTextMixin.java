package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.world.OptimizeWorldScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(OptimizeWorldScreen.class)
public class OptimizeWorldScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/OptimizeWorldScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 0))
    private void drawCenteredStringTitleChangeColor(OptimizeWorldScreen optimizeWorldScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            optimizeWorldScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("OptimizeWorldScreen.textColor.heading", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        optimizeWorldScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/OptimizeWorldScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 1))
    private void drawCenteredStringStatusChangeColor(OptimizeWorldScreen optimizeWorldScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 10526880) {
            optimizeWorldScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("OptimizeWorldScreen.textColor.status", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        optimizeWorldScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/OptimizeWorldScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 2))
    private void drawCenteredStringProgress1ChangeColor(OptimizeWorldScreen optimizeWorldScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 10526880) {
            optimizeWorldScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("OptimizeWorldScreen.textColor.progress", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        optimizeWorldScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/OptimizeWorldScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 3))
    private void drawCenteredStringProgress2ChangeColor(OptimizeWorldScreen optimizeWorldScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 10526880) {
            optimizeWorldScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("OptimizeWorldScreen.textColor.progressPercent", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        optimizeWorldScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/OptimizeWorldScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(OptimizeWorldScreen optimizeWorldScreen, TextRenderer textRenderer, String str, int x, int y, int color) {
        if (color == 10526880) {
            optimizeWorldScreen.drawString(textRenderer, str, x, y, tcm.getColor("OptimizeWorldScreen.textColor.counts", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        optimizeWorldScreen.drawString(textRenderer, str, x, y, color);
    }
}

