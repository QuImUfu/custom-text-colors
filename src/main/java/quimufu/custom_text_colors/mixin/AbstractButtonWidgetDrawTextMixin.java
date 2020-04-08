package quimufu.custom_text_colors.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.GameOptionSliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(AbstractButtonWidget.class)
public class AbstractButtonWidgetDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "renderButton",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/AbstractButtonWidget;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(AbstractButtonWidget AbstractButtonWidget, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        switch (color & 0xffffff) {
            case 16777215:
                if (this.getClass().equals(GameOptionSliderWidget.class)) {
                    AbstractButtonWidget.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("AbstractButtonWidget.textColor.activeSlider", color));
                    return;
                }
                if (this.getClass().equals(TextFieldWidget.class)) {
                    AbstractButtonWidget.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("AbstractButtonWidget.textColor.textField", color));
                    return;
                }
                AbstractButtonWidget.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("AbstractButtonWidget.textColor.active", color));
                return;
            case 10526880:
                AbstractButtonWidget.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("AbstractButtonWidget.textColor.inactive", color));
                return;
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        AbstractButtonWidget.drawCenteredString(textRenderer, str, centerX, y, color);
    }

}

