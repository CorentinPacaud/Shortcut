package fr.corentinpacaud.shortcut;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private CircleBarView mCircleBarView;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*MediaPlayer mpintro;

        mpintro = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/01 - Loud Like Love.mp3"));
        mpintro.setLooping(true);
        //  mpintro.start();

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/01 - Loud Like Love.mp3"));

        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/Music/01 - Loud Like Love.mp3");
*/
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mCircleBarView = (CircleBarView) findViewById(R.id.circleBarView);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleAnimation();
            }
        });

    }

    private void toggleAnimation() {
        if (mCircleBarView.isPlaying) {
            mCircleBarView.stop();
            mFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        } else {
            mCircleBarView.play();
            mFab.setImageResource(R.drawable.ic_pause_white_24dp);
        }
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
