package com.mozarellabytes.kroy.Utilities;

import java.util.Random;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

/**
 * Allows for Camera Shaking
 */
public class CameraShake {
    private static float[] samples;
    private static float internalTimer = 0;
    private static float shakeDuration = 0;

    private static final int duration = 5;
    private static final int frequency = 35;

    private static int sampleCount;

    /**
     * Generates an array of random samples the size of duration * frequency used in the update() method
     * to calculate new camera positions.
     */
    public CameraShake(){
        sampleCount = duration * frequency;
        samples = new float[sampleCount];

        for (int i =0; i < sampleCount; i++){
            Random rand = new Random();
            samples[i] = rand.nextFloat() * 2f - 1f;
        }
    }

    /**
     * Calculates new camera positions using randomly generated samples (in the constructor)
     * and rapidly offsets the camera by using those positions. The method calls itself until
     * shakeDuration reaches 0.
     *
     * Must be called every frame in the screen where camera shakes are supposed to happen.
     *
     * @param delta Current Time (In-Game)
     * @param camera Camera Object (To change its position)
     * @param center Center of camera to allow it to return to its original position
     */
    public static void update(float delta, Camera camera, Vector2 center){
        internalTimer += delta;
        if (internalTimer > duration) {
            internalTimer -= duration;
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
     * Called in-game whenever a Camera Shake happens.
     *
     * @param seconds Duration of the Camera Shake
     */
    public void shakeIt(float seconds){
        shakeDuration = seconds;
    }
}
