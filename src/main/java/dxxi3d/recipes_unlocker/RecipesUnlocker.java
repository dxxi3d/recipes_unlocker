package dxxi3d.recipes_unlocker;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecipesUnlocker implements ModInitializer {

    private static final String MOD_NAME = "Recipes Unlocker";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        LOGGER.info("[{}] mod loaded!", MOD_NAME);
    }
}
