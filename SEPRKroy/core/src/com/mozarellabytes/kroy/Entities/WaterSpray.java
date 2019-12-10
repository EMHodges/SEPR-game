package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;

public class WaterSpray {

    private ArrayList<Particle> spray;
    private Vector2 start;
    private Vector2 end;
    private FireTruck truck;
    private Fortress target;

    public WaterSpray(Vector2 start, Fortress target, FireTruck truck) {
        this.spray = new ArrayList<Particle>();
        this.start = start;
        this.end = target.getPosition();
        this.truck = truck;
        this.target = target;
    }

    public void createParticle() {
        this.spray.add(new Particle(this.end, this.truck, this.target));
    }

    public Vector2 getStart() {
        return this.start;
    }

    public Vector2 getEnd() {
        return this.end;
    }

    public ArrayList<Particle> getParticles() {
        return this.spray;
    }

    public void removeParticle(Particle particle) {
        this.spray.remove(particle);
    }

}
