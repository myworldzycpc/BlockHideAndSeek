package io.github.myworldzycpc.block_has.func;

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


}
