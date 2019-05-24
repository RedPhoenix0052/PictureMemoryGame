package matchpicturegameactivity.rcpl.com.matchpicturegameactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.media.MediaExtractor.MetricsConstants.FORMAT;

public class BeginGame extends AppCompatActivity {

    ImageView imageView;
    TextView textView2,textView3;
    Button match,start,not_match;
    CountDownTimer countDownTimer;
    boolean isCountdownRunning=false;
    Random num = new Random();
    int score = 0;
    int[] imgID = {101,109,553,227,105,753};
    int[] img = {R.drawable.circle,R.drawable.hexagon,R.drawable.pentagon,R.drawable.square,R.drawable.star,R.drawable.triangle};
    int prevID = imgID[0];
    int nextID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_game);
        this.setFinishOnTouchOutside(false);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        imageView = findViewById(R.id.imageView);
        start = findViewById(R.id.start);
        match = findViewById(R.id.match);
        not_match = findViewById(R.id.notmatch);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.GONE);
                match.setVisibility(View.VISIBLE);
                not_match.setVisibility(View.VISIBLE);
                startTimer();
                matchImage();
            }
        });

    }


    public void matchImage(){
        int index = num.nextInt(6);
        nextID = imgID[index];
        imageView.setImageResource(img[index]);
        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prevID==nextID){
                    resultMatch();;
                }
                else
                    resultNotMatch();
            }
        });
        not_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prevID!=nextID){
                  resultMatch();
                }
                else
                    resultNotMatch();
            }
        });
    }


    //Result for Correct Match
    public void resultMatch(){
        score+=10;
        prevID=nextID;
        textView2.setText("Score: "+score);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        matchImage();
    }

    //Result for Incorrect Match
    public void resultNotMatch(){
        countDownTimer.cancel();
        AlertDialog.Builder dialog = new AlertDialog.Builder(BeginGame.this);
        dialog.setTitle("Game Over");
        dialog.setIcon(R.mipmap.skull);
        dialog.setMessage("Your Score: "+score);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetGame();
            }
        });
        dialog.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(BeginGame.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }

    //Reset Game
    public void resetGame(){
        score=0;
        textView2.setText("Score: "+score);
        match.setVisibility(View.GONE);
        not_match.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.circle);
        prevID=imgID[0];
        textView3.setVisibility(View.GONE);
    }


    //Timer
    public void startTimer(){
        textView3.setVisibility(View.VISIBLE);
        if(isCountdownRunning) {
            countDownTimer.cancel();
            countDown();
        }
        else
            countDown();
    }
    public void countDown() {
        countDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                isCountdownRunning=true;
                long minutes=l/60000;
                long seconds=l/1000;
                textView3.setText("Timer:  "+minutes+":"+seconds);
            }

            @Override
            public void onFinish() {
                isCountdownRunning=false;
                textView3.setText("!!!Time's Up!!!");
                resultNotMatch();
            }
        };
        countDownTimer.start();
    }


}
