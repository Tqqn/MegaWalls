package dev.tqqn.megawalls.common.classes.levels;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public abstract class UpgradeValue<T> {

    private final List<T> values = new ArrayList<>(5);

    public void addValue(int place, T value) {
        values.add(place, value);
    }

    public void removeValue(int place) {
        values.remove(place);
    }

    public T getValue(int place) {
        return values.get(place);
    }

    public List<T> getValues() {
        return ImmutableList.copyOf(values);
    }
}
