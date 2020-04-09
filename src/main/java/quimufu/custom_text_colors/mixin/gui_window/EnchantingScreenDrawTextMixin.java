package quimufu.custom_text_colors.mixin.gui_window;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.EnchantingScreen;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;

import java.util.List;
import java.util.regex.Pattern;


@Mixin(EnchantingScreen.class)
public class EnchantingScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/EnchantingScreen;renderTooltip(Ljava/util/List;II)V"))
    private void markTooltipList(EnchantingScreen enchantingScreen, List<String> text, int x, int y) {
        for (int i = 0; i < text.size(); i++) {
            String s = text.get(i);
            if (i == 0) {
                text.set(i, reformatEnchantment(s));
            } else {
                text.set(i, reformatEnchantmentCosts(s));
            }
        }
        enchantingScreen.renderTooltip(text, x, y);
    }

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 0))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("EnchantingScreen.textColor.heading", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "drawForeground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I",
                    ordinal = 1))
    private int drawTextInvChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4210752) {
            return textRenderer.draw(text, x, y, tcm.getColor("EnchantingScreen.textColor.inventory.heading", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "drawBackground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawTrimmed(Ljava/lang/String;IIII)V"))
    private void drawTextEnchantChangeColor(TextRenderer textRenderer, String text, int x, int y, int maxWidth, int color) {
        if (color == 3419941) {
            textRenderer.drawTrimmed(text, x, y, maxWidth, tcm.getColor("EnchantingScreen.textColor.notAffordable.glyphs", color));
            return;
        } else if (color == 16777088) {
            textRenderer.drawTrimmed(text, x, y, maxWidth, tcm.getColor("EnchantingScreen.textColor.affordable.glyphsSelected", color));
            return;
        } else if (color == 6839882) {
            textRenderer.drawTrimmed(text, x, y, maxWidth, tcm.getColor("EnchantingScreen.textColor.affordable.glyphsNotSelected", color));
            return;
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        textRenderer.drawTrimmed(text, x, y, maxWidth, color);
    }

    @Redirect(method = "drawBackground",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I"))
    private int drawText___ChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == 4226832) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("EnchantingScreen.textColor.notAffordable.expLevel", color));
        } else if (color == 8453920) {
            return textRenderer.drawWithShadow(text, x, y, tcm.getColor("EnchantingScreen.textColor.affordable.expLevel", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.drawWithShadow(text, x, y, color);
    }

    private String reformatEnchantment(String s) {
        String[] formatGroups = s.split("§");
        StringBuilder s1 = new StringBuilder();
        for (String stf : formatGroups) {
            if (stf.isEmpty()) continue;
            Formatting f = Formatting.byCode(stf.charAt(0));
            if (f.isColor() || f.equals(Formatting.RESET)) {
                int color;

                if (f.equals(Formatting.RESET)) {
                    color = tcm.getColor("Tooltip.textColor.enchanting.continuedClue", -1);
                    s1.append("§r");
                }else if (f.equals(Formatting.WHITE))
                    color = tcm.getColor("Tooltip.textColor.enchanting.continuedClue", f.getColorValue());
                else if (f.equals(Formatting.GRAY))
                    color = tcm.getColor("Tooltip.textColor.enchanting.normal", f.getColorValue());
                else if (f.equals(Formatting.RED))
                    color = tcm.getColor("Tooltip.textColor.enchanting.cursed", f.getColorValue());
                else {
                    CustomTextColors.log(Level.ERROR, "unknown enchantment formatting: " + f.getName());
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

    private String reformatEnchantmentCosts(String s) {
        String[] formatGroups = s.split("§");
        StringBuilder s1 = new StringBuilder();
        for (String stf : formatGroups) {
            if (stf.isEmpty()) continue;
            Formatting f = Formatting.byCode(stf.charAt(0));
            if (f.isColor()) {
                int color;
                if (f.equals(Formatting.RED))
                    color = tcm.getColor("Tooltip.textColor.enchanting.negative", f.getColorValue());
                else if (f.equals(Formatting.GRAY))
                    color = tcm.getColor("Tooltip.textColor.enchanting.positive", f.getColorValue());
                else {
                    CustomTextColors.log(Level.ERROR, "unknown enchantment formatting: " + f.getName());
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

