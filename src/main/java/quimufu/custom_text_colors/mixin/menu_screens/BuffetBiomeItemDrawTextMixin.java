package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;

@Mixin(targets = "net/minecraft/client/gui/screen/CustomizeBuffetLevelScreen$BuffetBiomesListWidget$BuffetBiomeItem")
public abstract class BuffetBiomeItemDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIIIIIIZF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/CustomizeBuffetLevelScreen$BuffetBiomesListWidget;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(@Coerce DrawableHelper buffetBiomesListWidget, TextRenderer textRenderer, String str, int x, int y, int color) {
        if (color == 16777215) {
            buffetBiomesListWidget.drawString(textRenderer, str, x, y, tcm.getColor("BuffetBiomeItem.textColor.biome", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        buffetBiomesListWidget.drawString(textRenderer, str, x, y, color);
    }

}

