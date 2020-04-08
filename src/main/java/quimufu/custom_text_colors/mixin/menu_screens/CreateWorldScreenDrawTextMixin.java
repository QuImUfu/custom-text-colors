package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(CreateWorldScreen createWorldScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        switch (color) {
            case -1:
                createWorldScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("CreateWorldScreen.textColor.heading", color));
                return;
            case -6250336:
                createWorldScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("CreateWorldScreen.textColor.fields", color));
                return;
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        createWorldScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(CreateWorldScreen createWorldScreen, TextRenderer textRenderer, String str, int x, int y, int color) {
        if (color == -6250336) {
            createWorldScreen.drawString(textRenderer, str, x, y, tcm.getColor("CreateWorldScreen.textColor.fields", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        createWorldScreen.drawString(textRenderer, str, x, y, color);
    }
}

