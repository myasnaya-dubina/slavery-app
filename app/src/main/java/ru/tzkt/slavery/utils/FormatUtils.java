package ru.tzkt.slavery.utils;

public class FormatUtils {

    public static String formatPercent(float input) {
        return input + "%";
    }

    public static String formatMillions(int input) {
        float inputFormat = (float) input / 1_000_000;
        return (String.format("%.2fm", inputFormat));
    }

    public static String formatThousands(int input) {
        float inputFormat = (float) input / 1_000;
        return (String.format("%.2fk", inputFormat));
    }

    public static String formatUnits(int input) {
        return String.format("%dp", input);
    }

    public static String formatAmount(int input) {
        if (input < 1_000_000) {
            if (input < 1_000) {
                return formatUnits(input);
            } else {
                return formatThousands(input);
            }
        } else {
            return formatMillions(input);
        }
    }

    public static String formatAmount(float input) {
        return formatPercent(input);
    }

}

