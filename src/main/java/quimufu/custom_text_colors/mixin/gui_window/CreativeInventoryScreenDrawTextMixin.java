package quimufu.custom_text_colors.mixin.gui_window;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;

import java.util.List;


@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("CreativeInventoryScreen.textColor.heading", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "renderTooltip(Lnet/minecraft/item/ItemStack;II)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;renderTooltip(Ljava/util/List;II)V"))
    private void drawTextItemTooltipChangeColor(CreativeInventoryScreen screen, List<String> text, int x, int y) {
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

    private String reformatOther(String s) {
        String[] formatGroups = s.split("§");
        StringBuffer s1 = new StringBuffer();
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
        //all titles should have a (the same) color coding first and second in Creative search...
        Formatting f = Formatting.byCode(s.charAt(1));
        int color = tcm.getColor("Tooltip.textColor.rarityItem." + f.getName(), f.getColorValue());
        return "§§" + color + "§§" + s.substring(4);
    }
}

