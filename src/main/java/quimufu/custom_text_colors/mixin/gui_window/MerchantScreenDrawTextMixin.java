package quimufu.custom_text_colors.mixin.gui_window;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(MerchantScreen.class)
public class MerchantScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 0))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("MerchantScreen.textColor.heading", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 1))
    private int drawTextInvChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("MerchantScreen.textColor.inventory.heading", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 3))
    private int drawTextChangeColor2(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("MerchantScreen.textColor.heading", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 4))
    private int drawTextInvChangeColor2(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("MerchantScreen.textColor.inventory.heading", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }


    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 2))
    private int drawTextLvlChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("MerchantScreen.textColor.level", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 5))
    private int drawTextTradesChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("MerchantScreen.textColor.trades", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }
}

