package com.github.mrgeotech;

import org.bukkit.Bukkit;

public record Shape(ParticleHandler.Type type, Runnable runnable, double x, double y, IntegerContainer taskID) {

    public void run() {
        taskID.integer = Bukkit.getScheduler()
                .runTaskTimerAsynchronously(Claims.getInstance(), runnable, 0, 2).getTaskId();
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID.integer);
    }

}
