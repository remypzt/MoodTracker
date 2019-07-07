package remy.pouzet.moodtracker.controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.Mood;
import remy.pouzet.moodtracker.model.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity

{
    public static final String PREF_KEY_COMMENT = "PREF_KEY_COMMENT";
    public static final String PREF_KEY_COUNTER = "PREF_KEY_COUNTER2";
    public static final String PREF_KEY_DATE = "PREF_KEY_DATE";


    int counter = 0;

    private ConstraintLayout mContraintLayout;
    private ImageView mSmiley;
    private ImageView mComment;
    private ImageView mHistoric;
    private EditText mCommentInput;

    private String userComment;
    private String mDate;

    private SharedPreferences mPreferences;
    private Mood mMood;

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

        mPreferences = getPreferences(MODE_PRIVATE);

        mPreferences = getSharedPreferences(PREF_KEY_COUNTER, MODE_PRIVATE);
        int previousCounter = mPreferences.getInt(PREF_KEY_COUNTER, 0);

        mPreferences = getSharedPreferences(PREF_KEY_COMMENT, MODE_PRIVATE);
        final String[] previousUserComment = {mPreferences.getString(PREF_KEY_COMMENT, null)};

        //mMood Management
        mMood = new Mood(counter, userComment, mDate);

        //END\| mMood Management



        //Date management
        Date now = new Date();

        DateFormat dateformatter = DateFormat.getDateInstance(DateFormat.SHORT);
        String mDate = dateformatter.format(now);

        mMood.setDate(mDate);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_KEY_DATE, mDate).apply();
        //END\\ Date management

        //Counter management
        if (previousCounter != 0)
        {
            counter = previousCounter;
        }
        //END\\ Counter management


        // Display Mood and swipe management
        final int[] arraySmileys = new int[]{
                R.mipmap.smiley_happy,
                R.mipmap.smiley_normal,
                R.mipmap.smiley_disappointed,
                R.mipmap.smiley_sad,
                R.mipmap.smiley_super_happy,
        };

        final int[] arrayBackgroundColors = new int[]{
                R.color.light_sage,
                R.color.cornflower_blue_65,
                R.color.warm_grey,
                R.color.faded_red,
                R.color.banana_yellow,
        };

        mSmiley.setImageResource(arraySmileys[counter]);
        mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));

        mContraintLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this)
        {
            public void onSwipeTop()
            {
                if (counter > 0)
                {
                    counter--;
                    mSmiley.setImageResource(arraySmileys[counter]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));


                } else
                {
                    counter = 4;
                    mSmiley.setImageResource(arraySmileys[counter]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));
                }
                // Counter saving
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putInt(PREF_KEY_COUNTER, counter).apply();
                //END\\ Counter saving

                mMood.setCounter(counter);
            }

            public void onSwipeBottom()
            {
                if (counter < 4)
                {
                    counter++;
                    mSmiley.setImageResource(arraySmileys[counter]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));
                } else
                {
                    counter = 0;
                    mSmiley.setImageResource(arraySmileys[counter]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));
                }
                //Counter saving
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putInt(PREF_KEY_COUNTER, counter).apply();
                //END\\ Counter saving

                mMood.setCounter(counter);
            }
        });
        //END\\ Display Mood and swipe management


        // Comment button management
        // TODO : fix mCommentInput bug
        mComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                new AlertDialog.Builder(v.getContext())
                        .setView(R.layout.comment_alert_dialog)
                        .setTitle("Commentaire")
                        .setPositiveButton("VALIDER", new DialogInterface.OnClickListener()
                        {// positive button : Validation and save comment management
                            // Comment management : if there is already an comment , replace it by this one,
                            // else create ArrayListComment

                            public void onClick(DialogInterface dialog, int id)
                            {
                                EditText mCommentInput = (EditText) ((AlertDialog) dialog).findViewById(R.id.comment);
                                String userComment = mCommentInput.getText().toString();

                                if (userComment.length() != 0)
                                {

                                    previousUserComment[0] = userComment;

                                    SharedPreferences.Editor editor = mPreferences.edit();
                                    editor.putString(PREF_KEY_COMMENT, previousUserComment[0]).apply();

                                    mMood.setComment(userComment);
                                } else
                                    {
                                        Toast.makeText(MainActivity.this, "Votre commentaire ne doit pas être vide", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        //END\\ positive button : Validation and save comment management
                        .setNegativeButton("ANNULER", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                // User cancelled the dialog : do nothing and quit the dialog
                            }
                        })
                        .show();
            }
        });
        //END\\ Comment button management


        // Historic button management
        mHistoric.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                Intent historicActivity = new Intent(MainActivity.this, HistoricActivity.class);
                startActivity(historicActivity);
            }
        });
        //END\\ Historic button management
    }
}