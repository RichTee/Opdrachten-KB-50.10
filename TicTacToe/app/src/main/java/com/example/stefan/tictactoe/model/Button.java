package com.example.stefan.tictactoe.model;

/**
 * Created by Stefan on 2015-11-24.
 */
public class Button {

    enum State{Blank, X, O}
    int x;
    int y;
    State state;

    public Button(int x, int y, State state){
        this.x = x;
        this.y = y;
        this.state = state;
    }
}
