package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * @author Essi Supponen
 * @version 1.6
 * @since 2018-05-06
 *
 * Plays the cutscenes of the game and calls the TutorialScreen.
 */

public class CutsceneScreen implements Screen {
    Rikollisentunnistus host;
    Stage stage;
    OrthographicCamera camera;
    float width;
    float height;

    Label text;
    Skin mySkin;
    boolean soundEffectsOn;

    Window pauseWindow;
    boolean paused;

    Image assistantImage1;
    Image assistantImage2;
    Image assistantImage3;
    Image chiefImage1;
    Image chiefImage2;
    Image chiefImage3;
    Image dialogBoxImage;
    Label helpText;

    int sceneToAct;
    int round;
    String playerName;

    float elapsedTime;
    final float TIME_TO_WAIT = 4.0f;

    public CutsceneScreen(Rikollisentunnistus host) {
        this.host = host;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth, camera.viewportHeight));

        width = camera.viewportWidth;
        height = camera.viewportHeight;

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));
        text = new Label("", mySkin);

        helpText = new Label(host.texts.get(49), mySkin);
        helpText.setPosition(width/2 - helpText.getWidth()/2, helpText.getHeight()/2);
        helpText.setVisible(false);

        Settings settings = Settings.getInstance();
        soundEffectsOn = settings.getBoolean("soundEffects", GameData.DEFAULT_SOUND_EFFECTS);

        Image backgroundImage = new Image(new Texture("cutscenes/background-office.jpg"));
        assistantImage1 = new Image(new Texture("cutscenes/assistent1.png"));
        assistantImage2 = new Image(new Texture("cutscenes/assistent2.png"));
        assistantImage3 = new Image(new Texture("cutscenes/assistent3.png"));
        chiefImage1 = new Image(new Texture("cutscenes/chief1.png"));
        chiefImage2 = new Image(new Texture("cutscenes/chief2.png"));
        chiefImage3 = new Image(new Texture("cutscenes/chief3.png"));
        dialogBoxImage = new Image(new Texture("cutscenes/dialogbox.png"));

        assistantImage1.setVisible(false);
        assistantImage1.setPosition(width*5/4f - assistantImage1.getWidth()/2, 0);
        assistantImage2.setVisible(false);
        assistantImage2.setPosition(width*5/4f - assistantImage1.getWidth()/2, 0);
        assistantImage3.setVisible(false);
        assistantImage3.setPosition(width*5/4f - assistantImage1.getWidth()/2, 0);

        chiefImage1.setVisible(false);
        chiefImage1.setPosition(-width/4 - chiefImage1.getWidth()/2,0);
        chiefImage2.setVisible(false);
        chiefImage2.setPosition(-width/4 - chiefImage1.getWidth()/2,0);
        chiefImage3.setVisible(false);
        chiefImage3.setPosition(-width/4 - chiefImage1.getWidth()/2,0);

        dialogBoxImage.setVisible(true);
        dialogBoxImage.setPosition(width/2 - dialogBoxImage.getWidth()/2,height/60f);

        backgroundImage.setBounds(0,0,width,height);

        stage.addActor(backgroundImage);
        stage.addActor(assistantImage1);
        stage.addActor(assistantImage2);
        stage.addActor(assistantImage3);
        stage.addActor(chiefImage1);
        stage.addActor(chiefImage2);
        stage.addActor(chiefImage3);
        stage.addActor(dialogBoxImage);
        stage.addActor(text);
        stage.addActor(helpText);

        createPauseWindow();

        round = 1;
        sceneToAct = 0;

        elapsedTime = 0;
    }

    private void createPauseWindow() {
        pauseWindow = new Window(host.texts.get(43), mySkin);
        pauseWindow.setVisible(false);
        pauseWindow.setResizable(false);
        pauseWindow.setMovable(false);

        TextButton continueButton = new TextButton(host.texts.get(12), mySkin);
        continueButton.addListener(
                new ClickListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        SoundManager.playButtonPushSound(host.soundEffectsOn);
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        pauseWindow.setVisible(false);
                        paused = false;
                    }

                });

        TextButton menuButton = new TextButton(host.texts.get(15), mySkin);
        menuButton.addListener(
                new ClickListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        SoundManager.playButtonPushSound(host.soundEffectsOn);
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        paused = false;
                        host.resetAll();
                        host.setMainScreen();
                        SoundManager.stopIngameMusic();
                    }

                });

        pauseWindow.add(continueButton).pad(10).row();
        pauseWindow.add(menuButton).pad(10).row();
        pauseWindow.pack();

        pauseWindow.setPosition((width - pauseWindow.getWidth()) / 2, (height - pauseWindow.getHeight()) / 2);

        stage.addActor(pauseWindow);
    }

    /**
     * Updates language of helpText;
     */
    public void updateLanguage() {
        helpText.setText(host.texts.get(49));
        helpText.setPosition(width/2 - helpText.getWidth()/2, helpText.getHeight()/2);
    }

    /**
     * Sets the name of the player.
     *
     * Gets the name of the player from savefiles and sets it to playerName
     */
    public void setName() {
        if (host.gameData.profileUsed == 1) {
            playerName = host.saveFiles.getString("name1", "");
        } else if (host.gameData.profileUsed == 2) {
            playerName = host.saveFiles.getString("name2", "");
        } else if (host.gameData.profileUsed == 3) {
            playerName = host.saveFiles.getString("name3", "");
        }
    }

    /**
     * Makes MoveToAction for assistant to move onscreen.
     *
     * @return MoveToAction
     */
    private MoveToAction moveAssistantOnscreen() {
        MoveToAction move = new MoveToAction();
        move.setDuration(0.5f);
        move.setPosition(width*3/4f - assistantImage1.getWidth()/2,0);

        return move;
    }

    /**
     * Makes MoveToAction for assistant to move offscreen.
     *
     * @return MoveToAction
     */
    private MoveToAction moveAssistantOffscreen() {
        MoveToAction move = new MoveToAction();
        move.setDuration(0.5f);
        move.setPosition(width*5/4f - assistantImage1.getWidth()/2,0);

        return move;
    }

    /**
     * Makes MoveToAction for chief to move onscreen.
     *
     * @return MoveToAction
     */
    private MoveToAction moveChiefOnscreen() {
        MoveToAction move = new MoveToAction();
        move.setDuration(0.5f);
        move.setPosition(width/4 - chiefImage1.getWidth()/2, 0);

        return move;
    }

    /**
     * Makes MoveToAction for chief to move offscreen.
     *
     * @return MoveToAction
     */
    private MoveToAction moveChiefOffscreen() {
        MoveToAction move = new MoveToAction();
        move.setDuration(0.5f);
        move.setPosition(-width/4 - chiefImage1.getWidth()/2, 0);

        return move;
    }

    /**
     * Sets dialog text.
     *
     * @param string    text to be set
     */
    private void setText(String string) {
        text.setText(string);
        text.setPosition(width/2-dialogBoxImage.getWidth()*24/50,dialogBoxImage.getHeight()*1/2f + height/60f);
    }

    /**
     * Sets first part of given scene.
     *
     * @param number    Scene number
     */
    public void setScene(int number) {
        sceneToAct = number;

        if(sceneToAct == 1) {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene1_line1"));
        } else if (sceneToAct == 2) {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene2_line1"));
        } else if (sceneToAct == 3) {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene3_line1"));
        } else if (sceneToAct == 4) {
            chiefImage1.setVisible(true);
            MoveToAction move = moveChiefOnscreen();
            chiefImage1.addAction(move);
            setText(host.script.get("scene4_line1"));
        } else if (sceneToAct == 5) {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene5_line1"));
        } else if (sceneToAct == 6) {
            assistantImage3.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage3.addAction(move);
            setText(host.script.get("scene6_line1"));
        } else if (sceneToAct == 7) {
            assistantImage3.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage3.addAction(move);
            setText(host.script.get("scene7_line1"));
        } else if (sceneToAct == 8) {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene8_line1"));
        } else if (sceneToAct == 9) {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene9_line1"));
        } else if (sceneToAct == 10) {
            chiefImage1.setVisible(true);
            MoveToAction move = moveChiefOnscreen();
            chiefImage1.addAction(move);
            setText(host.script.format("scene10_line1", playerName));
        } else if (sceneToAct == 11) {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene11_line1"));
        } else if (sceneToAct == 12) {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene12_line1"));
        } else if (sceneToAct == 13) {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene13_line1"));
        } else if (sceneToAct == 14) {
            assistantImage3.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage3.addAction(move);
            setText(host.script.get("scene14_line1"));
        } else {
            assistantImage1.setVisible(true);
            MoveToAction move = moveAssistantOnscreen();
            assistantImage1.addAction(move);
            setText(host.script.get("scene15_line1"));
        }

        round = 1;
    }

    /**
     * Resets everything and sets rankScreen.
     */
    private void setNextScreen() {
        assistantImage1.setVisible(false);
        assistantImage1.setPosition(width*5/4f - assistantImage1.getWidth()/2, 0);
        assistantImage2.setVisible(false);
        assistantImage2.setPosition(width*5/4f - assistantImage1.getWidth()/2, 0);
        assistantImage3.setVisible(false);
        assistantImage3.setPosition(width*5/4f - assistantImage1.getWidth()/2, 0);

        chiefImage1.setVisible(false);
        chiefImage1.setPosition(-width/4 - chiefImage1.getWidth()/2,0);
        chiefImage2.setVisible(false);
        chiefImage2.setPosition(-width/4 - chiefImage1.getWidth()/2,0);
        chiefImage3.setVisible(false);
        chiefImage3.setPosition(-width/4 - chiefImage1.getWidth()/2,0);

        setText("");

        round = 0;

        RankScreen rankScreen = new RankScreen(host);
        SoundManager.stopMenuMusic();
        SoundManager.stopLastCutsceneMusic();
        host.setScreen(rankScreen);
    }

    /**
     * Checks which scene to act and acts it.
     */
    private void actScene() {
        if(sceneToAct == 1) {
            actScene1();
        } else if (sceneToAct == 2) {
            actScene2();
        } else if (sceneToAct == 3) {
            actScene3();
        } else if (sceneToAct == 4) {
            actScene4();
        } else if (sceneToAct == 5) {
            actScene5();
        } else if (sceneToAct == 6) {
            actScene6();
        } else if (sceneToAct == 7) {
            actScene7();
        } else if (sceneToAct == 8) {
            actScene8();
        } else if (sceneToAct == 9) {
            actScene9();
        } else if (sceneToAct == 10) {
            actScene10();
        } else if (sceneToAct == 11) {
            actScene11();
        } else if (sceneToAct == 12) {
            actScene12();
        } else if (sceneToAct == 13) {
            actScene13();
        } else if (sceneToAct == 14) {
            actScene14();
        } else {
            actScene15();
        }

        round++;
    }

    /**
     * Acts scene 1 and plays tutorial
     */
    private void actScene1() {
        if (round == 1) {
            text.setText(host.script.get("scene1_line2"));
        } else if (round == 2) {
            text.setText(host.script.get("scene1_line3"));
        } else if (round == 3) {
            TutorialScreen tutorialScreen = new TutorialScreen(host);
            host.setScreen(tutorialScreen);
            text.setText(host.script.get("scene1_line11"));
        }  else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 2
     */
    private void actScene2() {
        if (round == 1) {
            setText(host.script.get("scene2_line2"));
        } else if (round == 2) {
            assistantImage1.setVisible(false);
            assistantImage3.setVisible(true);
            assistantImage3.setPosition(assistantImage1.getX(), 0);
            setText(host.script.get("scene2_line3"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 3
     */
    private void actScene3() {
        if (round == 1) {
            assistantImage1.setVisible(false);
            assistantImage3.setVisible(true);
            assistantImage3.setPosition(assistantImage1.getX(), 0);
            setText(host.script.get("scene3_line2"));
        } else if (round == 2) {
            assistantImage3.setVisible(false);
            assistantImage1.setVisible(true);
            setText(host.script.get("scene3_line3"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 4
     */
    private void actScene4() {
        if (round == 1) {
            MoveToAction moveChief = moveChiefOffscreen();
            MoveToAction moveAssistant = moveAssistantOnscreen();
            chiefImage1.addAction(moveChief);
            assistantImage1.setVisible(true);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.get("scene4_line2"));
        } else if (round == 2) {
            chiefImage1.setVisible(false);
            chiefImage2.setVisible(true);
            MoveToAction moveChief = moveChiefOnscreen();
            MoveToAction moveAssistant = moveAssistantOffscreen();
            assistantImage1.addAction(moveAssistant);
            chiefImage2.addAction(moveChief);
            setText(host.script.get("scene4_line3"));
        } else if (round == 3) {
            MoveToAction moveChief = moveChiefOffscreen();
            MoveToAction moveAssistant = moveAssistantOnscreen();
            chiefImage2.addAction(moveChief);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.get("scene4_line4"));
        } else if (round == 4) {
            MoveToAction moveChief = moveChiefOnscreen();
            MoveToAction moveAssistant = moveAssistantOffscreen();
            assistantImage1.addAction(moveAssistant);
            chiefImage2.addAction(moveChief);
            setText(host.script.format("scene4_line5", playerName));
        } else if (round == 5) {
            MoveToAction moveChief = moveChiefOffscreen();
            MoveToAction moveAssistant = moveAssistantOnscreen();
            chiefImage2.addAction(moveChief);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.format("scene4_line6", playerName));
        } else if (round == 6) {
            MoveToAction moveChief = moveChiefOnscreen();
            MoveToAction moveAssistant = moveAssistantOffscreen();
            assistantImage1.addAction(moveAssistant);
            chiefImage2.addAction(moveChief);
            setText(host.script.get("scene4_line7"));
        } else if (round == 7) {
            setText(host.script.get("scene4_line8"));
        } else if (round == 8) {
            MoveToAction moveChief = moveChiefOffscreen();
            MoveToAction moveAssistant = moveAssistantOnscreen();
            chiefImage2.addAction(moveChief);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.get("scene4_line9"));
        } else if (round == 9) {
            assistantImage3.setVisible(true);
            assistantImage1.setVisible(false);
            assistantImage3.setPosition(assistantImage1.getX(), 0);
            setText(host.script.get("scene4_line10"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 5
     */
    private void actScene5() {
        if (round == 1) {
            assistantImage1.setVisible(false);
            assistantImage2.setVisible(true);
            assistantImage2.setPosition(assistantImage1.getX(), 0);
            setText(host.script.get("scene5_line2"));
        } else if (round == 2) {
            setText(host.script.get("scene5_line3"));
        } else if (round == 3) {
            assistantImage1.setVisible(true);
            assistantImage2.setVisible(false);
            setText(host.script.get("scene5_line4"));
        } else if (round == 4) {
            setText(host.script.get("scene5_line5"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 6
     */
    public void actScene6() {
        if (round == 1) {
            assistantImage3.setVisible(false);
            assistantImage1.setVisible(true);
            assistantImage1.setPosition(assistantImage3.getX(), 0);
            setText(host.script.get("scene6_line2"));
        } else if (round == 2) {
            setText(host.script.get("scene6_line3"));
        } else if (round == 3) {
            assistantImage1.setVisible(false);
            assistantImage3.setVisible(true);
            setText(host.script.get("scene6_line4"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 7
     */
    public void actScene7() {
        if (round == 1) {
            assistantImage3.setVisible(false);
            assistantImage2.setVisible(true);
            assistantImage2.setPosition(assistantImage3.getX(), 0);
            setText(host.script.get("scene7_line2"));
        } else if (round == 2) {
            assistantImage2.setVisible(false);
            assistantImage3.setVisible(true);
            setText(host.script.get("scene7_line3"));
        }  else if (round == 3) {
            assistantImage3.setVisible(false);
            assistantImage1.setVisible(true);
            assistantImage1.setPosition(assistantImage2.getX(), 0);
            setText(host.script.get("scene7_line4"));
        } else if (round == 4) {
            assistantImage1.setVisible(false);
            assistantImage3.setVisible(true);
            setText(host.script.get("scene7_line5"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 8
     */
    public void actScene8() {
        if (round == 1) {
            setText(host.script.get("scene8_line2"));
        } else if (round == 2) {
            assistantImage1.setVisible(false);
            assistantImage3.setVisible(true);
            assistantImage3.setPosition(assistantImage1.getX(), 0);
            setText(host.script.get("scene8_line3"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 9
     */
    public void actScene9() {
        if (round == 1) {
            setText(host.script.get("scene9_line2"));
        } else if (round == 2) {
            assistantImage1.setVisible(false);
            assistantImage2.setVisible(true);
            assistantImage2.setPosition(assistantImage1.getX(), 0);
            setText(host.script.get("scene9_line3"));
        } else if (round == 3) {
            assistantImage2.setVisible(false);
            assistantImage1.setVisible(true);
            assistantImage1.setPosition(assistantImage1.getX(), 0);
            setText(host.script.get("scene9_line4"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 10
     */
    public void actScene10() {
        if (round == 1) {
            setText(host.script.get("scene10_line2"));
        } else if (round == 2) {
            setText(host.script.get("scene10_line3"));
        } else if (round == 3) {
            setText(host.script.get("scene10_line4"));
        } else if (round == 4) {
            setText(host.script.get("scene10_line5"));
        } else if (round == 5) {
            chiefImage2.setPosition(chiefImage1.getX(), 0);
            chiefImage2.setVisible(true);
            chiefImage1.setVisible(false);
            setText(host.script.get("scene10_line6"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 11
     */
    public void actScene11() {
        if (round == 1) {
            setText(host.script.get("scene11_line2"));
        } else if (round == 2) {
            assistantImage1.setVisible(false);
            assistantImage3.setVisible(true);
            assistantImage3.setPosition(assistantImage1.getX(), 0);
            setText(host.script.get("scene11_line3"));
        } else if (round == 3) {
            assistantImage3.setVisible(false);
            assistantImage1.setVisible(true);
            setText(host.script.get("scene11_line4"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 12
     */
    public void actScene12() {
        if (round == 1) {
            setText(host.script.get("scene12_line2"));
        } else if (round == 2) {
            assistantImage3.setVisible(true);
            assistantImage1.setVisible(false);
            assistantImage3.setPosition(assistantImage1.getX(), 0);
            setText(host.script.get("scene12_line3"));
        } else if (round == 3) {
            assistantImage3.setVisible(false);
            assistantImage1.setVisible(true);
            setText(host.script.get("scene12_line4"));
        } else if (round == 4) {
            setText(host.script.get("scene12_line5"));
        } else if (round == 5) {
            setText(host.script.get("scene12_line6"));
        } else if (round == 6) {
            setText(host.script.get("scene12_line7"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 13
     */
    public void actScene13() {
        if (round == 1) {
            assistantImage3.setPosition(assistantImage1.getX(), 0);
            assistantImage3.setVisible(true);
            assistantImage1.setVisible(false);
            setText(host.script.get("scene13_line2"));
        } else if (round == 2) {
            assistantImage2.setPosition(assistantImage1.getX(), 0);
            assistantImage2.setVisible(true);
            assistantImage3.setVisible(false);
            setText(host.script.get("scene13_line3"));
        } else if (round == 3) {
            assistantImage1.setVisible(true);
            assistantImage2.setVisible(false);
            setText(host.script.get("scene13_line4"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 14
     */
    public void actScene14() {
        if (round == 1) {
            assistantImage1.setPosition(assistantImage3.getX(), 0);
            assistantImage1.setVisible(true);
            assistantImage3.setVisible(false);
            setText(host.script.get("scene14_line2"));
        } else if (round == 2) {
            assistantImage3.setVisible(true);
            assistantImage1.setVisible(false);
            setText(host.script.get("scene14_line3"));
        } else if (round == 3) {
            assistantImage1.setVisible(true);
            assistantImage3.setVisible(false);
            setText(host.script.get("scene14_line4"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Acts scene 15
     */
    public void actScene15() {
        if (round == 1) {
            setText(host.script.get("scene15_line2"));
        } else if (round == 2) {
            MoveToAction moveChief = moveChiefOnscreen();
            MoveToAction moveAssistant = moveAssistantOffscreen();
            chiefImage3.setVisible(true);
            chiefImage3.addAction(moveChief);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.get("scene15_line3"));
        } else if (round == 3) {
            MoveToAction moveChief = moveChiefOffscreen();
            MoveToAction moveAssistant = moveAssistantOnscreen();
            chiefImage3.addAction(moveChief);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.get("scene15_line4"));
        }  else if (round == 4) {
            MoveToAction moveChief = moveChiefOnscreen();
            MoveToAction moveAssistant = moveAssistantOffscreen();
            chiefImage3.addAction(moveChief);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.get("scene15_line5"));
        } else if (round == 5) {
            MoveToAction moveChief = moveChiefOffscreen();
            MoveToAction moveAssistant = moveAssistantOnscreen();
            chiefImage3.addAction(moveChief);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.get("scene15_line6"));
        }  else if (round == 6) {
            MoveToAction moveChief = moveChiefOnscreen();
            MoveToAction moveAssistant = moveAssistantOffscreen();
            chiefImage3.addAction(moveChief);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.get("scene15_line7"));
        } else if (round == 7) {
            setText(host.script.format("scene15_line8", playerName));
        }  else if (round == 8) {
            MoveToAction moveChief = moveChiefOffscreen();
            MoveToAction moveAssistant = moveAssistantOnscreen();
            chiefImage3.addAction(moveChief);
            assistantImage1.addAction(moveAssistant);
            setText(host.script.get("scene15_line9"));
        } else if (round == 9) {
            assistantImage3.setVisible(true);
            assistantImage1.setVisible(false);
            assistantImage3.setPosition(assistantImage1.getX(), 0);
            setText(host.script.format("scene15_line10", playerName));
        }  else if (round == 10) {
            MoveToAction moveChief = moveChiefOnscreen();
            MoveToAction moveAssistant = moveAssistantOffscreen();
            chiefImage3.addAction(moveChief);
            assistantImage3.addAction(moveAssistant);
            setText(host.script.format("scene15_line11", playerName));
        } else if (round == 11) {
            setText(host.script.get("scene15_line12"));
        } else {
            setNextScreen();
        }
    }

    /**
     * Checks if given time has passed.
     *
     * @return      true if given time has passed
     */
    private boolean timer(float timeToPass) {
        return timeToPass <= elapsedTime;
    }

    @Override
    public void show() {
        setName();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(stage);

        Settings settings = Settings.getInstance();
        soundEffectsOn = settings.getBoolean("soundEffects", GameData.DEFAULT_SOUND_EFFECTS);

        SoundManager.stopIngameMusic();
        if (sceneToAct == 15) {
            SoundManager.playLastCutsceneMusic();
        } else {
            SoundManager.playMenuMusic();
        }
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();

        if (elapsedTime >= TIME_TO_WAIT) {
            helpText.setVisible(true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || (Gdx.input.isTouched() && timer(0.5f) && !paused)) {
            actScene();
            SoundManager.playButtonPushSound(soundEffectsOn);
            helpText.setVisible(false);
            elapsedTime = 0;
        }

        if (!paused) {
            elapsedTime += delta;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseWindow.setVisible(true);
            paused = true;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {

    }
}
