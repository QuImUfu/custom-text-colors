package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public abstract class ServerEntryDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerScreen;setTooltip(Ljava/lang/String;)V",
                    ordinal = 0))
    private void drawTooltipSetColor(MultiplayerScreen multiplayerScreen, String text) {
        if (text == null) {
            multiplayerScreen.setTooltip(null);
            return;
        }
        String[] sa = text.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String s : sa) {
            if (!(sb.length() == 0))
                sb.append("\n");
            int color = tcm.getColor("Tooltip.textColor.server.ping", -1);
            sb.append("§§");
            sb.append(color);
            sb.append("§§");
            sb.append(s);
        }
        multiplayerScreen.setTooltip(sb.toString());
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerScreen;setTooltip(Ljava/lang/String;)V",
                    ordinal = 1))
    private void drawTooltipSetColor2(MultiplayerScreen multiplayerScreen, String text) {
        if (text == null) {
            multiplayerScreen.setTooltip(null);
            return;
        }
        String[] sa = text.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String s : sa) {
            if (!(sb.length() == 0))
                sb.append("\n");
            int color = tcm.getColor("Tooltip.textColor.server.playerList", -1);
            sb.append("§§");
            sb.append(color);
            sb.append("§§");
            sb.append(s);
        }
        multiplayerScreen.setTooltip(sb.toString());
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 0))
    private int drawStringNameChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 16777215) {
            return textRenderer.draw(text, x, y, tcm.getColor("ServerEntry.textColor.name", color));
        }
        CustomTextColors.log(Level.ERROR, "unknown value: " + color);
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 1))
    private int drawStringLabelChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 8421504) {
            return textRenderer.draw(text, x, y, tcm.getColor("ServerEntry.textColor.label", color));
        }
        CustomTextColors.log(Level.ERROR, "unknown value: " + color);
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 2))
    private int drawStringChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 8421504) {
            String[] formatGroups = text.split("§");
            int i = 0;
            int returnVal = 0;
            String s1;
            for (String s : formatGroups) {
                if (s.isEmpty()) continue;
                switch (Formatting.byCode(s.charAt(0))) {
                    case GRAY:
                        s1 = s.substring(1);
                        returnVal += textRenderer.draw(s1, x + i, y, tcm.getColor("ServerEntry.textColor.playerCounts", Formatting.GRAY.getColorValue()));
                        i += textRenderer.getStringWidth(s1);
                        break;
                    case DARK_GRAY:
                        s1 = s.substring(1);
                        if (formatGroups.length >= 2) {
                            returnVal += textRenderer.draw(s1, x + i, y, tcm.getColor("ServerEntry.textColor.playerCountSeparator", Formatting.DARK_GRAY.getColorValue()));
                        } else {
                            returnVal += textRenderer.draw(s1, x + i, y, tcm.getColor("ServerEntry.textColor.unknownStatus", Formatting.DARK_GRAY.getColorValue()));
                        }
                        i += textRenderer.getStringWidth(s1);
                        break;
                    case DARK_RED:
                        s1 = s.substring(1);
                        returnVal += textRenderer.draw(s1, x + i, y, tcm.getColor("ServerEntry.textColor.versionMismatch", Formatting.DARK_RED.getColorValue()));
                        i += textRenderer.getStringWidth(s1);
                }
            }
            return returnVal;
        }
        CustomTextColors.log(Level.ERROR, "unknown value: " + color);
        CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        return textRenderer.draw(text, x, y, color);
    }

}

