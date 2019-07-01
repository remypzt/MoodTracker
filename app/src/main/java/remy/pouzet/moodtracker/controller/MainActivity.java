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
    public static final String PREF_KEY_COUNTER = "PREF_KEY_COUNTER";

    private ConstraintLayout mContraintLayout;
    private ImageView mSmiley;
    private ImageView mComment;
    private ImageView mHistoric;
    private EditText mCommentInput;
    private SharedPreferences mPreferences;


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
        Integer[] counter = {0};

        mPreferences = getSharedPreferences(PREF_KEY_COUNTER, MODE_PRIVATE);
        String fromJsonCounter = mPreferences.getString(PREF_KEY_COUNTER, null);

        Gson gson5 = new Gson();
        final Integer[] lastCounter = gson5.fromJson(fromJsonCounter, new TypeToken<Integer>()
        {
        }.getType());

        if (fromJsonCounter != null)
        {
            counter = lastCounter;
        }

        final Integer[] finalCounter = counter;
        //END\\ Counter management


        // Display Mood and swipe management
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
                if (finalCounter[0] > 0)
                {
                    finalCounter[0]--;
                    mSmiley.setImageResource(arraySmileys[finalCounter[0]]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[finalCounter[0]]));


                } else
                {
                    finalCounter[0] = 4;
                    mSmiley.setImageResource(arraySmileys[finalCounter[0]]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[finalCounter[0]]));
                }
            }

            public void onSwipeBottom()
            {
                if (finalCounter[0] < 4)
                {
                    finalCounter[0]++;
                    mSmiley.setImageResource(arraySmileys[finalCounter[0]]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[finalCounter[0]]));

                } else
                {
                    finalCounter[0] = 0;
                    mSmiley.setImageResource(arraySmileys[finalCounter[0]]);
                    mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[finalCounter[0]]));
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
        //  after this line there is solution (even i'm not sure to understand everything)whose working
        //  but create an another problems( java.lang.IllegalStateException: You need to use a Theme.AppCompat
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
                //      the last comment saved in commetlist2
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

                mCommentInput.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        /*POSITIVEBUTTONID.setEnabled(s.toString().length() != 0);*/
                        //TODO POSITIVEBUTTONID.setDisable
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {

                    }
                });
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
        Gson gson4 = new Gson();
        String jsonCounter = gson4.toJson(counter);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_KEY_COUNTER, jsonCounter).apply();
        //END\\ Counter saving
    }
}