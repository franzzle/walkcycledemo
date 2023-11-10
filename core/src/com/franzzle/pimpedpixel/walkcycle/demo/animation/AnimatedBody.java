package com.franzzle.pimpedpixel.walkcycle.demo.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.franzzle.pimpedpixel.walkcycle.demo.PathBuilder;

import java.util.*;

public class AnimatedBody extends Actor {
    private float stateTime = 0;
    private Animation<Sprite> currentWalkAnimation;
    private Animation<Sprite> currentNonWalkAnimation;
    private final Map<String, Animation<Sprite>> walkAnimsMap = new HashMap<>();
    private final Map<String, Animation<Sprite>> nonWalkAnimsMap = new HashMap<>();
    private final UUID uuid;
    private final String characterName;
    private boolean active;
    private int zsort = -1;

    public AnimatedBody(String characterName) {
        this.characterName = characterName;
        this.uuid = UUID.randomUUID();
        AnimationParser animationParser = new AnimationParser();
        this.setActive(false);

        final TextureAtlas characterAtlas = getTextureAtlas(characterName);

        for (Texture texture : characterAtlas.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }

        final Array<Sprite> walkRightSprites = animationParser.initAnimation(characterName, "walksideways", characterAtlas, false);
        final Array<Sprite> walkLeftSprites = animationParser.initAnimation(characterName, "walksideways", characterAtlas, true);

        final Array<Sprite> walkUpSprites = animationParser.initAnimation(characterName, "walkup", characterAtlas, false);
        final Array<Sprite> walkDownSprites = animationParser.initAnimation(characterName, "walkdown", characterAtlas, false);

        final Array<Sprite> walkDownRestSprites = animationParser.initAnimation(characterName, "walkdownrest", characterAtlas, false);
        final Array<Sprite> walkUpRestSprites = animationParser.initAnimation(characterName, "walkuprest", characterAtlas, false);

        final Array<Sprite> walkRightRestSprites = animationParser.initAnimation(characterName, "walksidewaysrest", characterAtlas, false);
        final Array<Sprite> walkLeftRestSprites = animationParser.initAnimation(characterName, "walksidewaysrest", characterAtlas, true);

        // Since there's only one row, we only get the first array
        final float frameDuration = 0.1f;
        Animation<Sprite> heroWalkRightAnimation = new Animation<>(frameDuration, walkRightSprites);
        Animation<Sprite> heroWalkLeftAnimation = new Animation<>(frameDuration, walkLeftSprites);

        Animation<Sprite> heroWalkUpAnimation = new Animation<>(frameDuration, walkUpSprites);
        Animation<Sprite> heroWalkDownAnimation = new Animation<>(frameDuration, walkDownSprites);

        Animation<Sprite> heroWalkRightRestAnimation = new Animation<>(10, walkRightRestSprites);
        Animation<Sprite> heroWalkLeftRestAnimation = new Animation<>(10, walkLeftRestSprites);

        Animation<Sprite> heroWalkUpRestAnimation = new Animation<>(10, walkUpRestSprites);
        Animation<Sprite> heroWalkDownRestAnimation = new Animation<>(10, walkDownRestSprites);

        // Set it to ping pong mode so our hero looks like he's walking
        heroWalkRightAnimation.setPlayMode(Animation.PlayMode.LOOP);
        heroWalkLeftAnimation.setPlayMode(Animation.PlayMode.LOOP);

        heroWalkUpAnimation.setPlayMode(Animation.PlayMode.LOOP);
        heroWalkDownAnimation.setPlayMode(Animation.PlayMode.LOOP);

        heroWalkRightRestAnimation.setPlayMode(Animation.PlayMode.LOOP);
        heroWalkLeftRestAnimation.setPlayMode(Animation.PlayMode.LOOP);

        heroWalkUpRestAnimation.setPlayMode(Animation.PlayMode.LOOP);
        heroWalkDownRestAnimation.setPlayMode(Animation.PlayMode.LOOP);

        walkAnimsMap.put(WalkingDirection.RIGHT.getType(), heroWalkRightAnimation);
        walkAnimsMap.put(WalkingDirection.LEFT.getType(), heroWalkLeftAnimation);
        walkAnimsMap.put(WalkingDirection.DOWN.getType(), heroWalkDownAnimation);
        walkAnimsMap.put(WalkingDirection.UP.getType(), heroWalkUpAnimation);

        setNonWalkableAnims(characterName, animationParser, characterAtlas);

        currentWalkAnimation = heroWalkRightAnimation;
        currentNonWalkAnimation = null;

        this.setWidth(heroWalkDownRestAnimation.getKeyFrame(0).getRegionWidth());
        this.setHeight(heroWalkDownRestAnimation.getKeyFrame(0).getRegionHeight());
        this.setName("body");
    }


    private void setNonWalkableAnims(String characterName, AnimationParser animationParser, TextureAtlas characterAtlas) {
        List<String> regionNames = new ArrayList<>();
        for (TextureAtlas.AtlasRegion region : characterAtlas.getRegions()) {
            if (!region.name.contains("walk") && !region.name.contains("lipsync")) {
                regionNames.add(region.name);
            }
        }

        for (String regionName : regionNames) {
            String[] nameParts = regionName.split("-");

            if (nameParts.length >= 3) {
                String animationPrefix = nameParts[1]; // Get the animation prefix

                // Animation prefix not found in the map, create a new animation
                Array<Sprite> sprites = animationParser.initAnimation(characterName, animationPrefix, characterAtlas, false);
                Animation<Sprite> animation = new Animation<>(1f / sprites.size, sprites);
                animation.setPlayMode(Animation.PlayMode.NORMAL);
                nonWalkAnimsMap.put(animationPrefix, animation);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Update the animation time for non-walk animations
        stateTime += delta;

        if (currentNonWalkAnimation != null && currentNonWalkAnimation.isAnimationFinished(stateTime) && !nonWalkAnimsMap.isEmpty()) {
            if (nonWalkAnimsMap.containsKey("idle")) {
                currentNonWalkAnimation = nonWalkAnimsMap.get("idle");
            } else {
                String first = nonWalkAnimsMap.keySet().iterator().next();
                currentNonWalkAnimation = nonWalkAnimsMap.get(first);
            }
            stateTime = 0;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isActive()) {
            // Check if the animation is not null and draw it
            if (getCurrentAnimation() != null) {
                Sprite currentFrame = getCurrentAnimation().getKeyFrame(stateTime, true);
                currentFrame.setPosition(getX(), getY());
                currentFrame.draw(batch);
            }
        }
    }

    public Animation<Sprite> getAnimation(String name) {
        return nonWalkAnimsMap.get(name);
    }

    public void animateHeroWalkingInDirection(WalkingDirection walkingDirection) {
        Animation<Sprite> spriteAnimation = this.walkAnimsMap.get(walkingDirection.getType());
        if (spriteAnimation != null) {
            if(walkingDirection == WalkingDirection.DOWN || walkingDirection == WalkingDirection.RIGHT){
            }
            this.currentWalkAnimation = spriteAnimation;
        }
    }

    public void setAnimation(String name) {
        if (nonWalkAnimsMap.containsKey(name)) {
            currentNonWalkAnimation = nonWalkAnimsMap.get(name);
            Sprite currentFrame = getCurrentAnimation().getKeyFrame(stateTime, true);
            currentFrame.setPosition(getX() - getWidth() / 2, getY());
            stateTime = 0;
        }
    }

    public void setWalkAnimation(String name) {

    }


    private TextureAtlas getTextureAtlas(String characterName) {
        PathBuilder pathBuilder = CharacterPathUtil.initPathBuilder(characterName);
        if (pathBuilder.pathExists()) {
            return new TextureAtlas(pathBuilder.build());
        }
        throw new GdxRuntimeException(String.format("Character with name %s is not present", characterName));
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Animation<Sprite> getCurrentAnimation() {
        if (currentNonWalkAnimation != null) {
            return currentNonWalkAnimation;
        } else {
            return currentWalkAnimation;
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "AnimatedBody{" +
                "uuid=" + uuid +
                ", characterName='" + characterName + '\'' +
                ", Zsort='" + getZsort() + "'" +
                '}';
    }

    public void setZsort(int zsort) {
        this.zsort = zsort;
    }

    public int getZsort() {
        return zsort;
    }
}
