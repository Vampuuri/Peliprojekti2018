package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Koivulahti on 20.4.2018.
 */

public class SaveFileSelectScreen implements Screen {
    private Rikollisentunnistus game;
    private OrthographicCamera camera;
    Skin mySkin;
    private Stage stage;
    private float row_height;
    private float col_width;
    private float height;
    private float width;
    TextButton profile1Button;
    TextButton profile2Button;
    TextButton profile3Button;
    int profileUsed;
    GameData gameData;

    public SaveFileSelectScreen(Rikollisentunnistus g) {
        game = g;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));

        row_height = camera.viewportHeight / 12;
        col_width = camera.viewportWidth / 12;
        width = camera.viewportWidth;
        height = camera.viewportHeight;

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        profile1Button();
        profile2Button();
        profile3Button();
        buttonBack();
    }

    public void profile1Button() {
        profile1Button = new TextButton("Player 1", mySkin);
        profile1Button.setSize(col_width*3, row_height*1.5f);
        profile1Button.setPosition(col_width*0.75f, height/2 - profile1Button.getHeight()/2);
        profile1Button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(game.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.gameData.setProfileUsed(1);
                game.setCriminalScreen();
            }

        });

        stage.addActor(profile1Button);
    }

    public void profile2Button() {
        profile2Button = new TextButton("Player 2",mySkin);
        profile2Button.setSize(col_width*3,row_height*1.5f);
        profile2Button.setPosition(col_width*4.5f,height/2 - profile2Button.getHeight()/2);
        profile2Button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(game.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.gameData.setProfileUsed(2);
                game.setCriminalScreen();
            }

        });

        stage.addActor(profile2Button);
    }

    public void profile3Button() {
        profile3Button = new TextButton("Player 3",mySkin);
        profile3Button.setSize(col_width*3,row_height*1.5f);
        profile3Button.setPosition(col_width*8.25f,height/2 - profile3Button.getHeight()/2);
        profile3Button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(game.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.gameData.setProfileUsed(3);
                game.setCriminalScreen();
            }

        });

        stage.addActor(profile3Button);
    }

    public void buttonBack() {
        TextButton back = new TextButton("Back",mySkin,"small");
        back.setSize(col_width*2,row_height*2);
        back.setPosition(0,height - back.getHeight());
        back.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundManager.playButtonPushSound(game.soundEffectsOn);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("TAG", "back");
                game.setMainScreen();
            }

        });

        stage.addActor(back);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(70/255f,130/255f,180/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {
        stage.dispose();
        mySkin.dispose();
    }
}