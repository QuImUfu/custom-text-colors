package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(targets = "net/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget")
public class ItemStatsListWidgetDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "Lnet/minecraft/client/gui/screen/StatsScreen$ItemStatsListWidget;render(Lnet/minecraft/text/Text;II)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawTextShadowedChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == -1) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("ItemStatsList.textColor.tooltip", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

}

