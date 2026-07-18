package com.danklin.playerevolutions.events;

import com.danklin.playerevolutions.PlayerEvolutions;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerEvolutions.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        // Check if the projectile has our custom tag
        if (event.getEntity().getTags().contains("explosive_bolt")) {
            World world = event.getEntity().getEntityWorld();

            if (!world.isRemote) {
                // Create the explosion at the impact point (3.0F is Creeper size)
                world.createExplosion(
                        event.getEntity(),
                        event.getEntity().getPosX(),
                        event.getEntity().getPosY(),
                        event.getEntity().getPosZ(),
                        3.0F,
                        Explosion.Mode.BREAK
                );
                // Destroy the projectile so it doesn't linger
                event.getEntity().remove();
            }
        }
    }
}