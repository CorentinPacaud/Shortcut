package fr.corentinpacaud.shortcut;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaPlayer mpintro;

        mpintro = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/01 - Loud Like Love.mp3"));
        mpintro.setLooping(true);
        //  mpintro.start();

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/01 - Loud Like Love.mp3"));

        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/Music/01 - Loud Like Love.mp3");
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            byte[] data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Log.d("Caca", "bitrate : " + mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
        Log.d("Caca", "duration (ms): " + mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Log.d("Caca", "hasAudio : " + mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO));
    }


  /*  private byte[] getDatas(FileInputStream fis){
        try {
            byte[] bytes = new byte[fis.available()];
            BufferedInputStream buf = new BufferedInputStream(fis);
            buf.read(bytes, 0, bytes.length);
            buf.close();
            return buf.
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
}
