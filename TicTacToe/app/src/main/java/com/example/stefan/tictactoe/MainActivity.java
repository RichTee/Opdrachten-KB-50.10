package com.example.stefan.tictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //Current state
    private CELL_STATE state = CELL_STATE.X;
    //Count moves
    private int moveCount = 0;
    //Easy(SP), Hard(SP), MultiPlayer
    private String mode;

    //Create 2d array
    private CELL_STATE[][] board = new CELL_STATE[][]{
            //  0                   1               2
        { CELL_STATE.BLANK, CELL_STATE.BLANK, CELL_STATE.BLANK },   // 0
        { CELL_STATE.BLANK, CELL_STATE.BLANK, CELL_STATE.BLANK },   // 1
        { CELL_STATE.BLANK, CELL_STATE.BLANK, CELL_STATE.BLANK }    // 2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mode = getIntent().getStringExtra("mode");
        //Set board
        if (savedInstanceState != null){
            for (int x = 0; x < board[0].length; x++) {
                CELL_STATE[] row = (CELL_STATE[]) savedInstanceState.getSerializable("board" + x);
                for (int y = 0; y < board.length; y++) {
                    board[x][y] = row[y];
                }
            }
        }
        setBoard();

        //hacky TODO: Naam gelijk goed weergeven bij startup zonder code.
        TextView text = (TextView) findViewById(R.id.lblPlayer);
        text.setText(String.format(getResources().getString(R.string.player_turn), 1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        for (int i = 0; i < board.length; i++) {
            savedInstanceState.putSerializable("board" + i, board[i]);
        }
    }

    //Reset Buttons, movecount, Current player and board.
    public void reset(View v){
        //Set all buttons empty
        for (int i = 1; i < 10; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            Button button = (Button) findViewById(id);
            button.setText("");
            button.setEnabled(true);
        }
        //Set move to player 1
        TextView text = (TextView) findViewById(R.id.lblPlayer);
        text.setText(String.format(getResources().getString(R.string.player_turn), 1));
        //Move count 0
        moveCount = 0;

        //clear board.
        clearBoard();
    }

    //Back button
    public void menu(View v){
        finish();
    }

    //Restart board
    public void clearBoard(){
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
               board[x][y] = CELL_STATE.BLANK;
            }
        }
    }

    //Player Move
    public void setChar(View v){
        TextView text = (TextView) findViewById(R.id.lblPlayer);
        if(state == CELL_STATE.X) {
            setX(v.getId());
            text.setText(String.format(getResources().getString(R.string.player_turn), 1));
        } else if (state == CELL_STATE.O){
            setO(v.getId());
            text.setText(String.format(getResources().getString(R.string.player_turn), 2));
        } else{
            System.out.println("Geen state bekend.");
        }
        if(!fillMove(v)) {
            changeState(v);
        }
        moveCount++;
    }

    //BOT Move
    public void randomMove(View v){
        Random rn = new Random();
        int nummer = rn.nextInt(9)+1;
        int id = getResources().getIdentifier("button"+nummer, "id", getPackageName());
        Button button = (Button)findViewById(id);
        //Check state is blank
        int xloc = 0;
        int yloc = 0;
        switch (button.getId()) {
            case R.id.button1:
                xloc = 0; yloc = 0; break;
            case R.id.button2:
                xloc = 0; yloc = 1; break;
            case R.id.button3:
                xloc = 0; yloc = 2; break;
            case R.id.button4:
                xloc = 1; yloc = 0; break;
            case R.id.button5:
                xloc = 1; yloc = 1; break;
            case R.id.button6:
                xloc = 1; yloc = 2; break;
            case R.id.button7:
                xloc = 2; yloc = 0; break;
            case R.id.button8:
                xloc = 2; yloc = 1; break;
            case R.id.button9:
                xloc = 2; yloc = 2; break;
        }
        if(board[xloc][yloc] == CELL_STATE.BLANK){
            setChar(button);
        } else{
            randomMove(v);
        }
    }

    // BOT Calculated Move
    private void calculatedMove(View v) {
        // TODO: Check middle, Check Corner, Check Block
        int min = 0;
        int max = board.length;

        if(checkAiWinCondition())
            return;

        if(checkMid())
            return;

        // Check block before checking corners
        //if(checkBlock())
        //    return;

        if(checkCorners())
            return;

        randomMove(v);
    }

    // Check win condition for Smart Bot AI
    private boolean checkAiWinCondition() {
        // TODO: Have the AI chase a win if he has 2 in a row.
        return false;
    }

    // Check middle for Smart Bot AI
    private boolean checkMid() {
        if(board[1][1] == CELL_STATE.BLANK) {
            setChar(findViewById(R.id.button5)); // 5 is mid, should find with specified enum name instead
            return true;
        }
        return false;
    }

    // Check block for Smart Bot AI
    private boolean checkBlock() {
        // TODO: Have the AI block if the player has a possible win condition.
        return true;
    }

    // Check corners for Smart Bot AI
    private boolean checkCorners() {
        // Check corners
        //      top-left                            top-right                           bottom-left                         bottom-right
        if(board[0][0] == CELL_STATE.BLANK || board[0][2] == CELL_STATE.BLANK || board[2][0] == CELL_STATE.BLANK || board[2][2] == CELL_STATE.BLANK){
            Random rand = new Random();
            int xLoc = 0, yLoc = 0;
            boolean recheck = true;
            while(recheck) {
                int number = rand.nextInt(4); // 0 - 3
                // Usage of a map with KeyValuePair to have a pre-determined name and
                // location would be better for expansion and maintainability.
                switch (number) {
                    case 0:
                        if (board[xLoc][yLoc] == CELL_STATE.BLANK) {
                            setChar(findViewById(R.id.button1));
                            recheck = false;
                            return true;
                        }
                    case 1:
                        if(board[xLoc][yLoc + 2] == CELL_STATE.BLANK) {
                            setChar(findViewById(R.id.button3));
                            recheck = false;
                            return true;
                        }
                    case 2:
                        if(board[xLoc + 2][yLoc] == CELL_STATE.BLANK) {
                            setChar(findViewById(R.id.button7));
                            recheck = false;
                            return true;
                        }
                    case 3:
                        if(board[xLoc + 2][yLoc + 2] == CELL_STATE.BLANK) {
                            setChar(findViewById(R.id.button9));
                            recheck = false;
                            return true;
                        }
                    default:
                        break; // Something gone wrong
                }
            }
        }

        return false;
    }

    //Set x in button
    public void setX(int id){
        Button button = (Button)findViewById(id);
        button.setText("X");
        button.setEnabled(false);
    }

    //Set O in button
    public void setO(int id){
        Button button = (Button)findViewById(id);
        button.setText("O");
        button.setEnabled(false);
    }

    //Fill board with chosen position
    public boolean fillMove(View v){
        int xloc = 0;
        int yloc = 0;
        //Fill 2d array
        switch (v.getId()) {
            case R.id.button1:
                xloc = 0; yloc = 0; break;
            case R.id.button2:
                xloc = 0; yloc = 1; break;
            case R.id.button3:
                xloc = 0; yloc = 2; break;
            case R.id.button4:
                xloc = 1; yloc = 0; break;
            case R.id.button5:
                xloc = 1; yloc = 1; break;
            case R.id.button6:
                xloc = 1; yloc = 2; break;
            case R.id.button7:
                xloc = 2; yloc = 0; break;
            case R.id.button8:
                xloc = 2; yloc = 1; break;
            case R.id.button9:
                xloc = 2; yloc = 2; break;
        }
        board[xloc][yloc] = state;
        if(checkWin(xloc, yloc, state)) return true;
        else return false;
    }

    //Check Vertical, Check Horizontal, Check diag signal, check anti diag signal, check draw
    public boolean checkWin(int x, int y, CELL_STATE state){
        //check Vertical
        for(int i = 0; i < board.length; i++){
            if(board[x][i] != state)
                break;
            if(i == board.length-1){
                playerWins(state);
                return true;
            }
        }

        //check Horizontal
        for(int i = 0; i < board.length; i++){
            if(board[i][y] != state)
                break;
            if(i == board.length-1){
                playerWins(state);
                return true;
            }
        }

        //check diag
        if(x == y){
            for(int i = 0; i < board.length; i++){
                if(board[i][i] != state)
                    break;
                if(i == board.length-1){
                    playerWins(state);
                    return true;
                }
            }
        }

        //check anti diag
        for(int i = 0;i<board.length;i++){
            if(board[i][(board.length-1)-i] != state)
                break;
            if(i == board.length-1){
                playerWins(state);
                return true;
            }
        }

        //check draw
        if(moveCount == ((board.length*board[0].length) -1)){
            playerWins(CELL_STATE.BLANK);
            return true;
        }
        return false;
    }

    //Change state of current user
    public void changeState(View v){
        if(state == CELL_STATE.X){
            state = CELL_STATE.O;
            setAiMove(v);
        } else if (state ==  CELL_STATE.O){
            state = CELL_STATE.X;
        } else {
            System.out.println("Fout! geen state bekend");
        }


    }

    public void setAiMove(View v){
        TextView text = (TextView) findViewById(R.id.lblPlayer);
        if(mode.equalsIgnoreCase("singleplayer1")){
            randomMove(v);
            text.setText(String.format(getResources().getString(R.string.player_turn), 1));
        }
        if(mode.equalsIgnoreCase("singleplayer2")){
            System.out.println("Hard bot is nu aan zet.");
            calculatedMove(v);
            text.setText(String.format(getResources().getString(R.string.player_turn), 1));
        }
    }

    //End of game
    public void playerWins(CELL_STATE state){
        //Disable buttons
        for (int i = 1; i < 10; i++) {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            Button button = (Button) findViewById(id);
            button.setEnabled(false);
        }

        //Show Message
        if(state == CELL_STATE.BLANK){
            TextView text = (TextView) findViewById(R.id.lblPlayer);
            text.setText(String.format(getResources().getString(R.string.draw)));
        } else{
            TextView text = (TextView) findViewById(R.id.lblPlayer);
            text.setText(String.format(getResources().getString(R.string.won), state.toString())); // Only works for player.
            System.out.println("Reached");
        }
    }

    //Check if 2d array is filled correctly.
    public void setBoard(){
        showBoard();
        int number = 1;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                int id = getResources().getIdentifier("button"+number, "id", getPackageName());
                if(board[x][y] == CELL_STATE.X){
                    setX(id);
                }else if(board[x][y] == CELL_STATE.O) {
                    setO(id);
                } else{
                    //Niks (TODO: Handle exception if here, otherwise remove else statement if impossible)
                }
                number++;
            }
        }
    }

    //Check if 2d array is filled correctly.
    public void showBoard(){
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                System.out.println("x=" + x + " y=" + y + " state="+board[x][y]);
            }
        }
    }

    private enum GAME_MODE { SP_EASY, SP_HARD, MULTIPLAYER;}

    private enum CELL_STATE {
        BLANK,
        X,
        O;      // Not a number
    }
}
