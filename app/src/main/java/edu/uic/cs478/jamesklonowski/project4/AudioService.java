package edu.uic.cs478.jamesklonowski.project4;

import android.app.Service;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private IBinder audioBinder = new MusicBinder();
    private MediaPlayer mediaPlayer;
    private long songNumber;
    private int songPosition = 0;
    private int mStartID;
    //private TransactionDataSource dataSource;
    private MySQLiteHelper mySQLiteHelper;
    private TransactionDataSource dataSource;


    public AudioService() {
    }

    public void onCreate() {
        super.onCreate();
        //System.out.println("AudioService onCreate...");
        songNumber = 0;
        //create the mediaplayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        //mediaPlayer = MediaPlayer.create(this, R.raw.ashortsong);

        //create and open the transaction database
        mySQLiteHelper = new MySQLiteHelper(this);

        dataSource = new TransactionDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*mySQLiteHelper = new MySQLiteHelper(this);
        dataSource = new TransactionDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }


    public void onDestroy(){
        //System.out.println("DELETING DATABASE!");
        dataSource.deleteDatabase();
        //dataSource = null;
    }

    //Set the song the user selected
    public void setSong(long songID) {
        songNumber = songID;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return audioBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }


    //returns a string which indicates the players status during a transaction
    private String getStatus(int song){
        String status = "";
        if(mediaPlayer.isPlaying()){
            status+= "PLAYING CLIP NUMBER "+song;
        }else {
            status+= "PAUSED/STOPPED WHILE PLAYING CLIP NUMBER "+song;
        }
        return status;
    }

    //Update the database with a new transaction
    private void updateDatabase(int clipnumber, String type, String status){
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()).toString();
        dataSource.createTransaction(datetime, clipnumber, type, status);
    }

    //User clicked the play button
    public void playSong(int songnum){
        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), (int)songNumber);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
        //song = songnum;
        updateDatabase(songnum, "PLAY", getStatus(songnum));
    }

    //User clicked the view transactions button.  I probably should have done this in the client app, using AIDL
    public void viewTransactions(){
        Intent intent = new Intent(this, TransactionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //User clicked the clear transactions button
    public void clearTransactions(){
        dataSource.clearTransactions();
    }

    //User clicked the pause button
    public void pauseSong(int songnum){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            songPosition = mediaPlayer.getCurrentPosition();
            updateDatabase(songnum, "PAUSE", getStatus(songnum));
        }
    }

    //User clicked the resume button
    public void resumeSong(int songnum){
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(songPosition);
            mediaPlayer.start();
            updateDatabase(songnum, "RESUME", getStatus(songnum));
        }
    }

    //User clicked the stop button
    public void stopPlayer(int songnum){
        mediaPlayer.pause();
        mediaPlayer.stop();
        updateDatabase(songnum, "STOP", getStatus(songnum));
        stopSelf();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // This doesn't work... should instead be broadcasting a message that the playerclient handles and shows a toast
        Toast.makeText(getApplicationContext(), "Song Finished Playing", Toast.LENGTH_LONG).show();
        //System.out.println("Toast should have shown...");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startid){
        if(null != mediaPlayer){
            mStartID = startid;
            if(mediaPlayer.isPlaying()){
                mediaPlayer.seekTo(0);
            }else{
                mediaPlayer.start();
            }
        }
        return START_NOT_STICKY;
    }*/

    public class MusicBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }


}