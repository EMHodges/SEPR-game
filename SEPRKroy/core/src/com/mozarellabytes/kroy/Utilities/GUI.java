package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;

public class GUI {

    private ShapeRenderer renderer;
    private int x, y, w, h;

    public GUI(ShapeRenderer shapeRenderer, int w, int h) {
        this.renderer = shapeRenderer;
        this.x = 10;
        this.y = Gdx.graphics.getHeight() - 10 - h;
        this.w = w;
        this.h = h;
    }

    public void render(Object entity) {
        if (entity != null) {
            Gdx.app.log("Entity", entity.toString());
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderBackground();
            renderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            // if fire truck is selected
            if (entity instanceof FireTruck) {
                FireTruck truck = (FireTruck) entity;
                renderTruck(truck);
            } else if (entity instanceof Fortress) {
                Fortress fortress = (Fortress) entity;
                renderFortress(fortress);
            }
            renderer.end();

        }
    }

    private void renderTruck(FireTruck truck) {
        renderer.rect(10, Gdx.graphics.getHeight()-10-100, 0.6f*100,0.8f*100);
        renderer.rect(17.5f, Gdx.graphics.getHeight()-10-100, 0.2f*100,0.6f*100, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        renderer.rect(17.5f, Gdx.graphics.getHeight()-10-100, 0.2f*100,(float) truck.getHP() / (float) truck.type.getMaxHP() * 0.6f*100, Color.RED, Color.RED, Color.RED, Color.RED);
        renderer.rect(42.5f, Gdx.graphics.getHeight()-10-100, 0.2f*100,0.6f*100, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE);
        renderer.rect(42.5f, Gdx.graphics.getHeight()-10-100, 0.2f*100, (float) truck.getReserve() / (float) truck.type.getMaxReserve() * 0.6f*100, Color.CYAN, Color.CYAN, Color.CYAN, Color.CYAN);
    }

    private void renderBackground() {
        renderer.setColor(0, 0, 0, 0.5f);
        renderer.rect(x, y, w, h);
    }

    private void renderFortress(Fortress fortress) {
        renderBar(fortress.getHP(), fortress.getMaxHP(), Color.RED, Color.FIREBRICK);
    }

    private void renderBar(float value, float maxValue, Color progressColour, Color backgroundColour) {
        int whiteW = 50;
        int spacing = 10;
        int smallerSpacing = 5;
        int barHeight = this.h - spacing*2 - smallerSpacing*2 - 50;
        renderer.rect(this.x + this.w - whiteW - spacing, this.y + spacing, whiteW, this.h - spacing*2 - 50, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        renderer.rect(this.x + this.w - whiteW - spacing + smallerSpacing, this.y + spacing + smallerSpacing, whiteW - smallerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
        renderer.rect(this.x + this.w - whiteW - spacing + smallerSpacing, this.y + spacing + smallerSpacing, whiteW - smallerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);
    }
}
