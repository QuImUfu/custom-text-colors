package quimufu.custom_text_colors.mixin.gui_darkend;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
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


@Mixin(ConfirmScreen.class)
public class ConfirmScreenDrawTextMixin extends Screen {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);
    @Shadow
    @Final
    private Text message;

    private ConfirmScreenDrawTextMixin(Text title) {
        super(title);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ConfirmScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 0))
    private void drawCenteredStringChangeColor(ConfirmScreen confirmScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            if (this.getClass().equals(ConfirmChatLinkScreen.class)) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ConfirmChatLinkScreen.textColor.body", color));
                return;
            } else if (calledFrom("deathScreen")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("DeathScreen.textColor.confirm.heading", color));
                return;
            } else if (calledFrom("texturePrompt")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ServerResourcePackPrompt.textColor.heading", color));
                return;
            } else if (calledFrom("resourcePack.incompatible")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ResourcePack.textColor.incompatible.heading", color));
                return;
            } else if (calledFrom("difficulty.lock")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("Difficulty.textColor.confirm.lock.heading", color));
                return;
            } else if (calledFrom("selectWorld.delete")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldList.textColor.confirm.delete.heading", color));
                return;
            } else if (calledFrom("selectWorld.recreate")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldList.textColor.confirm.recreate.heading", color));
                return;
            } else if (calledFrom("selectWorld.version")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldList.textColor.confirm.open.futureWorld.heading", color));
                return;
            } else if (calledFrom("selectServer.delete")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ServerList.textColor.confirm.delete.heading", color));
                return;
            } else {
                CustomTextColors.log(Level.ERROR, "unknown caller. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
            }
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        confirmScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    private boolean calledFrom(String cName) {
        return message.getClass().equals(TranslatableText.class) && ((TranslatableText) message).getKey().contains(cName)
                || super.getTitle().getClass().equals(TranslatableText.class) && ((TranslatableText) super.getTitle()).getKey().contains(cName);
    }

    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ConfirmScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 1))
    private void drawCenteredStringChangeColor2(ConfirmScreen confirmScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            if (this.getClass().equals(ConfirmChatLinkScreen.class)) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ConfirmChatLinkScreen.textColor.link", color));
                return;
            } else if (calledFrom("deathScreen")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("DeathScreen.textColor.confirm.message", color));
                return;
            } else if (calledFrom("texturePrompt")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ServerResourcePackPrompt.textColor.message", color));
                return;
            } else if (calledFrom("resourcePack.incompatible")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ResourcePack.textColor.incompatible.message", color));
                return;
            } else if (calledFrom("difficulty.lock")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("Difficulty.textColor.confirm.lock.message", color));
                return;
            } else if (calledFrom("selectWorld.delete")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldList.textColor.confirm.delete.message", color));
                return;
            } else if (calledFrom("selectWorld.recreate")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldList.textColor.confirm.legacyRecreate.message", color));
                return;
            } else if (calledFrom("selectWorld.version")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("WorldList.textColor.confirm.open.futureWorld.message", color));
                return;
            } else if (calledFrom("selectServer.delete")) {
                confirmScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("ServerList.textColor.confirm.delete.message", color));
                return;
            } else {
                CustomTextColors.log(Level.ERROR, "unknown caller. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
            }
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        confirmScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }
}