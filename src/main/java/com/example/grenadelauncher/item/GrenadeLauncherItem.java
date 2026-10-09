package com.example.grenadelauncher.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import com.example.grenadelauncher.entity.GrenadeEntity;
import com.example.grenadelauncher.GrenadeLauncherMod;

public class GrenadeLauncherItem extends Item {
    public GrenadeLauncherItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.getAbilities().creativeMode || itemStack.getDamage() < itemStack.getMaxDamage()) {
            if (!world.isClient) {
                // Create and shoot the grenade entity
                GrenadeEntity grenadeEntity = new GrenadeEntity(world, user);
                grenadeEntity.setItem(itemStack);
                grenadeEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
                world.spawnEntity(grenadeEntity);

                // Play shooting sound
                world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        SoundEvents.ENTITY_ARROW_SHOOT,
                        SoundCategory.PLAYERS,
                        1.0f,
                        1.0f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
                );
            }

            // Increment damage/use count if not in creative mode
            if (!user.getAbilities().creativeMode) {
                itemStack.damage(1, user, LivingEntity.getSlotForHand(hand));
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(itemStack, world.isClient());
        }
        return TypedActionResult.fail(itemStack, world.isClient());
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
}