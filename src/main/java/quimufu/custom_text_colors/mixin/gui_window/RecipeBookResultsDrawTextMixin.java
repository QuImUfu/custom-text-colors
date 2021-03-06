package quimufu.custom_text_colors.mixin.gui_window;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.recipebook.AnimatedResultButton;
import net.minecraft.client.gui.screen.recipebook.RecipeBookResults;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.container.CraftingContainer;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;

import java.util.List;


@Mixin(RecipeBookResults.class)
public abstract class RecipeBookResultsDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);
    @Shadow
    private RecipeBook recipeBook;
    @Shadow
    private AnimatedResultButton hoveredResultButton;

    @Shadow
    public abstract MinecraftClient getMinecraftClient();

    @Shadow
    private RecipeResultCollection resultCollection;

    @Redirect(method = "draw",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFI)I"))
    private int drawTextChangeColor(TextRenderer textRenderer, String text, float x, float y, int color) {
        if (color == -1) {
            return textRenderer.draw(text, x, y, tcm.getColor("RecipeBookResults.textColor.page", color));
        } else {
            CustomTextColors.log(Level.ERROR, "unknown value: " + color);
            CustomTextColors.log(Level.ERROR, "falling back to default. " + this.getClass().getSimpleName() + " !report this to CustomTextColors!");
        }
        return textRenderer.draw(text, x, y, color);
    }

    @Redirect(method = "drawTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;renderTooltip(Ljava/util/List;II)V"))
    private void drawTextItemTooltipChangeColor(Screen screen, List<String> text, int x, int y) {
        //item tooltip
        if (text == null) {
            screen.renderTooltip(text, x, y);
            return;
        }

        boolean bl = hoveredResultButton.getResultCollection().getResults(recipeBook.isFilteringCraftable((CraftingContainer) getMinecraftClient().player.container)).size() > 1;
        for (int i = 0; i < text.size(); i++) {
            String s = text.get(i);
            if (i == 0) {
                text.set(i, reformatTitle(s));
            } else if (i == text.size() - 1 && bl) {
                text.set(i, reformatMoreRecipies(s));
            } else {
                text.set(i, reformatOther(s));
            }
        }
        screen.renderTooltip(text, x, y);
    }

    private String reformatMoreRecipies(String s) {
        int color = tcm.getColor("Tooltip.textColor.moreRecipes", -1);
        return "§§" + color + "§§" + s;
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

