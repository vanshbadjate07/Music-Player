package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    int[] songIds = {R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4, R.raw.song5, R.raw.song6};
    int[] songViewIds = {R.id.song1, R.id.song2, R.id.song3, R.id.song4, R.id.song5, R.id.song6};
    MediaPlayer[] players = new MediaPlayer[6];
    int currentSongIndex = -1;

    RelativeLayout[] songLayouts;
    TextView songName;
    ImageView play, pause;
    SeekBar seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songLayouts = new RelativeLayout[songViewIds.length];
        for (int i = 0; i < songViewIds.length; i++) {
            songLayouts[i] = findViewById(songViewIds[i]);
        }

        songName = findViewById(R.id.song_name);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        seek = findViewById(R.id.seek);

        for (int i = 0; i < players.length; i++) {
            players[i] = MediaPlayer.create(this, songIds[i]);
        }

        for (int i = 0; i < songLayouts.length; i++) {
            final int index = i;
            songLayouts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSong(index);
                }
            });
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSongIndex != -1) {
                    players[currentSongIndex].start();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSongIndex != -1) {
                    players[currentSongIndex].pause();
                }
            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (currentSongIndex != -1 && fromUser) {
                    players[currentSongIndex].seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentSongIndex != -1 && players[currentSongIndex].isPlaying()) {
                    seek.setProgress(players[currentSongIndex].getCurrentPosition());
                }
            }
        }, 0, 900);
    }

    private void playSong(int index) {
        if (currentSongIndex != -1) {
            players[currentSongIndex].pause();
        }

        players[index].start();
        currentSongIndex = index;

        String[] songNames = getResources().getStringArray(R.array.song_names);
        songName.setText(songNames[index]);

        seek.setMax(players[index].getDuration());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (MediaPlayer player : players) {
            if (player != null) {
                player.release();
            }
        }
    }
}
