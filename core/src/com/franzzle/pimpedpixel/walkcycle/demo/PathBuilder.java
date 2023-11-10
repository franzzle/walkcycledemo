package com.franzzle.pimpedpixel.walkcycle.demo;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

public class PathBuilder {
    private final List<String> segments = new ArrayList<>();

    public PathBuilder() {
    }

    public PathBuilder(PathBuilder other) {
        this.segments.addAll(other.segments);
    }

    public PathBuilder prepend(String segment) {
        segments.add(0, segment);
        return this;
    }

    public PathBuilder append(String segment) {
        segments.add(segment);
        return this;
    }

    public String build() {
        return String.join("/", segments).replaceAll("^/+", "");
    }

    public boolean pathExists() {
        String fullPath = this.build();
        return Gdx.files.internal(fullPath).exists();
    }
}