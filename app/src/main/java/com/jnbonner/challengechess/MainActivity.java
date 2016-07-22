package com.jnbonner.challengechess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button onePlayerButton;
    Button twoPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onePlayerButton = (Button)findViewById(R.id.onePlayerButton);

        onePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("isSingle", true);
                startGame(intent);
            }
        });

        twoPlayerButton = (Button)findViewById(R.id.twoPlayerButton);

        twoPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("isSingle", false);
                startGame(intent);
            }
        });
    }
    
    public void startGame(Intent intent){
        startActivity(intent);
    }

}
