package net.dappls.dapplelib.client.shader;

public class Timekeeper {
    private static long startTimeNanos = -1;
    public static float Time;

    public static void updateTime() {
        if (startTimeNanos == -1) startTimeNanos = System.nanoTime();
        float time = (float) ((System.nanoTime() - startTimeNanos) / 1_000_000_000.0);
        Time = time;
    }
}
