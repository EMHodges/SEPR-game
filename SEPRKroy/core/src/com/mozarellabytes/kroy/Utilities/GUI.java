package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mozarellabytes.kroy.Entities.FireTruck;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Kroy;

public class GUI {

    private Kroy game;
    private ShapeRenderer renderer;
    private int x, y, w, h;

    public GUI(Kroy game, ShapeRenderer shapeRenderer, int w, int h) {
        this.game = game;
        this.renderer = shapeRenderer;
        this.x = 10;
        this.y = Gdx.graphics.getHeight() - 10 - h;
        this.w = w;
        this.h = h;
    }

    public void render(Object entity) {
        if (entity != null) {
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

    private void renderBackground() {
        renderer.setColor(0, 0, 0, 0.5f);
        renderer.rect(x, y, w, h);
    }

    private void renderTruck(FireTruck truck) {
        // also render text stats along left side
        renderBar(truck.getHP(), truck.getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderBar(truck.getReserve(), truck.getMaxReserve(), Color.CYAN, Color.BLUE, 2);
        renderText(truck);
    }

    private void renderFortress(Fortress fortress) {
        renderBar(fortress.getHP(), fortress.getMaxHP(), Color.RED, Color.FIREBRICK, 1);
        renderText(fortress);
    }

    private void renderText(FireTruck truck) {
        int newLine = 20;
        game.batch.begin();
        game.bigFont.draw(game.batch, truck.getName(), this.x + 10, this.y + this.h - 10);
        game.smallFont.draw(game.batch, "HP: ", this.x + 15, this.y + this.h - 50);
        game.smallFont.draw(game.batch, String.format("%.1f", truck.getHP()) + " / " + String.format("%.1f", truck.getMaxHP()), this.x + 20, this.y + this.h - 50 - newLine);
        game.smallFont.draw(game.batch, "Reserve: ", this.x + 15, this.y + this.h - 50 - newLine*2);
        game.smallFont.draw(game.batch, String.format("%.1f", truck.getReserve()) + " / " + String.format("%.1f", truck.getMaxReserve()), this.x + 20, this.y + this.h - 50 - newLine*3);
        game.smallFont.draw(game.batch, "Speed: ", this.x + 15, this.y + this.h - 50 - newLine*4);
        game.smallFont.draw(game.batch, String.format("%.1f", truck.getSpeed()), this.x + 20, this.y + this.h - 50 - newLine*5);
        game.smallFont.draw(game.batch, "Range: ", this.x + 15, this.y + this.h - 50 - newLine*6);
        game.smallFont.draw(game.batch, String.format("%.1f", truck.getRange()), this.x + 20, this.y + this.h - 50 - newLine*7);
        game.smallFont.draw(game.batch, "AP: ", this.x + 15, this.y + this.h - 50 - newLine*8);
        game.smallFont.draw(game.batch, String.format("%.2f", truck.getAP()), this.x + 20, this.y + this.h - 50 - newLine*9);
        game.batch.end();
    }

    private void renderText(Fortress fortress) {
        int newLine = 20;
        game.batch.begin();
        game.bigFont.draw(game.batch, fortress.getName(), this.x + 10, this.y + this.h - 10);
        game.smallFont.draw(game.batch, "HP: ", this.x + 15, this.y + this.h - 50);
        game.smallFont.draw(game.batch, String.format("%.1f", fortress.getHP()) + " / " + String.format("%.1f", fortress.getMaxHP()), this.x + 20, this.y + this.h - 50 - newLine);
        game.smallFont.draw(game.batch, "Range: ", this.x + 15, this.y + this.h - 50 - newLine*2);
        game.smallFont.draw(game.batch, String.format("%.1f", fortress.getRange()), this.x + 20, this.y + this.h - 50 - newLine*3);
        game.smallFont.draw(game.batch, "AP: ", this.x + 15, this.y + this.h - 50 - newLine*4);
        game.smallFont.draw(game.batch, String.format("%.2f", fortress.getAP()), this.x + 20, this.y + this.h - 50 - newLine*5);
        game.batch.end();
    }

    private void renderBar(float value, float maxValue, Color progressColour, Color backgroundColour, int position) {
        int whiteW = 50;
        int outerSpacing = 10;
        int innerSpacing = 5;
        int spaceForText = 35;
        int barHeight = this.h - outerSpacing*2 - innerSpacing*2 - spaceForText;
        int positionSpacer = position * whiteW;
        int barSpacer = 0;
        if (position > 1) barSpacer = 5;
        renderer.rect(this.x + this.w - positionSpacer - outerSpacing - barSpacer, this.y + outerSpacing, whiteW, this.h - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        renderer.rect(this.x + this.w - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.y + outerSpacing + innerSpacing, whiteW - innerSpacing*2, barHeight, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
        renderer.rect(this.x + this.w - positionSpacer - outerSpacing + innerSpacing - barSpacer, this.y + outerSpacing + innerSpacing, whiteW - innerSpacing*2, value/maxValue*barHeight, progressColour, progressColour, progressColour, progressColour);
    }
}
