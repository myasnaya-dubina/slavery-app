package ru.tzkt.slavery.utils;

public class MathUtils {

    public static int lerp(int a, int b, float progress) {
        return (int) (a + (b - a) * progress / 100);
    }

    // для работы с процентами
    public static float lerp(float a, float b, float progress) {
        return a + (b - a) * progress / 100;
    }

    public static double logMonth(int monthlySavings, int currentPrice, double mortgagePercent) {
        return Math.log(monthlySavings / (monthlySavings - currentPrice * mortgagePercent / 1200)) / Math.log(1 + mortgagePercent / 1200);
    }
}