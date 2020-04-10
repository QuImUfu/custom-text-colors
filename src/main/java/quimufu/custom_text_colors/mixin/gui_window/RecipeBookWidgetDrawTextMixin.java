package quimufu.custom_text_colors.mixin.gui_window;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;

import java.util.List;
import java.util.regex.Pattern;


@Mixin(RecipeBookWidget.class)
public class RecipeBookWidgetDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);
    private final Pattern p = Pattern.compile("§§([-0-9]*)§§");


    @Redirect(method = "drawGhostSlotTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;renderTooltip(Ljava/util/List;II)V"))
    private void drawTextItemTooltipChangeColor(Screen screen, List<String> text, int x, int y) {
        //item tooltip
        for (int i = 0; i < text.size(); i++) {
            String s = text.get(i);
            if (i == 0) {
                text.set(i, reformatTitle(s));
            } else {
                text.set(i, reformatOther(s));
            }
        }
        screen.renderTooltip(text, x, y);
    }

    @Redirect(method = "drawTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;renderTooltip(Ljava/lang/String;II)V"))
    private void drawTextButtonTooltipChangeColor(Screen screen, String text, int x, int y) {
        //Toggle view craftable tooltip
        text = reformatButton(text);
        screen.renderTooltip(text, x, y);
    }

    private String reformatButton(String text) {
        int color = tcm.getColor("Tooltip.textColor.craftable", -1);
        return "§§" + color + "§§" + text;
    }

    private String reformatOther(String s) {
        String[] formatGroups = s.split("§");
        StringBuilder s1 = new StringBuilder();
        for (String stf : formatGroups) {
            if (stf.isEmpty()) continue;
            Formatting f = Formatting.byCode(stf.charAt(0));
            if (f.isColor()) {
                int color = tcm.getColor("Tooltip.textColor.generic." + f.getName(), f.getColorValue());
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

    private String reformatTitle(String s) {
        //all Titles should have a color coding first
        Formatting f = Formatting.byCode(s.charAt(1));
        int color = tcm.getColor("Tooltip.textColor.rarityItem." + f.getName(), f.getColorValue());
        return "§§" + color + "§§" + s.substring(2);
    }

}

