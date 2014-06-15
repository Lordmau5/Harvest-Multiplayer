package com.lordmau5.harvest.client;

import org.newdawn.slick.*;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 11:43
 */
public class Client extends BasicGame {

    /**
     * Create a new basic game
     *
     * @param title The title for the game
     */
    public Client() {
        super("Harvest Multiplayer");
    }

    public static void main(String args[]) {
        AppGameContainer game;
        try {
            game = new AppGameContainer(new Client());
            game.setDisplayMode(800, 600, false);
            game.setShowFPS(false);
            game.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {

    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {

    }
}