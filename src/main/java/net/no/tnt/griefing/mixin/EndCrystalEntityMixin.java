package net.no.tnt.griefing.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.no.tnt.griefing.NoTNTGriefing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity{
    public EndCrystalEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @ModifyArg(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)V"), index = 8)
    private World.ExplosionSourceType modifyExplosionSourceType(World.ExplosionSourceType destructionType) {
        if(this.getWorld() instanceof ServerWorld) {
            GameRules gameRules = this.getWorld().getServer().getGameRules();
            if (!gameRules.getBoolean(NoTNTGriefing.END_CRYSTAL_GRIEFING)) {
                return World.ExplosionSourceType.NONE;
            }
        }
        return World.ExplosionSourceType.BLOCK;
    }

}
