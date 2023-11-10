package com.franzzle.pimpedpixel.walkcycle.demo.animation;

public enum WalkingDirection {
    UP("up", false),
    DOWN("down", false),
    LEFT("sideways", true),
    RIGHT("sideways", false);
    private String type;
    private boolean flipped;

    WalkingDirection(String type, boolean flipped) {
        this.type = type;
        this.flipped = flipped;
    }

    public String getType() {
        return type;
    }

    public boolean isFlipped() {
        return flipped;
    }
}
