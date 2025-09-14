package net.no.tnt.griefing.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.no.tnt.griefing.NoTNTGriefing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TntMinecartEntity.class)
public abstract class TntMinecartEntityMixin extends Entity {
    public TntMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(method = "explode(Lnet/minecraft/entity/damage/DamageSource;D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;"), index = 8)
    private World.ExplosionSourceType modifyExplosionSourceType(World.ExplosionSourceType destructionType) {
        // Check if we're in the Nether dimension
        RegistryKey<World> dimension = this.getWorld().getRegistryKey();
        boolean isInNether = dimension == World.NETHER;
        
        // Get the game rule value
        GameRules gameRules = this.getWorld().getGameRules();
        boolean tntGriefingEnabled = gameRules.getBoolean(NoTNTGriefing.TNT_GRIEFING);
        
        // Allow TNT destruction only if:
        // 1. The game rule allows TNT griefing AND
        // 2. We're in the Nether dimension
        if (tntGriefingEnabled && isInNether) {
            return World.ExplosionSourceType.TNT;
        }
        
        return World.ExplosionSourceType.NONE;
    }
}