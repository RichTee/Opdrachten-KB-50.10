package com.example.stefan.tictactoe;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {
    enum State{Blank, X, O}
    //Current state
    private State state = State.X;
    //Count moves
    private int moveCount = 0;

    //Create 2d array
    private State[][] board = new State[][]{
        { State.Blank, State.Blank, State.Blank },
        { State.Blank, State.Blank, State.Blank },
        { State.Blank, State.Blank, State.Blank }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Set board
        setBoard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        text.setText("Player 1 is aan zet.");
        //Move count 0
        moveCount = 0;

        //clear board.
        clearBoard();
    }

    public void clearBoard(){
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
               board[x][y] = State.Blank;
            }
        }
    }

    public void setChar(View v){
        int xloc = 0;
        int yloc = 0;

        TextView text = (TextView) findViewById(R.id.lblPlayer);
        if(state == State.X) {
            setX(v.getId());
            text.setText("Player 1 is aan zet.");
        } else if (state == State.O){
            setO(v.getId());
            text.setText("Player 2 is aan zet.");
        } else{
            System.out.println("Geen state bekend.");
        }

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
        checkWin(xloc, yloc, state);
        changeState();
        moveCount++;
    }


    //Check if 2d array is filled correctly.
    public void showBoard(){
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                System.out.println("x=" + x + " y=" + y + " state="+board[x][y]);
            }
        }
    }

    //Check if 2d array is filled correctly.
    public void setBoard(){
        System.out.println("Create board");
        showBoard();
        int number = 0;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if(board[x][y] == State.X){
                    setX(number);
                }else if(board[x][y] == State.O) {
                    setO(number);
                } else{
                    //Niks
                }
                number++;
            }
        }
    }

    //Check Vertical, Check Horizontal, Check diogo signal, check anti diogo signal, check draw
    public void checkWin(int x, int y, State state){
        //check Vertical
        for(int i = 0; i < board.length; i++){
            if(board[x][i] != state)
                break;
            if(i == board.length-1){
                playerWins(state);
            }
        }

        //check Horizontal
        for(int i = 0; i < board.length; i++){
            if(board[i][y] != state)
                break;
            if(i == board.length-1){
                playerWins(state);
            }
        }

        //check diag
        if(x == y){
            for(int i = 0; i < board.length; i++){
                if(board[i][i] != state)
                    break;
                if(i == board.length-1){
                    playerWins(state);
                }
            }
        }

        //check anti diag
        for(int i = 0;i<board.length;i++){
            if(board[i][(board.length-1)-i] != state)
                break;
            if(i == board.length-1){
                playerWins(state);
            }
        }

        //check draw
        if(moveCount == ((board.length*board[0].length) -1)){
            playerWins(State.Blank);
        }
    }

    //Change state of current user
    public void changeState(){
        if(state == State.X){
            state = State.O;
        } else if (state ==  State.O){
            state = State.X;
        } else {
            System.out.println("Fout! geen state bekend");
        }
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

    public void playerWins(State state){
        //Diable buttons
        for (int i = 1; i < 10; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            Button button = (Button) findViewById(id);
            button.setEnabled(false);
        }

        //Show Message
        if(state == State.Blank){
            TextView text = (TextView) findViewById(R.id.lblPlayer);
            text.setText("Its a draw. Press restart game to play again.");
        } else{
            TextView text = (TextView) findViewById(R.id.lblPlayer);
            text.setText(state + " wins. Press restart game to play again.");
        }
    }
}
