package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author Essi Supponen
 * @version 1.6
 * @since 2018-02-23
 *
 * Plays the cutscenes of the game and calls the TutorialScreen.
 */

public class Face extends Actor {
    private float originalScale;

    private Texture baseImg;
    private Texture faceShapeImg;
    private Texture hairImg;
    private Texture eyesImg;
    private Texture noseImg;
    private Texture mouthImg;
    private Texture accessoryImg;

    private String idCode;
    private int[] featureIds;

    boolean active;

    /**
     * Generates a totally random face from given features.
     *
     * @param bases
     * @param faceShapes
     * @param hairs
     * @param eyes
     * @param noses
     * @param mouths
     */
    public Face(Texture[] bases, Texture[] faceShapes, Texture[] hairs,
                Texture[] eyes, Texture[] noses, Texture[] mouths) {
        originalScale = 1f;

        int randomFaceShape = MathUtils.random(0, faceShapes.length - 1);
        int randomHair = MathUtils.random(0, hairs.length - 1);
        int randomEyes = MathUtils.random(0, eyes.length - 1);
        int randomNose = MathUtils.random(0, noses.length - 1);
        int randomMouth = MathUtils.random(0, mouths.length - 1);

        baseImg = bases[randomFaceShape%3];
        faceShapeImg = faceShapes[randomFaceShape];
        hairImg = hairs[randomHair];
        eyesImg = eyes[randomEyes];
        noseImg = noses[randomNose];
        mouthImg = mouths[randomMouth];

        featureIds = new int[5];
        featureIds[0] = randomFaceShape;
        featureIds[1] = randomHair;
        featureIds[2] = randomEyes;
        featureIds[3] = randomNose;
        featureIds[4] = randomMouth;

        idCode = randomFaceShape + "-" + randomHair + "-" + randomEyes + "-" + randomNose + "-" + randomMouth;

        active = false;
    }

    /**
     * Generates a Face from given features according to other requirements.
     *
     * @param bases
     * @param faceShapes
     * @param hairs
     * @param eyes
     * @param noses
     * @param mouths
     * @param attributes            Facial feature IDs
     * @param useSameAttributes     If given attributes should be used or not
     */
    public Face(Texture[] bases, Texture[] faceShapes, Texture[] hairs, Texture[] eyes,
                Texture[] noses, Texture[] mouths, int[] attributes, boolean useSameAttributes) {
        originalScale = 1f;

        int faceShapeId = -1;
        int hairId = -1;
        int eyesId = -1;
        int noseId = -1;
        int mouthId = -1;

        if (useSameAttributes) {
            /**
             * If same features must be used, goes trough the attribute list. If the attribute id is
             * -1, use a random attribute. In other cases use feature the attribute id is referring
             * to.
             */

            if (attributes[0] == -1) {
                faceShapeId = MathUtils.random(0, faceShapes.length - 1);
            } else {
                faceShapeId = attributes[0];
            }

            if (attributes[1] == -1) {
                hairId = MathUtils.random(0, hairs.length - 1);
            } else {
                hairId = attributes[1];
            }

            if (attributes[2] == -1) {
                eyesId = MathUtils.random(0, eyes.length - 1);
            } else {
                eyesId = attributes[2];
            }

            if (attributes[3] == -1) {
                noseId = MathUtils.random(0, noses.length - 1);
            } else {
                noseId = attributes[3];
            }

            if (attributes[4] == -1) {
                mouthId = MathUtils.random(0, mouths.length - 1);
            } else {
                mouthId = attributes[4];
            }
        } else {
            /**
             * If none of the attributes given should be used, makes sure generated random number
             * are not the same as given attribute IDs.
             */

            boolean attributeSet = false;
            int index;


            while (!attributeSet) {
                index = MathUtils.random(0, faceShapes.length-1);

                if (index != attributes[0]) {
                    faceShapeId = index;
                    attributeSet = true;
                }
            }

            attributeSet = false;

            while (!attributeSet) {
                index = MathUtils.random(0, hairs.length-1);

                if (index != attributes[1]) {
                    hairId = index;
                    attributeSet = true;
                }
            }

            attributeSet = false;


            while (!attributeSet) {
                index = MathUtils.random(0, eyes.length-1);

                if (index != attributes[2]) {
                    eyesId = index;
                    attributeSet = true;
                }
            }

            attributeSet = false;

            while (!attributeSet) {
                index = MathUtils.random(0, noses.length-1);

                if (index != attributes[3]) {
                    noseId = index;
                    attributeSet = true;
                }
            }

            attributeSet = false;

            while (!attributeSet) {
                index = MathUtils.random(0, noses.length-1);

                if (index != attributes[4]) {
                    mouthId = index;
                    attributeSet = true;
                }
            }
        }

        /**
         * Generates ID-code.
         */
        idCode = faceShapeId + "-" + hairId + "-" + eyesId + "-" + noseId + "-" + mouthId;

        /**
         * BaseImage will be chosen according faceShape, so that the skintones match.
         */
        baseImg = bases[faceShapeId%3];

        faceShapeImg = faceShapes[faceShapeId];
        hairImg = hairs[hairId];
        eyesImg = eyes[eyesId];
        noseImg = noses[noseId];
        mouthImg = mouths[mouthId];

        /**
         * Sets featureIDs.
         */
        featureIds = new int[5];
        featureIds[0] = faceShapeId;
        featureIds[1] = hairId;
        featureIds[2] = eyesId;
        featureIds[3] = noseId;
        featureIds[4] = mouthId;


        active = false;
    }

    /**
     * Sets a random accessory to the object.
     *
     * @param accessories
     */
    public void addAccessory(Texture[] accessories) {
        int random = MathUtils.random(0, accessories.length - 1);
        accessoryImg = accessories[random];
    }

    public void changeScale(float scale) {
        this.originalScale = scale;
    }

    public float getSpriteWidth() {
        return baseImg.getWidth();
    }

    public int[] getFeatureIds() {
        return featureIds;
    }

    public void setLocation(float x, float y) {
        this.setBounds(x,y,faceShapeImg.getWidth(),faceShapeImg.getHeight());
    }

    public void toggleActive() {
        active = !active;
    }

    public String getIdCode() {
        return idCode;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        float scale = originalScale * getScaleX();

        batch.draw(baseImg, getX() - baseImg.getWidth()*scale/2, getY(),
                baseImg.getWidth()*scale, baseImg.getHeight()*scale);

        batch.draw(faceShapeImg,getX()-faceShapeImg.getWidth()*scale/2,
                getY() + baseImg.getHeight()*scale/5,
                faceShapeImg.getWidth()*scale,faceShapeImg.getHeight()*scale);

        batch.draw(eyesImg, getX() + (4.5f/25f*faceShapeImg.getWidth() - eyesImg.getWidth()/2)*scale,
                getY()+ baseImg.getHeight()*scale/5 + (9/16f*faceShapeImg.getHeight() - eyesImg.getHeight()/2)*scale,
                eyesImg.getWidth()*scale, eyesImg.getHeight()*scale);
        batch.draw(eyesImg, getX() - (4.5f/25f*faceShapeImg.getWidth() + eyesImg.getWidth()/2)*scale,
                getY()+ (baseImg.getHeight()/5 + 9/16f*faceShapeImg.getHeight() - eyesImg.getHeight()/2)*scale,
                eyesImg.getWidth()*scale, eyesImg.getHeight()*scale,
                0, 0, eyesImg.getWidth(), eyesImg.getHeight(),
                true, false);
        batch.draw(noseImg, getX() - (noseImg.getWidth()/2)*scale,
                getY()+ (baseImg.getHeight()/5 + (2/5f*faceShapeImg.getHeight() - noseImg.getHeight()/2))*scale,
                noseImg.getWidth()*scale, noseImg.getHeight()*scale);
        batch.draw(mouthImg, getX() - (mouthImg.getWidth()/2)*scale,
                getY()+ (baseImg.getHeight()/5 + 5/18f*faceShapeImg.getHeight() - 1/2f*mouthImg.getHeight())*scale,
                mouthImg.getWidth()*scale,mouthImg.getHeight()*scale);


        if (accessoryImg != null) {
            batch.draw(accessoryImg, getX() - (accessoryImg.getWidth()/2)*scale,
                    getY()+ baseImg.getHeight()*scale/5,
                    accessoryImg.getWidth()*scale,accessoryImg.getHeight()*scale);
        }

        batch.draw(hairImg, getX() - (hairImg.getWidth()/2)*scale,
                getY()+ (baseImg.getHeight()/5+ faceShapeImg.getHeight() - hairImg.getHeight()/2)*scale,
                hairImg.getWidth()*scale, hairImg.getHeight()*scale);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
