package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.math.Vector2;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;

public class WaterSpray {

    private ArrayList<Particle> spray;
    private FireTruck truck;
    private Fortress target;

    public WaterSpray(Fortress target, FireTruck truck) {
        this.spray = new ArrayList<Particle>();
        this.truck = truck;
        this.target = target;
    }

    public void createParticle() {
        this.spray.add(new Particle(this.truck, this.target));
    }

    public ArrayList<Particle> getParticles() {
        return this.spray;
    }

    public void removeParticle(Particle particle) {
        this.spray.remove(particle);
    }

    public Fortress getTarget() {
        return this.target;
    }

}
