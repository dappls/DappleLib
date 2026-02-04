package net.dappls.dapplelib.util;

import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DappleLibParticleUtils {
    public static void addParticleEffects(Entity entity, ParticleEffect particleEffect, int count, ParticleAnchor anchor, ParticleVelocity velocity) {
            for (int i = 0; i < count; i++) {
                double y = entity.getY();
                if (anchor == ParticleAnchor.BODY) {
                    y = entity.getRandomBodyY();
                } else if (anchor == ParticleAnchor.EYES) {
                    y = entity.getEyeY() + MathHelper.nextDouble(entity.getRandom(), -0.1, 0.1);
                } else if (anchor == ParticleAnchor.CHEST) {
                    y += entity.getHeight() * MathHelper.nextDouble(entity.getRandom(), 0.4, 0.8);
                } else if (anchor == ParticleAnchor.FEET) {
                    y += entity.getHeight() * 0.15;
                }
                double velocityX, velocityY, velocityZ;
                if (velocity.randomMultiplier() == 0) {
                    velocityX = velocity.velocity().getX();
                    velocityY = velocity.velocity().getY();
                    velocityZ = velocity.velocity().getZ();
                } else {
                    Vec3d randomized = velocity.velocity().addRandom(entity.getRandom(), (float) velocity.randomMultiplier());
                    velocityX = randomized.getX();
                    velocityY = randomized.getY();
                    velocityZ = randomized.getZ();
                }
                entity.getEntityWorld().addParticleClient(particleEffect, entity.getParticleX(1), y, entity.getParticleZ(1), velocityX, velocityY, velocityZ);
            }
    }
}
