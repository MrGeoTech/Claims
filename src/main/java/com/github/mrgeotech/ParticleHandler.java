package com.github.mrgeotech;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class ParticleHandler {

    private static final List<Shape> shapes = new ArrayList<>();

    public static void removeAllShapes() {
        shapes.forEach(Shape::stop);
        shapes.clear();
    }

    public static void createLine(int x, int y, World world) {
        Runnable runnable = () -> world.spawnParticle(Particle.BLOCK_DUST, x, -64, y, 384, 0, 1, 0,
                new Particle.DustOptions(Color.RED, 1));

        Shape shape = new Shape(Type.LINE, runnable, x, y, new IntegerContainer(0));

        shapes.add(shape);
        shape.run();
    }

    enum Type {
        LINE
    }

}
