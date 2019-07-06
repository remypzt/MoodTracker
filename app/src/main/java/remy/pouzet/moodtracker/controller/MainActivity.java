package remy.pouzet.moodtracker.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.OnSwipeTouchListener;
import remy.pouzet.moodtracker.model.Mood;

public class MainActivity extends AppCompatActivity

{
    public static final String PREF_KEY_COMMENT = "PREF_KEY_COMMENT";
    public static final String PREF_KEY_COUNTER = "PREF_KEY_COUNTER2";

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

        mMood = new Mood(counter, userComment, mDate);

        //Date management
        Date now = new Date();

        DateFormat dateformatter = DateFormat.getDateInstance(DateFormat.SHORT);
        String mDate = dateformatter.format(now);

        mMood.setDate(mDate);

        System.out.println(mDate);
        //END\\ Date management

        //Counter management,
        mPreferences = getSharedPreferences(PREF_KEY_COUNTER, MODE_PRIVATE);
        int previousCounter = mPreferences.getInt(PREF_KEY_COUNTER, 0);

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
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                //Inflating ... ?
                Context context = builder.getContext();
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.comment_alert_dialog, null, false);
                //END\\//Inflating ... ?

                builder.setView(R.layout.comment_alert_dialog);

                mCommentInput = (EditText) findViewById(R.id.comment);

                builder.setTitle("Commentaire");

                mCommentInput.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {

                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {
                        Toast.makeText(MainActivity.this, "3" +mCommentInput.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                });


                // positive button : Validation and save comment management
                // Comment management : if there is already an comment , replace it by this one,
                // else create ArrayListComment
                builder.setPositiveButton("VALIDER", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String userComment = mCommentInput.getText().toString();
                        if (userComment != null)
                        {

                            mPreferences = getSharedPreferences(PREF_KEY_COMMENT, MODE_PRIVATE);
                            String fromJsonCommentList = mPreferences.getString(PREF_KEY_COMMENT, null);

                            if (null == fromJsonCommentList)
                            {
                                ArrayList<String> commentList = new ArrayList();
                                commentList.add(userComment);

                                Gson gson2 = new Gson();
                                String jsonCommentList = gson2.toJson(commentList);

                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putString(PREF_KEY_COMMENT, jsonCommentList).apply();

                                Toast.makeText(MainActivity.this, "1" +mCommentInput.getText().toString(), Toast.LENGTH_LONG).show();

                            } else
                            {
                                Gson gson = new Gson();
                                ArrayList<String> commentList2 = gson.fromJson(fromJsonCommentList, new TypeToken<ArrayList<String>>()
                                {
                                }.getType());

                                commentList2.remove(0);
                                commentList2.add(userComment);

                                Gson gson3 = new Gson();
                                String jsonCommentList = gson3.toJson(commentList2);

                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putString(PREF_KEY_COMMENT, jsonCommentList).apply();

                                Toast.makeText(MainActivity.this, "2" + mCommentInput.getText().toString(), Toast.LENGTH_LONG).show();

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
    }
}