package com.example.music;


import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    //private Button b1 forward,b2 pause(startBtn in here),b3 playing(back)forward in here,b4 rewind;
    private ImageView favoriteBtn, previousBtn, playBtn, nextBtn, rewindBtn;
    private ImageView songPic;

    private MediaPlayer mediaPlayer;
    //  private Runnable runnable;
    private AudioManager audioManager;

    // for seek bar
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;


    private SeekBar seekbar;
    private TextView songTitle, singerName, songFullTime, songCurrentTime;

    int currentIndex = 0;

    public static int oneTimeOnly = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (ImageView) findViewById(R.id.start_btn);
        previousBtn = (ImageView) findViewById(R.id.previous_btn);
        nextBtn = (ImageView) findViewById(R.id.next_btn);
        rewindBtn = (ImageView) findViewById(R.id.repeat_btn);
        songPic = (ImageView) findViewById(R.id.song_pic);
        songTitle = (TextView) findViewById(R.id.song_title);
        singerName = (TextView) findViewById(R.id.singer_name);
        seekbar = (SeekBar) findViewById(R.id.seek_bar);
        songFullTime = (TextView) findViewById(R.id.song_time);
        songCurrentTime = (TextView) findViewById(R.id.song_current_time);



        //creating an array List to store songs

        ArrayList<Integer> songs = new ArrayList<>();
        //pass the index number and the song name with (directory R.raw)
        songs.add(0, R.raw.merry);
        songs.add(1, R.raw.calling);
        songs.add(2, R.raw.freestyle);
        songs.add(3, R.raw.prblms);
        songs.add(4, R.raw.pretty);

        //initialize the mediaPlayer and pass context,arraylist.get(currentIndex)
        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));




//PlayButton handler

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playBtn.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    playBtn.setImageResource(R.drawable.pause);
                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    songFullTime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    finalTime)))
                    );

                    songCurrentTime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );

                    seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);
                }


                songName();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    playBtn.setImageResource(R.drawable.pause);
                }
                //check arraylist and play e next song
                if (currentIndex < songs.size() - 1) {

                    currentIndex++;
                } else {
                    currentIndex = 0;
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mediaPlayer.start();
                //calling song name method
                songName();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    playBtn.setImageResource(R.drawable.pause);
                }
                if (currentIndex > 0) {
                    currentIndex--;

                } else {
                    currentIndex = songs.size() - 1;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mediaPlayer.start();
                songName();
            }
        });

    }

//method to get the song name,singer name and the pic of a song

    private void songName() {
        if (currentIndex == 0) {
            songTitle.setText("Merry");
            singerName.setText("Marawan Pablo");
            songPic.setImageResource(R.drawable.marawan);
        }
        if (currentIndex == 1) {
            songTitle.setText("Calling My Phone");
            singerName.setText("Lil Tjay");
            songPic.setImageResource(R.drawable.calling);

        }
        if (currentIndex == 2) {
            songTitle.setText("FreeStyle");
            singerName.setText("Tory Lanez");
            songPic.setImageResource(R.drawable.tory);

        }
        if (currentIndex == 3) {
            songTitle.setText("Prblms");
            singerName.setText("6lack");
            songPic.setImageResource(R.drawable.prblms);

        }
        if (currentIndex == 4) {
            songTitle.setText("Pretty Little Fears");
            singerName.setText("6lack ft Jcole");
            songPic.setImageResource(R.drawable.pretty);

        }


    }
    private    Runnable  UpdateSongTime = new Runnable() {
        public void run() {


            startTime = mediaPlayer.getCurrentPosition();

            songCurrentTime.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };


}

