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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity

{
    private ConstraintLayout mContraintLayout;
    private ImageView mSmiley;
    private ImageView mComment;
    private ImageView mHistoric;
    private EditText mCommentInput;
    private SharedPreferences mPreferences;

    public static final String PREF_KEY_COMMENT = "PREF_KEY_COMMENT";


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
        mCommentInput = findViewById(R.id.comment);

        mPreferences = getPreferences (MODE_PRIVATE);


        // Comment button management
        mComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setView(R.layout.comment_alert_dialog);

               builder.setTitle("Commentaire");

                AlertDialog dialog = builder.create();
                dialog.show();

                mCommentInput.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        /*POSITIVEBUTTONID.setEnabled(s.toString().length() != 0);*/
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                builder.setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User clicked OK button
                        String userComment = mCommentInput.getText().toString();

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
                            commentList2.add(userComment);

                            Gson gson3 = new Gson();
                            String commentList = gson3.toJson(commentList2);

                            SharedPreferences.Editor editor = mPreferences.edit();
                            editor.putString(PREF_KEY_COMMENT, commentList).apply();

                        }

                    }
                });
                builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog : do nothing and quit the dialog
                    }
                });


            }
        });
        // END Comment button management

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