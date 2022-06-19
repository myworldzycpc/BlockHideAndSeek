package io.github.myworldzycpc.block_has.func;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FuncAlgorithms {

    public static int nextId = 0;

    public static int getNextId() {
        return nextId++;
    }

    public static int getValueWithDefault(String text, int _default, int min, int max) {
        int num;
        try {
            num = Integer.parseInt(text);
        } catch (Exception e) {
            num = _default;
        }
        if (num < min) {
            num = min;
        } else if (num > max) {
            num = max;
        }
        return num;
    }

    public static double getValueWithDefault(String text, double _default, double min, double max) {
        double num;
        try {
            num = Double.parseDouble(text);
        } catch (Exception e) {
            num = _default;
        }
        if (num < min) {
            num = min;
        } else if (num > max) {
            num = max;
        }
        return num;
    }

    public static <T> List<T> extract(int count, List<T> list) {
        List<Integer> givenList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            givenList.add(i);
        }
        Collections.shuffle(givenList);

        List<Integer> randomSeries = givenList.subList(0, count);
        List<T> randomObjects = new ArrayList<T>();
        for (Integer i : randomSeries) {
            randomObjects.add(list.get(i));
        }
        return randomObjects;
    }

    public static double roundTo(double value, int digit) {
        return Math.round(value * Math.pow(10, digit)) / Math.pow(10, digit);
    }

    public static double average(List<Double> list) {
        double sum = 0;
        for (double i : list) {
            sum += i;
        }
        return sum / list.size();
    }

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format("%d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

}
