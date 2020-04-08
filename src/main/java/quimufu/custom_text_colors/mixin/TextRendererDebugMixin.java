package quimufu.custom_text_colors.mixin;

import net.minecraft.client.gui.screen.world.WorldListWidget;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldListWidget.Entry.class)
public class TextRendererDebugMixin {
    //@Shadow private AdvancementTab selectedTab;
    private int counter = 0;
/*
    @Redirect(method = "recreate",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;getLevelStorage()Lnet/minecraft/world/level/storage/LevelStorage;"))
    LevelStorage getLevelStorageReturnNull(MinecraftClient minecraftClient){
        return null;
    }*/

    /*
        @Inject(method = "drawWithShadow", at = @At("HEAD"))
        void drawWithShadowMixed(String text, float x, float y, int color, CallbackInfoReturnable<Integer> cir) {
            if (counter == 0) {
                CustomTextColors.log(Level.INFO, "result color is:" + color);
            }
            counter++;
            counter = counter % 20000;
        }*/
    /*
    @Inject(method = "drawAdvancementTree", at = @At("HEAD"))
    void drawAdvancementTree(int mouseX, int mouseY, int x, int y, CallbackInfo ci) {
        this.selectedTab=null;
    }*/
}
