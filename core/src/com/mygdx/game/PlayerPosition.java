package com.mygdx.game;

import java.io.Serializable;

public class PlayerPosition implements Serializable {
    public int id;
    public float x;
    public float y;

    // Default constructor for serialization
    public PlayerPosition() {}

    public PlayerPosition(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
