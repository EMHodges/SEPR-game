package com.mozarellabytes.kroy.Utilities;

import java.util.Random;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class CameraShake {
    private static float[] samples;
    private static float internalTimer = 0;
    private static float shakeDuration = 0;

    private static final int duration = 5;
    private static final int frequency = 35;

    private static int sampleCount;

    public CameraShake(){
        sampleCount = duration * frequency;
        samples = new float[sampleCount];

        for (int i =0; i < sampleCount; i++){
            Random rand = new Random();
            samples[i] = rand.nextFloat() * 2f - 1f;
        }
    }

    public static void update(float dt, Camera camera, Vector2 center){
        internalTimer += dt;
        if (internalTimer > duration) {
            internalTimer -= duration;
        }

        if (shakeDuration > 0){
            shakeDuration -= dt;
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

    public void shakeIt(float seconds){
        shakeDuration = seconds;
    }
}
