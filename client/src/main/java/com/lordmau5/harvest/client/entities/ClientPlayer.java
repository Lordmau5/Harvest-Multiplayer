package com.lordmau5.harvest.client.entities;

import com.lordmau5.harvest.client.util.texture.Sprite;
import com.lordmau5.harvest.client.util.texture.Texture;
import com.lordmau5.harvest.objects.player.Player;

import java.util.Random;

/**
 * @author: Lordmau5
 * @time: 11.06.2014 - 16:09
 */
public class ClientPlayer extends Player {

    public enum Facing {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    public enum State {
        STANDING,
        LEFT_FOOT,
        STANDING2,
        RIGHT_FOOT
    }

    boolean isWalking = false;
    boolean isRunning = false;
    int state = State.STANDING.ordinal();
    Facing facing = Facing.DOWN;
    String username;

    public Texture texture;

    public ClientPlayer() {
        this("User-" + new Random().nextInt(1000) + 1);
    }

    public ClientPlayer(String username) {
        super();
        this.username = username;
        texture = new Texture("player", true);
    }

    public String getUsername() {
        return this.username;
    }

    public Sprite getProperSprite() {
        String id = "player_";
        if(isRunning && isWalking) {
            id = id + "RUN_";
        }
        return isWalking ? texture.getSprite(id + facing.toString() + "_" + state) : texture.getSprite("player_" + facing.toString() + "_" + State.STANDING.ordinal());
    }

    public void render() {
        if(isWalking) {
            state += 1;
            if(state >= State.values().length)
                state = 0;
        }
    }

    public void faceTowards(Facing facing) {
        this.facing = facing;
    }

    public void updateWalking(boolean isWalking) {
        this.isWalking = isWalking;
    }

    public void updateRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}
