package co.alimad;

import net.fabricmc.api.ModInitializer;

public class shadowchakrajutsu implements ModInitializer {
    public static final String MOD_ID = "shadowchakrajutsu";

    @Override
    public void onInitialize() {
        ModItems.initialize();
        shadowchakraEffects.registerTickHandler();
    }
}
