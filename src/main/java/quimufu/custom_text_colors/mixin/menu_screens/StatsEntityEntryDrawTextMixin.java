package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(targets = "net/minecraft/client/gui/screen/StatsScreen$EntityStatsListWidget$Entry")
public class StatsEntityEntryDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "Lnet/minecraft/client/gui/screen/StatsScreen$EntityStatsListWidget$Entry;render(IIIIIIIZF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/StatsScreen$EntityStatsListWidget;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawStringChangeColor(@Coerce AlwaysSelectedEntryListWidget entityStatsListWidget, TextRenderer textRenderer, String str, int x, int y, int color) {
        switch (color) {
            case 16777215:
                entityStatsListWidget.drawString(textRenderer, str, x, y, tcm.getColor("EntityStatsList.textColor.entity", color));
                return;
            case 6316128:
                entityStatsListWidget.drawString(textRenderer, str, x, y, tcm.getColor("EntityStatsList.textColor.killsZero", color));
                return;
            case 9474192:
                entityStatsListWidget.drawString(textRenderer, str, x, y, tcm.getColor("EntityStatsList.textColor.kills", color));
                return;
            default:
                CustomTextColors.log(Level.ERROR, "unknown value: " + color);
                CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        entityStatsListWidget.drawString(textRenderer, str, x, y, color);
    }
}

