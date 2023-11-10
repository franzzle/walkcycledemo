package com.franzzle.pimpedpixel.walkcycle.demo.character;


import com.franzzle.pimpedpixel.walkcycle.demo.animation.AnimatedBody;

public class AnimatedCharacter {
    private final String characterName;
    private final AnimatedBody animatedBody;

    public AnimatedCharacter(String characterName) {
        this.characterName = characterName;
        this.animatedBody = new AnimatedBody(characterName);
    }

    public String getCharacterName() {
        return characterName;
    }

    public AnimatedBody getAnimatedBody() {
        return animatedBody;
    }
}
