package remy.pouzet.moodtracker;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity

{
    private LinearLayout mBackground;
    private ImageView mBackgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        mBackground = findViewById(R.id.activity_main_background) ;
        setContentView(R.layout.activity_main);


       /* ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomerPageAdaptater(this));


        mBackground = findViewById(R.id.activity_main_background);

       /* mBackground.setBackgroundColor(getResources().getColor(R.color.light_sage));*/

       mBackgroundImage = findViewById(R.id.imageView);

       mBackgroundImage.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                /*mBackground.setBackground;*/

            }
            public void onSwipeBottom() {

                /*setContentView(R.layout.activity_main_1);*/

            }

        });
       /*if Content View (or mbackground  or mBackground image 2 )= color1 then if OnSwipeTop then color 2 and if Onswipebottom then color 1 etc ()*/
       {}
    }
    /* custompage adaptater//
    public enum CustomPagerEnum
    {

        RED(R.string.app_name, R.layout.activity_main_1);


        private int mTitleResId;
        private int mLayoutResId;

        CustomPagerEnum(int titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }*/

}