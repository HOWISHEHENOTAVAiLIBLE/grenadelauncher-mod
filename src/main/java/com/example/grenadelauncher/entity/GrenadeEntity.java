package com.example.grenadelauncher.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import com.example.grenadelauncher.GrenadeLauncherMod;

public class GrenadeEntity extends PersistentProjectileEntity {
    private static final TrackedData<Boolean> BOUNCE;
    private int fuse;

    static {
        BOUNCE = DataTracker.registerData(GrenadeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    public GrenadeEntity(EntityType<? extends GrenadeEntity> entityType, World world) {
        super(entityType, world);
        this.fuse = 80; // 4 seconds fuse
    }

    public GrenadeEntity(World world, LivingEntity owner) {
        this(GrenadeLauncherMod.GRENADE_ENTITY_TYPE, world);
        this.setOwner(owner);
        // Set initial position to be slightly in front of the player
        Vec3d vec3d = owner.getRotationVec(1.0f);
        this.setPosition(owner.getX() + vec3d.x * 0.5,
                        owner.getEyeY() - 0.1,
                        owner.getZ() + vec3d.z * 0.5);
        this.setVelocity(vec3d.x * 1.5, vec3d.y * 1.5, vec3d.z * 1.5, 1.0f, 1.0f);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BOUNCE, false);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient) {
            this.explode();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getWorld().isClient) {
            this.explode();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            // Add some particle effects while flying
            for (int i = 0; i < 2; ++i) {
                this.getWorld().addParticle(
                        ParticleTypes.SMOKE,
                        this.getX() - this.getVelocity().x * 0.25,
                        this.getY() - this.getVelocity().y * 0.25,
                        this.getZ() - this.getVelocity().z * 0.25,
                        0.0, 0.0, 0.0
                );
            }
        } else {
            // Decrease fuse and explode when it reaches 0
            --this.fuse;
            if (this.fuse <= 0) {
                this.explode();
            }
        }
    }

    private void explode() {
        if (!this.getWorld().isClient) {
            // Create explosion
            this.getWorld().createExplosion(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    3.0f,
                    false,
                    World.ExplosionSourceType.MOB
            );

            // Play explosion sound
            this.getWorld().playSound(
                    null,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE,
                    this.getSoundCategory(),
                    4.0f,
                    (1.0f + (this.getWorld().random.nextFloat() - this.getWorld().random.nextFloat()) * 0.2f) * 0.7f
            );

            // Remove the grenade entity
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return GrenadeLauncherMod.GRENADE_LAUNCHER;
    }
}