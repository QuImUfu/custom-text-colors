package quimufu.custom_text_colors.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(NoticeScreen.class)
public class NoticeScreenDrawTextMixin extends Screen {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);
    @Shadow
    @Final
    protected Text notice;

    private NoticeScreenDrawTextMixin(Text title) {
        super(title);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/NoticeScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 0))
    private void drawCenteredStringChangeColor(NoticeScreen confirmScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            if (calledFrom("futureworld")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldListWidget.textColor.errorNotice.futureWorld.heading", color));
                return;
            } else if (calledFrom("recreate")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldListWidget.textColor.errorNotice.recreate.heading", color));
                return;
            }
        }
        CustomTextColors.log(Level.ERROR, "unknown value: " + color);
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        confirmScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    private boolean calledFrom(String cName) {
        return notice.getClass().equals(TranslatableText.class) && ((TranslatableText) notice).getKey().contains(cName)
                || super.getTitle().getClass().equals(TranslatableText.class) && ((TranslatableText) super.getTitle()).getKey().contains(cName);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/NoticeScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 1))
    private void drawCenteredStringChangeColor2(NoticeScreen confirmScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            if (calledFrom("futureworld")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldListWidget.textColor.errorNotice.futureWorld.message", color));
                return;
            } else if (calledFrom("recreate")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldListWidget.textColor.errorNotice.recreate.message", color));
                return;
            }
        }
        CustomTextColors.log(Level.ERROR, "unknown value: " + color);
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        confirmScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }
}