package com.github.mrgeotech;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ParticleHandler {

    private static final List<Shape> shapes = new ArrayList<>();

    public static void removeAllShapes() {
        shapes.forEach(Shape::stop);
        shapes.clear();
    }

    public static void showLine(int x, int y, Player player) {
        Runnable runnable = () ->  {
            for (int y1 = 0; y1 < 256; y1 += 2) {
                player.spawnParticle(Particle.REDSTONE, 0, y1, 0, 1, new Particle.DustOptions(Color.RED, 1));
            }
        };

        Shape shape = new Shape(Type.LINE, runnable, x, y, new IntegerContainer(0));

        shapes.add(shape);
        shape.run();
    }

    public static void showClaim(Claim claim) {
        Runnable runnable = () -> {
            int side1 = claim.getX1() - claim.getX2();
            int side2 = claim.getY1() - claim.getY2();
            int max1 = Math.max(side1, 0);
            if (max1 == side1) {
                for (int x = claim.getX2() + 2; x < claim.getX1() - 1; x += 2) {
                    Color color = Color.LIME;
                    int verticalDistance = 2;
                    if (x == claim.getX1() || x == claim.getX2()) {
                        color = Color.RED;
                        verticalDistance = 1;
                    }
                    for (int y = 0; y < 256; y += verticalDistance) {
                        claim.getWorld().spawnParticle(Particle.REDSTONE, x, y, claim.getY1(), 1, new Particle.DustOptions(color, 0.5f));
                        claim.getWorld().spawnParticle(Particle.REDSTONE, x, y, claim.getY2(), 1, new Particle.DustOptions(color, 0.5f));
                    }
                }
            }
            if (max1 == 0) {
                for (int x = claim.getX1() + 2; x < claim.getX2() - 1; x += 2) {
                    Color color = Color.LIME;
                    int verticalDistance = 2;
                    if (x == claim.getX1() || x == claim.getX2()) {
                        color = Color.RED;
                        verticalDistance = 1;
                    }
                    for (int y = 0; y < 256; y += verticalDistance) {
                        claim.getWorld().spawnParticle(Particle.REDSTONE, x, y, claim.getY1(), 1, new Particle.DustOptions(color, 0.5f));
                        claim.getWorld().spawnParticle(Particle.REDSTONE, x, y, claim.getY2(), 1, new Particle.DustOptions(color, 0.5f));
                    }
                }
            }
            int max2 = Math.max(side2, 0);
            if (max2 == side2) {
                for (int y = claim.getY2(); y < claim.getY1() + 1; y += 2) {
                    Color color = Color.LIME;
                    int verticalDistance = 2;
                    if (y == claim.getY1() || y == claim.getY2()) {
                        color = Color.RED;
                        verticalDistance = 1;
                    }
                    for (int y1 = 0; y1 < 256; y1 += verticalDistance) {
                        claim.getWorld().spawnParticle(Particle.REDSTONE, claim.getX1(), y1, y, 1, new Particle.DustOptions(color, 0.5f));
                        claim.getWorld().spawnParticle(Particle.REDSTONE, claim.getX2(), y1, y, 1, new Particle.DustOptions(color, 0.5f));
                    }
                }
            }
            if (max2 == 0) {
                for (int y = claim.getY1(); y < claim.getY2() + 1; y += 2) {
                    Color color = Color.LIME;
                    int verticalDistance = 2;
                    if (y == claim.getY1() || y == claim.getY2()) {
                        color = Color.RED;
                        verticalDistance = 1;
                    }
                    for (int y1 = 0; y1 < 256; y1 += verticalDistance) {
                        claim.getWorld().spawnParticle(Particle.REDSTONE, claim.getX1(), y1, y, 1, new Particle.DustOptions(color, 0.5f));
                        claim.getWorld().spawnParticle(Particle.REDSTONE, claim.getX2(), y1, y, 1, new Particle.DustOptions(color, 0.5f));
                    }
                }
            }
        };

        Shape shape = new Shape(Type.CLAIM_BOX, runnable,
                Math.min(claim.getX1(), claim.getX2()) + Math.max(claim.getX1(), claim.getX2()) / 2,
                Math.min(claim.getY1(), claim.getY2()) - Math.max(claim.getY1(), claim.getY2()) / 2,
                new IntegerContainer(0));

        shapes.add(shape);
        shape.run();
    }

    enum Type {
        LINE,
        CLAIM_BOX
    }

}
