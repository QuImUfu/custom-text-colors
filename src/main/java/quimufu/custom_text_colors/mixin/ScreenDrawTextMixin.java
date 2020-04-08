package quimufu.custom_text_colors.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Mixin(Screen.class)
public class ScreenDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);
    private final Pattern p = Pattern.compile("§§([-0-9]*)§§");


    @Redirect(method = "renderTooltip(Ljava/util/List;II)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/client/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I"))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int backgroundColor, int light) {
        if (color == -1) {
            Matcher m = p.matcher(text);
            if (m.find()) {
                String[] sa = p.split(text);
                m.reset();
                if (sa.length == 0) {
                    m.find();
                    String grup = m.group(1);
                    int colorN = Integer.parseInt(grup);
                    return textRenderer.draw("", x, y, colorN, shadow, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
                }
                int len = 0;
                int ret = 0;
                if (!sa[0].isEmpty()) {
                    len += textRenderer.getStringWidth(sa[0]);
                    ret += textRenderer.draw(sa[0], x, y, tcm.getColor("Screen.textColor.tooltip", color), shadow, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
                }
                for (int i = 1; i < sa.length; i++) {
                    m.find();
                    String grup = m.group(1);
                    int colorN = Integer.parseInt(grup);
                    ret += textRenderer.draw(sa[i], x + len, y, colorN, shadow, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
                    len += textRenderer.getStringWidth(sa[i]);
                }
                return ret;
            }
            return textRenderer.draw(text, x, y, tcm.getColor("Screen.textColor.tooltip", color), shadow, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color, shadow, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
    }

    @Redirect(method = "renderTooltip(Ljava/util/List;II)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;getStringWidth(Ljava/lang/String;)I"))
    private int getTextWidthIgnoreColor(TextRenderer textRenderer, String text) {
        return textRenderer.getStringWidth(text.replaceAll("§§[-0-9]*§§", ""));
    }

    @Redirect(method = "renderTooltip(Lnet/minecraft/item/ItemStack;II)V",
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
        //all Titles should have a color coding first
        Formatting f = Formatting.byCode(s.charAt(1));
        int color = tcm.getColor("Tooltip.textColor.rarityItem." + f.getName(), f.getColorValue());
        return "§§" + color + "§§" + s.substring(2);
    }

}

