package com.example.lobo.radiomickyandonie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import dyanamitechetan.vusikview.VusikView;
import library.radiomickyandonie.ChannelFactory;
import library.radiomickyandonie.IChannel;
import library.radiomickyandonie.SetupChannel;

import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.LibVLC;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {
    private MainActivity activity;
    ChannelFactory factory;
    IChannel channelEnglish;
    IChannel channelSpanish;
    Hashtable mediaDictionary;
    VusikView vusikView;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onStart() {
        auth.addAuthStateListener(authListener);
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                MediaPlayer mediaPlayer = (MediaPlayer) mediaDictionary.get("player");
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                auth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(auth.getCurrentUser().getDisplayName());
        setSupportActionBar(myToolbar);

        vusikView = (VusikView) findViewById(R.id.musicView);
        vusikView.start();
        vusikView.startNotesFall();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, Login.class));
                }
            }
        };

        activity = this;
        mediaDictionary = GetMediaDictionary(activity);
        factory = SetupChannel.GetFactory(mediaDictionary);
        channelEnglish = factory.CreateChannel("english");
        channelSpanish = factory.CreateChannel("spanish");

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_english:
                        channelEnglish.prepare();
                        channelEnglish.start();
                        Toast.makeText(MainActivity.this, "Clásicos en Ingles", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_spanish:
                        channelSpanish.prepare();
                        channelSpanish.start();
                        Toast.makeText(MainActivity.this, "Clásicos en Español", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private Hashtable GetMediaDictionary(final MainActivity activity) {
        final LibVLC libVLC = new LibVLC(activity);
        final MediaPlayer mediaPlayer = new MediaPlayer(libVLC);
        return new Hashtable<String, Object>(){{
           put("libVLC", libVLC);
           put("player", mediaPlayer);
        }};
    }
}
