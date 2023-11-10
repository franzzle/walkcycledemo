package com.franzzle.pimpedpixel.walkcycle.demo.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class AnimationParser {
    public Array<Sprite> initAnimation(String name, String animName, TextureAtlas atlas, boolean flip) {
        Array<Sprite> sprites = new Array<>(atlas.getRegions().size);
        for (TextureAtlas.AtlasRegion atlasRegion : atlas.getRegions()) {
            final String regionName = atlasRegion.name;
            final String regionNameStripped = regionName.split("-")[1];
            if (regionName.contains(name) && regionNameStripped.endsWith(animName)) {
                final Sprite sprite = atlas.createSprite(regionName);
                sprite.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                if (flip) {
                    sprite.flip(true, false);
                }
                sprites.add(sprite);
            }
        }
        return sprites;
    }
}
