package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.resourcepack.ResourcePackOptionsScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(ResourcePackOptionsScreen.class)
public class ResourcePackOptionsScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/resourcepack/ResourcePackOptionsScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(ResourcePackOptionsScreen ResourcePackOptionsScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        switch (color) {
            case 16777215:
                ResourcePackOptionsScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ResourcePackOptionsScreen.textColor.heading", color));
                return;
            case 8421504:
                ResourcePackOptionsScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ResourcePackOptionsScreen.textColor.folder", color));
                return;
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        ResourcePackOptionsScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

}

