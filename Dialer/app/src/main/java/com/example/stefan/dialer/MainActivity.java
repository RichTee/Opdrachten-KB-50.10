package com.example.stefan.dialer;

import android.content.Intent;
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

    public void onClick(View v){
        Button b = (Button)v;
        String buttonText = b.getText().toString();

        TextView lbl = (TextView) findViewById(R.id.lblNumber);
        lbl.append(buttonText);
    }

    public void remove(View v){
        TextView lbl = (TextView) findViewById(R.id.lblNumber);
        String number = lbl.getText().toString();
        if(number != null && !number.equals("")){
            lbl.setText(number.substring(0, number.length() - 1));
        }
    }

    public void showCountry(View v){
        //Get telephone number out of lblNumber.
        TextView lbl = (TextView) findViewById(R.id.lblNumber);
        String number = lbl.getText().toString();

        Intent i = new Intent(this, SecondActivity.class);
        i.putExtra("number", number);
        startActivityForResult(i, 1);
    }

    public void onActivityResult(int requestCode, Intent data)
    {
        TextView lbl = (TextView) findViewById(R.id.lblNumber);
        lbl.setText(data.getData().toString());
    }

}
