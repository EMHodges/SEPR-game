package com.mozarellabytes.kroy.Utilities;

import java.util.Random;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

/**
 * Allows for Camera Shaking by calling this update() method every frame in the game screen
 * and checking if shakeDuration is > 0, in which case the camera will "shake" for this duration.
 */
public class CameraShake {

    /** Array of floating point values used for X and Y axis offsetting. */
    private static float[] samples;

    /** Keeps track of time inside the update() method */
    private static float internalTimer = 0;

    /** Duration of camera shake in approximate seconds */
    private static float shakeDuration = 0;

    /** Used to calculate sampleCount */
    private static final int amount = 5;

    /** Used to calculate sampleCount and affect the smoothing of the camera shake */
    private static final int frequency = 35;

    /** Determines the size of "samples" */
    private static int sampleCount;

    /**
     * Generates an array of random samples the size of duration * frequency used in the update() method
     * to calculate new camera positions.
     */
    public CameraShake(){
        sampleCount = amount * frequency;
        samples = new float[sampleCount];

        for (int i =0; i < sampleCount; i++){
            Random rand = new Random();
            samples[i] = rand.nextFloat() * 2f - 1f;
        }
    }

    /**
     * Calculates new camera positions using randomly generated samples (in the constructor)
     * and rapidly offsets the camera by using those positions. The method is called every frame
     * until shakeDuration reaches 0.
     *
     * Must be called every frame in the screen where camera shakes are supposed to happen.
     *
     * @param delta Time since last render
     * @param camera Camera Object (To change its position)
     * @param center Center of camera to allow it to return to its original position
     */
    public static void update(float delta, Camera camera, Vector2 center){
        internalTimer += delta;

        if (internalTimer > amount) {
            internalTimer -= amount;
        }

        if (shakeDuration > 0){
            shakeDuration -= delta;
            float shakeTime = (internalTimer * frequency);
            int first = (int)shakeTime;
            int second = (first + 1)%sampleCount;
            int third = (first + 2)%sampleCount;
            float deltaT = shakeTime - (int)shakeTime;
            float deltaX = samples[first] * deltaT + samples[second] * ( 1f - deltaT);
            float deltaY = samples[second] * deltaT + samples[third] * ( 1f - deltaT);

            boolean falloff = true;
            float amplitude = 2;
            camera.position.x = center.x + deltaX * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
            camera.position.y = center.y + deltaY * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
            camera.update();
        }
    }

    /**
     * Called in-game whenever a Camera Shake happens. Since duration > 0, the update()
     * method begins shaking the camera
     *
     * @param seconds Duration of the Camera Shake
     */
    public void shakeIt(float seconds){
        shakeDuration = seconds;
    }
}
