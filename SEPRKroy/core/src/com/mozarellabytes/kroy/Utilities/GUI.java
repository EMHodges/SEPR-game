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
        renderBar(truck.getHP(), truck.getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderBar(truck.getReserve(), truck.getMaxReserve(), Color.CYAN, Color.BLUE, 2);
    }

    private void renderBackground() {
        renderer.setColor(0, 0, 0, 0.5f);
        renderer.rect(x, y, w, h);
    }

    private void renderFortress(Fortress fortress) {
        renderBar(fortress.getHP(), fortress.getMaxHP(), Color.RED, Color.FIREBRICK, 1);
    }

    private void renderBar(float value, float maxValue, Color progressColour, Color backgroundColour, int position) {
        int whiteW = 50;
        int outerSpacing = 10;
        int innerSpacing = 5;
        int spaceForText = 50;
        int barHeight = this.h - outerSpacing*2 - innerSpacing*2 - spaceForText;
        int positionSpacer = position * whiteW;
        int barSpacer = 0;
        if (position > 1) barSpacer = 5;
        renderer.rect(this.x + this.w - positionSpacer - outerSpacing - barSpacer, this.y + outerSpacing, whiteW, this.h - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        renderer.rect(this.x + this.w - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.y + outerSpacing + innerSpacing, whiteW - innerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
        renderer.rect(this.x + this.w - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.y + outerSpacing + innerSpacing, whiteW - innerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);
    }
}
