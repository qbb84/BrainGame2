package me.qbb84.braingame2.Test;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public enum Rotation {

    _90_DEGREES_RIGHT,
    _90_DEGREES_LEFT,
    _180_DEGREES;


    @Getter
    private int degrees;
    @Getter
    private static int listSize;

    public static <T> List<T> rotateList(List<T> originalList, me.qbb84.braingame2.Test.Rotation rotation) {
        int size = originalList.size();
        int steps = rotation.getDegrees();
        int normalizedSteps = (steps % size + size) % size;

        List<T> rotated = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            rotated.add(originalList.get((i + normalizedSteps) % size));
        }

        return rotated;
    }


    public static void setListSize(int size) {
        listSize = size;

        _90_DEGREES_RIGHT.degrees = size - 1;
        _90_DEGREES_LEFT.degrees = 1;
        _180_DEGREES.degrees = size / 2;
    }


}
