package com.franzzle.pimpedpixel.walkcycle.demo.animation;

import com.franzzle.pimpedpixel.walkcycle.demo.PathBuilder;

class CharacterPathUtil {
    public static PathBuilder initPathBuilder(String characterName) {
        PathBuilder pathBuilder = new PathBuilder();

        pathBuilder.append("characters");
        pathBuilder.append(String.format("%s%s.txt", characterName, "Animations"));

        return pathBuilder;
    }
}
