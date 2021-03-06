package quimufu.custom_text_colors.mixin.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.SpectatorHud;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(SpectatorHud.class)
public class SpectatorHudDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "renderSpectatorCommand",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawTextShadowedCommandChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if ((color & 16777215) == 16777215) {
            CustomTextColors.log(Level.INFO, MinecraftClient.getInstance().options.keysHotbar[0].getLocalizedName());
            return textRenderer.drawWithShadow(text, x, y, (-16777216 & color) | tcm.getColor("SpectatorHud.textColor.iconHotkey", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

    @Redirect(method = "render()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawTextShadowedChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if ((color & 16777215) == 16777215) {
            return textRenderer.drawWithShadow(text, x, y, (-16777216 & color) | tcm.getColor("SpectatorHud.textColor.commandTooltip", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }


}

