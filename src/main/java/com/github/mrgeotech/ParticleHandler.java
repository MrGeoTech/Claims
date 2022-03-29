package com.github.mrgeotech;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ParticleHandler {

    private static final List<Shape> shapes = new ArrayList<>();

    public static void removeAllShapes() {
        shapes.forEach(Shape::stop);
        shapes.clear();
    }

    public static void showLine(int x, int y, Player player) {
        Runnable runnable = () ->  {
            for (int y1 = 0; y1 < 256; y1 += 2) {
                player.spawnParticle(Particle.REDSTONE, x, y1, y, 1, new Particle.DustOptions(Color.RED, 1));
            }
        };

        Shape shape = new Shape(Type.LINE, runnable, x, y, new IntegerContainer(0));

        shapes.add(shape);
        shape.run();
    }

    public static void hideLine(int x, int y) {
        shapes.remove(null);
        AtomicReference<Shape> temp = new AtomicReference<>(null);
        shapes.forEach(shape -> {
            if (temp.get() == null && shape != null && shape.type() == Type.LINE && shape.x() == x && shape.y() == y) {
                temp.set(shape);
            }
        });
        temp.get().stop();
        shapes.remove(temp.get());
    }

    public static void showClaim(Claim claim) {
        Runnable runnable = () -> {
            int side1 = claim.getX1() - claim.getX2();
            int side2 = claim.getY1() - claim.getY2();
            int max1 = Math.max(side1, 0);
            if (max1 == side1) {
                for (int x = claim.getX2() + 2; x < claim.getX1() - 1; x += 2) {
                    for (int y = 0; y < 256; y += 2) {
                        claim.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, x, y, claim.getY1(), 1);
                        claim.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, x, y, claim.getY2(), 1);
                    }
                }
            }
            if (max1 == 0) {
                for (int x = claim.getX1() + 2; x < claim.getX2() - 1; x += 2) {
                    for (int y = 0; y < 256; y += 2) {
                        claim.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, x, y, claim.getY1(), 1);
                        claim.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, x, y, claim.getY2(), 1);
                    }
                }
            }
            int max2 = Math.max(side2, 0);
            if (max2 == side2) {
                for (int y = claim.getY2() + 2; y < claim.getY1() - 1; y += 2) {
                    for (int y1 = 0; y1 < 256; y1 += 2) {
                        claim.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, claim.getX1(), y1, y, 1);
                        claim.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, claim.getX2(), y1, y, 1);
                    }
                }
            }
            if (max2 == 0) {
                for (int y = claim.getY1() + 2; y < claim.getY2() - 1; y += 2) {
                    for (int y1 = 0; y1 < 256; y1 += 2) {
                        claim.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, claim.getX1(), y1, y, 1);
                        claim.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, claim.getX2(), y1, y, 1);
                    }
                }
            }
            for (int y1 = 0; y1 < 256; y1++) {
                claim.getWorld().spawnParticle(Particle.REDSTONE, claim.getX1(), y1, claim.getY1(), 1, new Particle.DustOptions(Color.RED, 0.5f));
                claim.getWorld().spawnParticle(Particle.REDSTONE, claim.getX2(), y1, claim.getY1(), 1, new Particle.DustOptions(Color.RED, 0.5f));
                claim.getWorld().spawnParticle(Particle.REDSTONE, claim.getX1(), y1, claim.getY2(), 1, new Particle.DustOptions(Color.RED, 0.5f));
                claim.getWorld().spawnParticle(Particle.REDSTONE, claim.getX2(), y1, claim.getY2(), 1, new Particle.DustOptions(Color.RED, 0.5f));
            }
        };

        Shape shape = new Shape(Type.CLAIM_BOX, runnable,
                claim.getX1() + claim.getX2() / 2d,
                claim.getY1() + claim.getY1() / 2d,
                new IntegerContainer(0));

        shapes.add(shape);
        shape.run();
    }

    public static void hideClaim(Claim claim) {
        double x = claim.getX1() + claim.getX2() / 2d;
        double y = claim.getY1() + claim.getY1() / 2d;

        shapes.remove(null);
        Shape temp = shapes.get(0);

        for (Shape shape : shapes) {
            if (shape.type() == Type.CLAIM_BOX && Math.ceil(shape.x()) == Math.ceil(x) && Math.ceil(shape.y()) == Math.ceil(y))
                temp = shape;
        }

        assert temp != null;
        temp.stop();
        shapes.remove(temp);
    }

    enum Type {
        LINE,
        CLAIM_BOX
    }

}
