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

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Current state
    private String state = "X";
    //Count moves
    private int moveCount = 0;
    //easy, hard, multiplayer
    private String mode;

    //Create 2d array
    private String[][] board = new String[][]{
        { "Blank", "Blank", "Blank" },
        { "Blank", "Blank", "Blank" },
        { "Blank", "Blank", "Blank" }
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

        mode = getIntent().getStringExtra("mode");
        //Set board
        if (savedInstanceState != null){
            for (int x = 0; x < 3; x++) {
                String[] row = savedInstanceState.getStringArray("board" + x);
                for (int y = 0; y < row.length; y++) {
                    board[x][y] = row[y];
                }
            }
        }
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

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        for (int i = 0; i < board.length; i++) {
            savedInstanceState.putStringArray("board" + i, board[i]);
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
        text.setText("Player 1 is aan zet.");
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
               board[x][y] = "Blank";
            }
        }
    }

    //Player Move
    public void setChar(View v){
        TextView text = (TextView) findViewById(R.id.lblPlayer);
        if(state == "X") {
            setX(v.getId());
            text.setText("Player 1 is aan zet.");
        } else if (state == "O"){
            setO(v.getId());
            text.setText("Player 2 is aan zet.");
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
        if(board[xloc][yloc] == "Blank"){
            setChar(button);
        } else{
            randomMove(v);
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

    //Check Vertical, Check Horizontal, Check diogo signal, check anti diogo signal, check draw
    public boolean checkWin(int x, int y, String state){
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
            playerWins("Blank");
            return true;
        }
        return false;
    }

    //Change state of current user
    public void changeState(View v){
        if(state == "X"){
            state = "O";
            setAiMove(v);
        } else if (state ==  "O"){
            state = "X";
        } else {
            System.out.println("Fout! geen state bekend");
        }


    }

    public void setAiMove(View v){
        if(mode.equalsIgnoreCase("singleplayer1")){
            randomMove(v);
        }
        if(mode.equalsIgnoreCase("singleplayer2")){
            System.out.println("Hard bot is nu aan zet.");
        }
    }

    //End of game
    public void playerWins(String state){
        //Diable buttons
        for (int i = 1; i < 10; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            Button button = (Button) findViewById(id);
            button.setEnabled(false);
        }

        //Show Message
        if(state == "Blank"){
            TextView text = (TextView) findViewById(R.id.lblPlayer);
            text.setText("Its a draw. Press restart game to play again.");
        } else{
            TextView text = (TextView) findViewById(R.id.lblPlayer);
            text.setText(state + " wins. Press restart game to play again.");
        }
    }

    //Check if 2d array is filled correctly.
    public void setBoard(){
        showBoard();
        int number = 1;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                int id = getResources().getIdentifier("button"+number, "id", getPackageName());
                if(board[x][y] == "X"){
                    setX(id);
                }else if(board[x][y] == "O") {
                    setO(id);
                } else{
                    //Niks
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

}
