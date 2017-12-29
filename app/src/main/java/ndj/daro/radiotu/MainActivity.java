package ndj.daro.radiotu;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private MediaPlayer player;
    private String url = "http://server.ohradio.com.ar:9478";
    private ImageButton btnPlay;
    private boolean isPlaying = false;
    private ImageSwitcher imageSwitcher;
    private int[] gallery = {R.drawable.img1, R.drawable.img2, R.drawable.img2, R.drawable.img3, R.drawable.img3, R.drawable.img1};
    private int pos;
    private Timer timer = null;
    private static final Integer DURATION = 3500;
    private ImageButton btnTwitter;
    private ImageButton btnFacebook;
    private ImageButton btnInstagram;
    private ImageButton btnWhatsapp;
    private Button btnProgramacion;
    private ImageButton btnTu;
    private TextView txtStatus;
    private TextView txtRadio;
    private RelativeLayout relative;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = new MediaPlayer();
        btnPlay = findViewById(R.id.btnPlay);
        btnFacebook = findViewById(R.id.facebook);
        btnInstagram = findViewById(R.id.instagram);
        btnTwitter = findViewById(R.id.twitter);
        btnWhatsapp = findViewById(R.id.whatsapp);
        btnTu = findViewById(R.id.tulogo);
        txtStatus = findViewById(R.id.txtStatus);
        txtRadio = findViewById(R.id.txtRadio);
        relative = findViewById(R.id.mainLayout);


        setAnimation();
        startSlider();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStreaming();
            }
        });

//        btnProgramacion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent viewIntent = new Intent(MainActivity.this, ProgramacionActivity.class);
//
//                startActivity(viewIntent);
//
//            }
//        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.facebook.com/somosradiotu"));
                startActivity(viewIntent);
            }
        });

        btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.instagram.com/somosradiotu/"));
                startActivity(viewIntent);
            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://twitter.com/somosradiotu"));
                startActivity(viewIntent);
            }
        });

        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://api.whatsapp.com/send?phone=+5491159909078"));
                startActivity(viewIntent);
            }
        });

        btnTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.radiotu.com.ar"));
                startActivity(viewIntent);
            }
        });

    }
    private void startStreaming(){
        if (!player.isPlaying() && !isPlaying) {
            try {

                Toast.makeText(getApplicationContext(),
                        "Cargando...",
                        Toast.LENGTH_LONG).show();
                player.reset();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource("http://198.7.61.133:9478");
                player.prepareAsync();
                txtStatus.setText("Conectando a la Radio...");
            } catch (IllegalArgumentException | SecurityException
                    | IllegalStateException | IOException e) {
                Toast.makeText(getApplicationContext(),
                        "Error al conectar con la radio", Toast.LENGTH_LONG).show();
            }
            player.setOnPreparedListener(new OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {

                    player.start();
                    isPlaying = true;
                    btnPlay.setImageResource(R.drawable.pausebutton);
                    txtStatus.setText("En Vivo ");
                    txtRadio.setText(" Radio TU!");

                }
            });
        }
        else if (!player.isPlaying() && isPlaying) {
            player.start();
            btnPlay.setImageResource(R.drawable.pausebutton);
        }
        else {
            player.pause();
            btnPlay.setImageResource(R.drawable.playbutton);
        }
    }
    private void setAnimation() {
        imageSwitcher = findViewById(R.id.imgSwitcher);
        imageSwitcher.setFactory(new ViewFactory() {

            public View makeView() {
                return new ImageView(MainActivity.this);
            }
        });
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);
    }
    private void startSlider() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        imageSwitcher.setImageResource(gallery[pos]);
                        pos++;
                        if (pos == gallery.length) {
                            pos = 0;
                        }
                    }
                });
            }

        }, 1000, DURATION);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
        if (player.isPlaying() && isPlaying) {
            player.reset();
            player.stop();
            btnPlay.setImageResource(R.drawable.playbutton);
            isPlaying = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            startSlider();
        }
    }
}
