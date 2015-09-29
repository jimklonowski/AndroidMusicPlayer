package edu.uic.cs478.jamesklonowski.project4;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class PlayerClient extends Activity {
    private AudioService audioService;
    private Intent playIntent;
    private boolean isBound = false;
    private ArrayList<Integer> songs = new ArrayList<Integer>();
    private ArrayList<String> names = new ArrayList<String>();
    private int currentSong = 1;
    private EditText editText;
    private ServiceConnection audioConnection;
    private ListView songList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_client);
        //get all songs from the raw folder
        getAllSongs();
        //set the song input box
        editText = (EditText)findViewById(R.id.song_number_input);
        //display the songs from the raw directory in a listview in case user wants to select a song that way
        songList = (ListView)findViewById(R.id.listView);
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position+1;
                editText.setText(Integer.toString(pos));
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.songtext, names);
        songList.setAdapter(adapter);
    }

    //Get all songs from raw folder and put them in the songs arraylist
    private void getAllSongs(){
        final Class<R.raw> c = R.raw.class;
        final Field[] fields = c.getDeclaredFields();
        for(int i=0, max = fields.length; i<max; i++){
            final int resID;
            try{
                resID = fields[i].getInt(null);
                songs.add(resID);
                names.add((i+1)+".  "+fields[i].getName());
              //  System.out.println("Added: " + fields[i].getName() + " to the names list.");
            }catch(Exception e){ continue; }
        }
    }

    //Make the service connection
    private void makeConnection(){
        audioConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                AudioService.MusicBinder binder = (AudioService.MusicBinder)service;
                audioService = binder.getService();
                audioService.setSong(songs.get(currentSong-1));
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };
    }

    //User clicked the play button
    public void playClick(View view){
        if(playIntent == null){
            onStart();
        }
        if(editText.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter a track number!", Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(editText.getText().toString()) > songs.size()){
            Toast.makeText(this, "Track number out of bounds.", Toast.LENGTH_SHORT).show();
        }
        else {
            currentSong = Integer.parseInt(editText.getText().toString());
            audioService.setSong(songs.get(currentSong - 1));
            audioService.playSong(currentSong);
        }
    }

    //User clicked the stop button
    public void stopClick(View view){
        stopService(playIntent);
//        unbindService(audioConnection);
        audioService.stopPlayer(currentSong);
        //audioService = null;
    }

    //User wants to view transactions
    public void transactionsClick(View view){
        audioService.viewTransactions();
//        startActivity(new Intent(this, TransactionActivity.class));
    }

    //User clicked the pause button
    public void pauseClick(View view){
        audioService.pauseSong(currentSong);
    }

    //User wants to clear the transaction database
    public void clearTransactionsClick(View view){
        audioService.clearTransactions();
        Toast.makeText(this, "Cleared transaction log.", Toast.LENGTH_SHORT).show();
    }

    //User clicked the resume button
    public void resumeClick(View view){
        audioService.resumeSong(currentSong);
    }

    //App starting, create a playintent then bind and start service
    @Override
    protected void onStart(){
        super.onStart();
        makeConnection();
        if(playIntent == null){
            playIntent = new Intent(this, AudioService.class);
            bindService(playIntent, audioConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            //System.out.println("Service should have started now!");
        }
    }

    /*
    @Override
    protected void onStop(){
        if(playIntent != null){
            unbindService(playClick(););
        }
    }

    @Override
    protected void onDestroy(){
        stopService(playIntent);
        audioService = null;
        super.onDestroy();
    }*/
}
