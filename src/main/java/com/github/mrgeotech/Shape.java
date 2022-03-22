package com.github.mrgeotech;

import org.bukkit.Bukkit;

public record Shape(ParticleHandler.Type type, Runnable runnable, int x, int y, IntegerContainer taskID) {

    public void run() {
        taskID.integer = Bukkit.getScheduler()
                .runTaskTimerAsynchronously(Claims.getInstance(), runnable, 0, 20).getTaskId();
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID.integer);
    }

}
