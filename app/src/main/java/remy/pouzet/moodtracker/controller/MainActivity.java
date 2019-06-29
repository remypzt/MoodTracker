package remy.pouzet.moodtracker.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity

{
    private ConstraintLayout mContraintLayout;
    private ImageView mSmiley;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*import remy.pouzet.moodtracker.model.MoodDisplay;*/
        /* mBackgroundColorFaded= findViewById(R.id.imageView2);*/
        /* mSmileyGoodHumor = findViewById(R.id.imageView3);*/
        /*mBackgroundColorFaded.setBackgroundColor(Color.BLACK);*/
        /*mSmileyGoodHumor.setImageResource(R.mipmap.smiley_happy);*/
         /*mSmiley.setImageResource(R.mipmap.smiley_sad);
        mBackgroundColor.setBackgroundColor(Color.CYAN);*/
        /*ArrayList<ImageView> smileyList = new ArrayList<>();
        smileyList.add(mSmiley);
        smileyList.add(mSmileyGoodHumor);*/
        /*MoodDisplay goodhumor = new MoodDisplay( mBackgroundColorFaded, mSmileyGoodHumor);
        MoodDisplay badhumor = new MoodDisplay( mBackgroundColor, mSmiley);
        ArrayList<MoodDisplay> humorList = new ArrayList();
        humorList.add(goodhumor);
        humorList.add(badhumor);
        humorList.get(1);*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContraintLayout = findViewById(R.id.constraintLayout);
        mSmiley = findViewById(R.id.imageView3);

        final int[] counter = {0};

        final int[] arraySmileys = new int[]{
                R.mipmap.smiley_super_happy,
                R.mipmap.smiley_happy,
                R.mipmap.smiley_normal,
                R.mipmap.smiley_disappointed,
                R.mipmap.smiley_sad,
        };

        final int[] arrayBackgroundColors = new int[]{
                R.color.banana_yellow,
                R.color.light_sage,
                R.color.cornflower_blue_65,
                R.color.warm_grey,
                R.color.faded_red,
        };


        mContraintLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this)
        {
            public void onSwipeTop()
            {
                if (counter[0] > 0)
                {
                    counter[0]--;
                    mSmiley.setImageResource(arraySmileys[counter[0]]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter[0]]));


                } else
                {
                    counter[0] = 4;
                    mSmiley.setImageResource(arraySmileys[counter[0]]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter[0]]));
                }
            }

            public void onSwipeBottom()
            {
                if (counter[0] < 4)
                {
                    counter[0]++;
                    mSmiley.setImageResource(arraySmileys[counter[0]]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter[0]]));

                } else
                {
                    counter[0] = 0;
                    mSmiley.setImageResource(arraySmileys[counter[0]]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter[0]]));
                }
            }
        });
    }
}