package quimufu.custom_text_colors;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CustomTextColors implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "custom_text_colors";
    public static final String MOD_NAME = "custom-text-colors";
    private static final Gson GSON = (new GsonBuilder()).setLenient().setPrettyPrinting().create();

    @Override
    public void onInitializeClient() {
        log(Level.INFO, "Initializing test CustomTextColors mod");
        TextColorsResourceManager tcm = TextColorsResourceManager.getInstance();
        AutoConfig.register(CustomTextColorsConfig.class, GsonConfigSerializer::new);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(tcm);
        tcm.setDefault(MOD_NAME, new CustomTextColorsDefaults());


    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

}