package com.example.lobo.radiomickyandonie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import dyanamitechetan.vusikview.VusikView;
import library.radiomickyandonie.ChannelFactory;
import library.radiomickyandonie.IChannel;
import library.radiomickyandonie.Player;
import library.radiomickyandonie.SetupChannel;

import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.LibVLC;

import java.util.Hashtable;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivity activity;
    private ChannelFactory factory;
    private IChannel channelEnglish;
    private IChannel channelSpanish;
    private Hashtable mediaDictionary;
    private VusikView vusikView;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseFirestore db;
    private List<DocumentSnapshot> usersOnline;
    private TextView counterUsersOnline;
    private List<DocumentSnapshot> allUsers;
    private TextView counterAllUsers;

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

        CollectionReference users = db.collection("users");
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null) {
                    Log.w("TAG", "Listen fail.", e);
                    return;
                }

                if(documentSnapshots != null) {
                    allUsers = documentSnapshots.getDocuments();
                    String counter = String.valueOf(allUsers.size());
                    counterAllUsers.setText("Usuarios Registrados: " + counter);
                    Log.d("TAG", "All users");
                } else {
                    Log.d("TAG", "Users null");
                }
            }
        });

        Query query = users.whereEqualTo("online", true);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null) {
                    Log.w("TAG", "Listen fail.", e);
                    return;
                }

                if(documentSnapshots != null) {
                    usersOnline = documentSnapshots.getDocuments();
                    String counter = String.valueOf(usersOnline.size());
                    counterUsersOnline.setText("Usuarios en linea: " + counter);
                    Log.d("TAG", "Users online");
                } else {
                    Log.d("TAG", "Users null");
                }
            }
        });
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
                FirebaseUser currentUser = auth.getCurrentUser();
                db.collection("users").document(currentUser.getEmail())
                        .update("online", false);
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
        db = FirebaseFirestore.getInstance();

        Toolbar myToolbar = findViewById(R.id.toolbar_main);
        myToolbar.setTitle(auth.getCurrentUser().getDisplayName());
        setSupportActionBar(myToolbar);

        counterUsersOnline = findViewById(R.id.users_online);
        counterAllUsers = findViewById(R.id.all_users);

        vusikView = findViewById(R.id.musicView);
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
        mediaDictionary = Player.getInstance(activity);
        factory = SetupChannel.getFactory(mediaDictionary);
        channelEnglish = factory.getCreateChannel("english");
        channelSpanish = factory.getCreateChannel("spanish");

        final ImageButton playOrPause = findViewById(R.id.action_play_or_pause);
        playOrPause.setEnabled(false);
        playOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = (MediaPlayer) mediaDictionary.get("player");
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playOrPause.setImageResource(R.mipmap.ic_play);
                } else {
                    mediaPlayer.play();
                    playOrPause.setImageResource(R.mipmap.ic_pause);
                }
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_english:
                        channelEnglish.prepare();
                        channelEnglish.start();
                        playOrPause.setImageResource(R.mipmap.ic_pause);
                        Toast.makeText(MainActivity.this, "MI MÚSICA DEL SIGLO XX", Toast.LENGTH_SHORT).show();
                        break;
                    /*case R.id.action_spanish:
                        channelSpanish.prepare();
                        channelSpanish.start();
                        playOrPause.setImageResource(R.mipmap.ic_pause);
                        Toast.makeText(MainActivity.this, "Éxitos en Español", Toast.LENGTH_SHORT).show();
                        break;*/
                }
                MediaPlayer mediaPlayer = (MediaPlayer) mediaDictionary.get("player");
                if(mediaPlayer.getMedia() != null){
                    playOrPause.setEnabled(true);
                }
                return true;
            }
        });

    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser currentUser = auth.getCurrentUser();
        db.collection("users").document(currentUser.getEmail())
                .update("online", false);
        Log.d("TAG", "cerro hp");

    }*/

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            db.collection("users").document(currentUser.getEmail())
                    .update("online", false);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            db.collection("users").document(currentUser.getEmail())
                    .update("online", true);
        }
    }
}
