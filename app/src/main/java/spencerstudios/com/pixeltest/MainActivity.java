package spencerstudios.com.pixeltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class MainActivity extends AppCompatActivity {

    private TextView tvDelay;
    private LinearLayout seekBarContainer;

    private int tracker = 5;
    private boolean isAuto = true;

    private Animation fadeInAnim, fadeOutAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDelay = (TextView) findViewById(R.id.tv_delay);
        Button btnStart = (Button) findViewById(R.id.btn_start);
        AppCompatSeekBar seekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);
        seekBarContainer = (LinearLayout) findViewById(R.id.seek_bar_container);

        final StickySwitch stickySwitch = (StickySwitch) findViewById(R.id.sticky_switch);

        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_activity);
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out_activity);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireTestActivity();
            }
        });

        stickySwitch.setDirection(StickySwitch.Direction.RIGHT);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                isAuto = text.equalsIgnoreCase("Auto");
                seekBarContainer.startAnimation(isAuto ? fadeInAnim : fadeOutAnim);
                seekBarContainer.setVisibility(isAuto ? View.VISIBLE : View.INVISIBLE);
            }
        });

        seekBar.setMax(24);
        seekBar.setProgress(5);
        tvDelay.setText(getResources().getString(R.string.default_delay));

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tracker = progress == 0 ? 1 : progress + 1;
                tvDelay.setText(String.format(Locale.getDefault(), "%d seconds", tracker));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void fireTestActivity() {
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        intent.putExtra("auto_mode", isAuto);
        if (isAuto) intent.putExtra("delay", tracker);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
    }
}
