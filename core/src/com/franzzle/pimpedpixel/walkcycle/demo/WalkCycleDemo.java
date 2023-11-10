package com.franzzle.pimpedpixel.walkcycle.demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.franzzle.pimpedpixel.walkcycle.demo.animation.AnimatedBody;
import com.franzzle.pimpedpixel.walkcycle.demo.animation.WalkAnimationAction;
import com.franzzle.pimpedpixel.walkcycle.demo.animation.WalkingDirection;
import com.franzzle.pimpedpixel.walkcycle.demo.character.AnimatedCharacter;

import static com.franzzle.pimpedpixel.walkcycle.demo.animation.WalkingDirection.*;

public class WalkCycleDemo extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;

	private AnimatedCharacter animatedCharacter;

	private Stage stage;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		animatedCharacter = new AnimatedCharacter("johncleese");
		animatedCharacter.getAnimatedBody().setActive(true);

		MoveByAction moveToTheRightAction = new MoveByAction();
		moveToTheRightAction.setAmount(800, 0);
		moveToTheRightAction.setDuration(3);

		MoveByAction moveDownAction = new MoveByAction();
		moveDownAction.setAmount(0, -400);
		moveDownAction.setDuration(3);

		MoveByAction moveLeftAction = new MoveByAction();
		moveLeftAction.setAmount(-800, 0);
		moveLeftAction.setDuration(3);

		MoveByAction moveUpAction = new MoveByAction();
		moveUpAction.setAmount(0, 400);
		moveUpAction.setDuration(3);

		SequenceAction walkAroundTheRoom = new SequenceAction();
		AnimatedBody animatedBody = animatedCharacter.getAnimatedBody();
		walkAroundTheRoom.addAction(moveToTheRightAction);
		walkAroundTheRoom.addAction(WalkAnimationAction.create(animatedBody, DOWN));
		walkAroundTheRoom.addAction(moveDownAction);
		walkAroundTheRoom.addAction(WalkAnimationAction.create(animatedBody, LEFT));
		walkAroundTheRoom.addAction(moveLeftAction);
		walkAroundTheRoom.addAction(WalkAnimationAction.create(animatedBody, UP));
		walkAroundTheRoom.addAction(moveUpAction);
		walkAroundTheRoom.addAction(WalkAnimationAction.create(animatedBody, RIGHT));

		RepeatAction forever = Actions.forever(walkAroundTheRoom);
		animatedCharacter.getAnimatedBody().addAction(forever);

		stage = new Stage();
		final Viewport viewportLoading = new FitViewport(
				1024,
				768,
				new OrthographicCamera());
		this.stage.setViewport(viewportLoading);

		animatedCharacter.getAnimatedBody().setX(0.1f * Gdx.graphics.getWidth());
		animatedCharacter.getAnimatedBody().setY(0.8f * Gdx.graphics.getHeight());

		this.stage.addActor(animatedCharacter.getAnimatedBody());
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();

		animatedCharacter.getAnimatedBody().act(Gdx.graphics.getDeltaTime());
		stage.getBatch().begin();
		animatedCharacter.getAnimatedBody().draw(stage.getBatch(), 1.0f);
		stage.getBatch().end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
