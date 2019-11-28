package com.mozarellabytes.kroy.Utilities;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Screens.GameScreen;

import java.util.Random;

import java.util.Random;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class CameraShake {
    private static float[] samples;
    private Random rand = new Random();
    private static float internalTimer = 0;
    private static float shakeDuration = 0;

    private static int duration = 5;
    private static int frequency = 35;
    private static float amplitude = 2;
    private static boolean falloff = true;

    private static int sampleCount;

    public CameraShake(){
        sampleCount = duration * frequency;
        samples = new float[sampleCount];

        for (int i =0; i < sampleCount; i++){
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

            camera.position.x = center.x + deltaX * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
            camera.position.y = center.y + deltaY * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
            camera.update();
        }
    }

    public void shakeIt(float seconds){
        shakeDuration = seconds;
    }
}
