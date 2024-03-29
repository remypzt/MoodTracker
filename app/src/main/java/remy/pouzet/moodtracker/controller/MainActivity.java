package remy.pouzet.moodtracker.controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.Mood;

public class MainActivity extends AppCompatActivity
{
    public static final String PREF_KEY_COUNTER = "PREF_KEY_COUNTER2";
    public static final String PREF_KEY_DATE = "PREF_KEY_DATE";
    public static final String PREF_KEY_MOOD = "PREF_KEY_MOOD";

    int[] arraySmileys = new int[]{
            R.mipmap.smiley_happy,
            R.mipmap.smiley_normal,
            R.mipmap.smiley_disappointed,
            R.mipmap.smiley_sad,
            R.mipmap.smiley_super_happy,
    };
    int[] arrayBackgroundColors = new int[]{
            R.color.light_sage,
            R.color.cornflower_blue_65,
            R.color.warm_grey,
            R.color.faded_red,
            R.color.banana_yellow,
    };

    int counter = 0;
    final int millisecondsNumberInADay = 86400000;
    long previousDate;
    String fromJsonMoods;
    Gson gson = new Gson();
    ArrayList<Mood> moods;

    private ConstraintLayout mContraintLayout;
    private ImageView mSmiley;
    private String userComment;
    private SharedPreferences mPreferences;
    private Mood mMood;

    @SuppressLint({"ClickableViewAccessibility", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Date now = new Date();
        long mDate = now.getTime() / millisecondsNumberInADay;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContraintLayout = findViewById(R.id.constraintLayout);
        mSmiley = findViewById(R.id.smileyView);

        sharingMood();

        //mMood Management
        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        counter = mPreferences.getInt(PREF_KEY_COUNTER, 0);
        mMood = new Mood(counter, userComment, mDate);
        fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());
        //END\| mMood Management

        swipe();
        comment();
        historic();
    }

    private void comment()
    {
        ImageView comment = findViewById(R.id.commentLogoView);
        // Comment button management
        comment.setOnClickListener(new View.OnClickListener()
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
                                EditText mCommentInput = ((AlertDialog) dialog).findViewById(R.id.comment);
                                assert mCommentInput != null;
                                String userCommentInput = mCommentInput.getText().toString();
                                if (userCommentInput.length() != 0)
                                {
                                    userComment = userCommentInput;
                                    mMood.setComment(userComment);
                                    checkDate();
                                    Toast.makeText(MainActivity.this, "Votre commentaire a été enregistré", Toast.LENGTH_SHORT).show();
                                } else
                                {
                                    Toast.makeText(MainActivity.this, "Votre commentaire ne doit pas être vide", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        //END\\ positive button : Validation and save comment management
                        .setNegativeButton("ANNULER", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                            }
                        })
                        .show();
            }
        });
        //END\\ Comment button management
    }

    @SuppressLint("ClickableViewAccessibility")
    private void swipe()
    { // Display Mood and swipe management
        displayingBehavior();
        mContraintLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this)
        {
            public void onSwipeTop()
            {
                if (counter > 0)
                {
                    counter--;
                    displayingBehavior();
                } else
                {
                    counter = 4;
                    displayingBehavior();
                }
                mMood.setComment(null);
                swipeBehavior();
            }
            public void onSwipeBottom()
            {
                if (counter < 4)
                {
                    counter++;
                    displayingBehavior();
                } else
                {
                    counter = 0;
                    displayingBehavior();
                }
                mMood.setComment(null);
                swipeBehavior();
            }
        });
        //END\\ Display Mood and swipe management
    }

    private void sharingMood()
    {
        //get and compress Smiley for sharing mood
        mSmiley.setDrawingCacheEnabled(true);
        Bitmap bitmap = mSmiley.getDrawingCache();
        File root = Environment.getExternalStorageDirectory();
        final File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
        try
        {
            cachePath.createNewFile();
            FileOutputStream ostream = new FileOutputStream(cachePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //END\|get and compress Smiley for sharing mood

        //Share button
        Button mButton = findViewById(R.id.buttonshare);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My mood");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        //END\| Share button
    }

    private void swipeBehavior()
    {
        mPreferences.edit().putInt(PREF_KEY_COUNTER, counter).apply();
        mMood.setCounter(counter);
        MediaPlayer mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.clak);
        mMediaPlayer.start();
        checkDate();
    }

    private void checkDate()
    {
        Date now = new Date();
        long mDate = now.getTime() / millisecondsNumberInADay;

        fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        previousDate = mPreferences.getLong(PREF_KEY_DATE, 0);
        moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());

        if (previousDate == 0)
        {
            previousDate = mDate;
        }
        if (previousDate == mDate)
        {
            if (null != fromJsonMoods)
            {
                moods.remove(moods.size() - 1);
                mMood.setDate(mDate);
                moods.add(mMood);
                String jsonMoods = gson.toJson(moods);
                mPreferences.edit().putString(PREF_KEY_MOOD, jsonMoods).apply();
            } else
            {
                ArrayList<Mood> moods1 = new ArrayList<>();
                mMood.setDate(mDate);
                moods1.add(mMood);
                String jsonMoods = gson.toJson(moods1);
                mPreferences.edit().putString(PREF_KEY_MOOD, jsonMoods).apply();
            }
            if (userComment != null)
            {
                Toast.makeText(MainActivity.this, "Votre  précédent commentaire a été effacé", Toast.LENGTH_SHORT).show();
            }
        } else
        {
            if (moods.size() < 7)
            {
                saveMood();
            } else  // moods max size = 7
            {
                moods.remove(0);
                saveMood();
            }//END\| moods max size = 7
            counter = 0;
            mPreferences.edit().putInt(PREF_KEY_COUNTER, counter).apply();
        }
        mMood.setDate(mDate);
        mPreferences.edit().putLong(PREF_KEY_DATE, mDate).apply();
    }

    private void saveMood()
    {
        Date now = new Date();
        long mDate = now.getTime() / millisecondsNumberInADay;
        mMood.setDate(mDate);
        moods.add(mMood);
        String jsonMoods = gson.toJson(moods);
        mPreferences.edit().putString(PREF_KEY_MOOD, jsonMoods).apply();
    }

    private void displayingBehavior()
    {
        mSmiley.setImageResource(arraySmileys[counter]);
        mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));
    }

    private void historic()
    {
        // Historic button management
        ImageView historic = findViewById(R.id.historicAcessLogoView);
        historic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
                if (null == fromJsonMoods)
                {
                    Toast.makeText(MainActivity.this, "Aucunes humeur n'a encore été enregistrée", Toast.LENGTH_SHORT).show();
                } else
                {
                    Intent HistoricActivity = new Intent(MainActivity.this, HistoricActivity.class);
                    startActivity(HistoricActivity);
                }
            }

        });
        //END\\ Historic button management
    }
}