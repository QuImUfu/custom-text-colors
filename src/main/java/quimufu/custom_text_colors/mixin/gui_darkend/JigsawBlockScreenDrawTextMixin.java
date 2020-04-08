package quimufu.custom_text_colors.mixin.gui_darkend;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.JigsawBlockScreen;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(JigsawBlockScreen.class)
public class JigsawBlockScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/JigsawBlockScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(JigsawBlockScreen jigsawBlockScreen, TextRenderer textRenderer, String str, int x, int y, int color) {
        if (color == 10526880) {
            jigsawBlockScreen.drawString(textRenderer, str, x, y, tcm.getColor("JigsawBlockScreen.textColor.fields", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        jigsawBlockScreen.drawString(textRenderer, str, x, y, color);
    }
}

