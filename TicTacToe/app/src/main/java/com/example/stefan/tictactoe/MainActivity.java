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

public class MainActivity extends AppCompatActivity {
    private int player = 0;

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
    }

    public void setChar(View v){
        TextView text = (TextView) findViewById(R.id.lblPlayer);
        if(player == 0){
            text.setText("Player 2 is aan zet.");
            setX(v.getId());
            player = 1;
        } else{
            text.setText("Player 1 is aan zet.");
            setO(v.getId());
            player = 0;
        }
        //checkWin(v.getId());
    }

    public void checkWin(int position){
        //if 1 until 3
        for (int i = 1; i <= 3; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            Button button = (Button) findViewById(id);
            String value = button.getText().toString();
        }
    }

    public void setX(int id){
        Button button = (Button)findViewById(id);
        button.setText("X");
        button.setEnabled(false);
    }

    public void setO(int id){
        Button button = (Button)findViewById(id);
        button.setText("O");
        button.setEnabled(false);
    }
}
