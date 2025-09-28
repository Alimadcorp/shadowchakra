package co.alimad.client;

import net.fabricmc.api.ClientModInitializer;

public class shadowchakrajutsuClient implements ClientModInitializer {
    public static final String MOD_ID = "shadowchakrajutsu";
    @Override
    public void onInitializeClient() {
        ModItems.initialize();
        shadowchakraEffects.registerTickHandler();
    }
}
