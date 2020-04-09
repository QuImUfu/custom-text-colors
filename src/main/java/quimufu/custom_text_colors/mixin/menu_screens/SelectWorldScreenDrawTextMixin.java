package quimufu.custom_text_colors.mixin.menu_screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;

import java.util.List;


@Mixin(SelectWorldScreen.class)
public class SelectWorldScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);


    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/SelectWorldScreen;renderTooltip(Ljava/util/List;II)V"))
    private void markTooltipList(SelectWorldScreen selectWorldScreen, List<String> text, int x, int y) {
        for (int i = 0; i < text.size(); i++) {
            String s = text.get(i);
            text.set(i, reformatWorldTooltip(s));
        }
        selectWorldScreen.renderTooltip(text, x, y);
    }


    @Redirect(method = "render(IIF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/SelectWorldScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void drawCenteredStringChangeColor(SelectWorldScreen SelectWorldScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        if (color == 16777215) {
            SelectWorldScreen.drawCenteredString(textRenderer, str, centerX, y, tcm.getColor("SelectWorldScreen.textColor.heading", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        SelectWorldScreen.drawCenteredString(textRenderer, str, centerX, y, color);
    }

    private String reformatWorldTooltip(String s) {
        String[] formatGroups = s.split("§");
        StringBuilder s1 = new StringBuilder();
        for (String stf : formatGroups) {
            if (stf.isEmpty()) continue;
            Formatting f = Formatting.byCode(stf.charAt(0));
            if (f.isColor()) {
                int color;

                if (f.equals(Formatting.GOLD))
                    color = tcm.getColor("Tooltip.textColor.world.notStable", f.getColorValue());
                else if (f.equals(Formatting.RED))
                    color = tcm.getColor("Tooltip.textColor.world.incompatible", f.getColorValue());
                else {
                    CustomTextColors.log(Level.ERROR, "unknown world tooltip formatting: " + f.getName());
                    color = f.getColorValue();
                }
                s1.append("§§");
                s1.append(color);
                s1.append("§§");
                s1.append(stf.substring(1));
            } else {
                s1.append("§");
                s1.append(stf);

            }

        }
        return s1.toString();
    }
}

