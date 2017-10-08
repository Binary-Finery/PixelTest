package spencerstudios.com.pixeltest;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class TestActivity extends AppCompatActivity {

    private final String[] HEX_COLORS = {"#F44336", "#4CAF50", "#2196F3", "#FFEB3B", "#9C27B0", "#FF9800", "#9E9E9E", "#E91E63", "#3F51B5", "#009688", "#FFC107", "#795548", "#673AB7", "#607D8B", "#FFFFFF", "#000000", "#FFFFFF"};

    private boolean isAuto = true, endTest = false;
    private int counter = 0, max = HEX_COLORS.length - 2, TRANSITION_DELAY;

    private Handler handler1, handler2;
    private Runnable runnable;

    private LinearLayout rootLayout;

    private ArgbEvaluator evaluator = new ArgbEvaluator();
    private ValueAnimator animator = new ValueAnimator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_test);

        Intent fetchData = getIntent();
        isAuto = fetchData.getBooleanExtra("auto_mode", true);
        if (isAuto) TRANSITION_DELAY = (fetchData.getIntExtra("delay", 5)) * 1000;

        handler1 = new Handler();

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        rootLayout = (LinearLayout) findViewById(R.id.root);
        rootLayout.setBackgroundColor(Color.parseColor(HEX_COLORS[0]));
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAuto) {
                    endTest = true;
                    terminateActivity();
                } else {
                    performColorAnimationTransition();
                    if (counter == max) terminateActivity();
                    else counter++;
                }
            }
        });

        if (isAuto) {
            handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!endTest) initAutoMode();
                }
            }, TRANSITION_DELAY);
        }
    }

    private void initAutoMode() {
        runnable = new Runnable() {
            @Override
            public void run() {

                performColorAnimationTransition();

                if (!endTest && counter != max-1) {
                    handler1.postDelayed(this, TRANSITION_DELAY);
                    counter++;
                } else {
                    handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            terminateActivity();
                        }
                    }, TRANSITION_DELAY);
                }
            }
        };
        handler1.post(runnable);
    }

    private void performColorAnimationTransition() {
        animator.setIntValues(Color.parseColor(HEX_COLORS[counter]), Color.parseColor(HEX_COLORS[counter + 1]));
        animator.setEvaluator(evaluator);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rootLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    private void terminateActivity() {
        handler1.removeCallbacks(runnable);
        finish();
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
    }
}
