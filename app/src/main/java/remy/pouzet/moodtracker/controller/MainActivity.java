package remy.pouzet.moodtracker.controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity

{
    public static final String PREF_KEY_COMMENT = "PREF_KEY_COMMENT";
    public static final String PREF_KEY_COUNTER = "PREF_KEY_COUNTER2";

    private ConstraintLayout mContraintLayout;
    private ImageView mSmiley;
    private ImageView mComment;
    private ImageView mHistoric;
    private EditText mCommentInput;
    private SharedPreferences mPreferences;

    int counter = 0;

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

        //Counter management
        mPreferences = getSharedPreferences(PREF_KEY_COUNTER, MODE_PRIVATE);
        int previousCounter = mPreferences.getInt(PREF_KEY_COUNTER, 0);

        if (mPreferences != null)
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
                    counter --;
                    mSmiley.setImageResource(arraySmileys[counter]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));


                } else
                {
                    counter = 4;
                    mSmiley.setImageResource(arraySmileys[counter]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));
                }
            }

            public void onSwipeBottom()
            {
                if (counter < 4)
                {
                    counter ++;
                    mSmiley.setImageResource(arraySmileys[counter]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));

                } else
                {
                    counter = 0;
                    mSmiley.setImageResource(arraySmileys[counter]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));
                }
            }
            // TODO : save mood :
            // - at 2359 get counter and save it in arraylist ,
            // in historicActivity define int=mood=display
        });
        //END\\ Display Mood and swipe management


        // Comment button management

        // TODO : fix mCommentInput bug
        //
        //  (cause I had made alertdialog XML Layout, mCommentInput ID belong to another XML than mainactivity XML)
        //  after this line there is solution (even i'm not sure to understand everything, inflate is for fragment?)
        //  who's working
        //  but create an another problem( java.lang.IllegalStateException: You need to use a Theme.AppCompat
        //  theme (or descendant) with this activity.

        //AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        //
        //                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE );
        //                View dialogView = inflater.inflate(R.layout.comment_alert_dialog, null);
        //
        //                builder.setView(dialogView);
        //
        //                mCommentInput = (EditText)  dialogView.findViewById(R.id.comment);

        mComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setView(R.layout.comment_alert_dialog);

                mCommentInput = (EditText) findViewById(R.id.comment);

                builder.setTitle("Commentaire");




                // positive button : Validation and save comment management
                // TODO : thinking about day time management (must have only one comment per day)
                //      - create new one ArrayList comment in HistoricActivity and adding inside it
                //      the last comment saved in commentlist2
                //      - comment in commentList2 must be remove at every ending day

                builder.setPositiveButton("VALIDER", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String userComment = mCommentInput.getText().toString();

                        System.out.println(mCommentInput);

                        // User clicked OK button
                        if (userComment != null)
                        {

                            mPreferences = getSharedPreferences(PREF_KEY_COMMENT, MODE_PRIVATE);
                            String fromJsonCommentList = mPreferences.getString(PREF_KEY_COMMENT, null);

                            Gson gson = new Gson();
                            ArrayList<String> commentList2 = gson.fromJson(fromJsonCommentList, new TypeToken<ArrayList<String>>()
                            {
                            }.getType());

                            if (null == fromJsonCommentList)
                            {
                                ArrayList<String> commentList = new ArrayList();
                                commentList.add(userComment);

                                Gson gson2 = new Gson();
                                String jsonCommentList = gson2.toJson(commentList);

                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putString(PREF_KEY_COMMENT, jsonCommentList).apply();

                            } else
                            {
                                commentList2.remove(0);
                                commentList2.add(userComment);

                                Gson gson3 = new Gson();
                                String commentList = gson3.toJson(commentList2);

                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putString(PREF_KEY_COMMENT, commentList).apply();

                            }
                        }
                    }
                });
                //END\\ positive button : Validation and save comment management


                builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User cancelled the dialog : do nothing and quit the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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

        // Counter saving

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(PREF_KEY_COUNTER, counter).apply();
        //END\\ Counter saving
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        // Counter saving

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(PREF_KEY_COUNTER, counter).apply();
        //END\\ Counter saving
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


}