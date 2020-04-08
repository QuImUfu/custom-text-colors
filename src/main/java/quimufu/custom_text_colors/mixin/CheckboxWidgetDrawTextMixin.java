package quimufu.custom_text_colors.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.BackupPromptScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(CheckboxWidget.class)
public class CheckboxWidgetDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "renderButton",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/CheckboxWidget;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(CheckboxWidget CheckboxWidget, TextRenderer textRenderer, String str, int x, int y, int color) {
        if ((color & 0xffffff) == 14737632) {
            if (MinecraftClient.getInstance().currentScreen == null) {
                return;
            } else if (MinecraftClient.getInstance().currentScreen.getClass().equals(MultiplayerWarningScreen.class)) {
                CheckboxWidget.drawString(textRenderer, str, x, y, tcm.getColor("MultiplayerWarningScreen.textColor.checkbox", color));
                return;
            } else if (MinecraftClient.getInstance().currentScreen.getClass().equals(BackupPromptScreen.class)) {
                CheckboxWidget.drawString(textRenderer, str, x, y, tcm.getColor("BackupPromptScreen.textColor.checkbox", color));
                return;
            } else {
                CustomTextColors.log(Level.ERROR, "unknown checkbox screen: " + MinecraftClient.getInstance().currentScreen.getClass().getName());
            }
        }
        CustomTextColors.log(Level.ERROR, "unknown value: " + color);
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        CheckboxWidget.drawString(textRenderer, str, x, y, color);
    }
}

