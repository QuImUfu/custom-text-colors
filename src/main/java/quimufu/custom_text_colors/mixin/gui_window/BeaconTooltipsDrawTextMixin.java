package quimufu.custom_text_colors.mixin.gui_window;

import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import quimufu.custom_text_colors.CustomTextColors;
import quimufu.custom_text_colors.NamespacedTextColorsRegistry;
import quimufu.custom_text_colors.TextColorsResourceManager;


@Mixin(targets = {"net/minecraft/client/gui/screen/ingame/BeaconScreen$CancelButtonWidget",
        "net/minecraft/client/gui/screen/ingame/BeaconScreen$DoneButtonWidget",
        "net/minecraft/client/gui/screen/ingame/BeaconScreen$EffectButtonWidget"})
public class BeaconTooltipsDrawTextMixin {
    private final NamespacedTextColorsRegistry tcm = TextColorsResourceManager.getInstance().getNamespacedTextColorsRegistry(CustomTextColors.MOD_NAME);

    @Redirect(method = "renderToolTip(II)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/BeaconScreen;renderTooltip(Ljava/lang/String;II)V"))
    private void drawTooltipChangeColor(BeaconScreen beaconScreen, String text, int x, int y) {
        int color = tcm.getColor("Tooltip.textColor.beacon", -1);
        String res = "§§" + color + "§§" + text;
        beaconScreen.renderTooltip(res, x, y);
    }

}

