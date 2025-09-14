package net.no.tnt.griefing.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.no.tnt.griefing.NoTNTGriefing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin extends Entity {
    public TntEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;"), index = 5)
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