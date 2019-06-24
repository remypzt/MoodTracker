package remy.pouzet.moodtracker;

import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity

{
    private LinearLayout mBackground;
    private ImageView mBackgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackground = findViewById(R.id.activity_main_background);

       /* mBackground.setBackgroundColor(getResources().getColor(R.color.light_sage));*/

        mBackgroundImage = findViewById(R.id.imageView);

       mBackgroundImage.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeBottom() {

                setContentView(R.layout.activity_main_1);
            }

        });

    }

}