package remy.pouzet.moodtracker.controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity

{
    private ConstraintLayout mContraintLayout;
    private ImageView mSmiley;
    private ImageView mComment;
    private ImageView mHistoric;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContraintLayout = findViewById(R.id.constraintLayout);
        mSmiley = findViewById(R.id.smileyView);
        mComment = findViewById(R.id.commentLogoView);
        mHistoric = findViewById(R.id.historicAcessLogoView);

        mComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                builder.setView(R.layout.comment_alert_dialog);

               builder.setTitle("Commentaire");

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        // Display Mood management
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
        // END Display Mood management

    }
}