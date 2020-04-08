package quimufu.custom_text_colors.mixin.commandSuggestor;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.CommandSuggestor;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(CommandSuggestor.SuggestionWindow.class)
public class SuggestionWindowDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawTextShadowedChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        switch (color) {
            case -256:
                return textRenderer.drawWithShadow(text, x, y, tcm.getColor("CommandSuggestor.textColor.selectedEntry", color));
            case -5592406:
                return textRenderer.drawWithShadow(text, x, y, tcm.getColor("CommandSuggestor.textColor.entry", color));
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

}

