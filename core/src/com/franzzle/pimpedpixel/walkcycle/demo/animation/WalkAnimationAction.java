package com.franzzle.pimpedpixel.walkcycle.demo.animation;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

public class WalkAnimationAction extends RunnableAction {
    private final AnimatedBody animatedBody;
    private final WalkingDirection walkingDirection;

    private WalkAnimationAction(AnimatedBody animatedBody, WalkingDirection walkingDirection) {
        this.animatedBody = animatedBody;
        this.walkingDirection = walkingDirection;
    }

    @Override
    public void run() {
        animatedBody.animateHeroWalkingInDirection(walkingDirection);
    }

    // Factory method
    public static WalkAnimationAction create(AnimatedBody animatedBody, WalkingDirection walkingDirection) {
        return new WalkAnimationAction(animatedBody, walkingDirection);
    }
}
