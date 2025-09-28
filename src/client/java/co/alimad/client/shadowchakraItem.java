package co.alimad.client;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class shadowchakraItem extends Item {
    public shadowchakraItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);

        if (user instanceof ServerPlayerEntity player) {
            if (!world.isClient) {
                shadowchakraEffects.doSomething(world, player);
            }
        }
        return result;
    }
}

