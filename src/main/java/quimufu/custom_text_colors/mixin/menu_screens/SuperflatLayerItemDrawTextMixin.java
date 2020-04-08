package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(targets = "net/minecraft/client/gui/screen/CustomizeFlatLevelScreen$SuperflatLayersListWidget$SuperflatLayerItem")
public abstract class SuperflatLayerItemDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIIIIIIZF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawStringChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 16777215) {
            return textRenderer.draw(text, x, y, tcm.getColor("SuperflatLayerItem.textColor.layer", color));
        }
        CustomTextColors.log(Level.ERROR, "unknown value: " + color);
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        return textRenderer.draw(text, x, y, color);
    }

}

