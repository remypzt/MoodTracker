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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.Mood;
import remy.pouzet.moodtracker.model.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity
{
    public static final String PREF_KEY_COMMENT = "PREF_KEY_COMMENT";
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

    private ConstraintLayout mContraintLayout;
    private ImageView mSmiley;
    private String userComment;
    private String mDate;
    private SharedPreferences mPreferences;
    private Mood mMood;

    SharedPreferences.Editor editor;
    String previousDate;
    String previousUserComment;
    String fromJsonMoods;

    Gson gson = new Gson();
    ArrayList<Mood> moods;

    @SuppressLint({"ClickableViewAccessibility", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());

        //FID
        mContraintLayout = findViewById(R.id.constraintLayout);
        ImageView comment = findViewById(R.id.commentLogoView);
        ImageView historic = findViewById(R.id.historicAcessLogoView);
        Button mButton = findViewById(R.id.buttonshare);
        mSmiley = findViewById(R.id.smileyView);
        //END\|FID

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

        //mMood Management
        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        counter = mPreferences.getInt(PREF_KEY_COUNTER, 0);
        userComment = previousUserComment;
        mMood = new Mood(counter, userComment, mDate);
        editor = mPreferences.edit();
        previousDate = mPreferences.getString(PREF_KEY_DATE, null);
        previousUserComment = mPreferences.getString(PREF_KEY_COMMENT, null);
        fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        //END\| mMood Management

        //Date management : if it's new day, then save previous mood
        Date now = new Date();
        DateFormat dateformatter = DateFormat.getDateInstance(DateFormat.SHORT);
        final String mDate = dateformatter.format(now);
        //END\\ Date management : if it's new day, then save previous mood

        // Display Mood and swipe management
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
                swipeBehavior();
            }
        });
        //END\\ Display Mood and swipe management

        //Share button
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
                                String userComment = mCommentInput.getText().toString();
                                if (userComment.length() != 0)
                                {
                                    previousUserComment = userComment;
                                    editor.putString(PREF_KEY_COMMENT, previousUserComment).apply();
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
                            }
                        })
                        .show();
            }
        });
        //END\\ Comment button management

        // Historic button management
        historic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent HistoricActivity = new Intent(MainActivity.this, HistoricActivity.class);
                startActivity(HistoricActivity);
            }
        });
        //END\\ Historic button management
    }

    private void swipeBehavior()
    {
        Date now = new Date();
        DateFormat dateformatter = DateFormat.getDateInstance(DateFormat.SHORT);
        final String mDate = dateformatter.format(now);
        editor.putInt(PREF_KEY_COUNTER, counter).apply();
        mMood.setCounter(counter);
        MediaPlayer mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.clak);
        mMediaPlayer.start();
        checkDate();
    }

    private void checkDate()
    {
        if (!mDate.equals(previousDate))
        {
            if (null == fromJsonMoods)
            {
                ArrayList<Mood> moods1 = new ArrayList<>();
                mMood.setDate(mDate);
                moods1.add(mMood);
                String jsonMoods = gson.toJson(moods1);
                editor.putString(PREF_KEY_MOOD, jsonMoods).apply();
            } else if (moods.size() != 7)
            {
                saveMood();
            } else // moods max size = 7
            {
                moods.remove(0);
                saveMood();
            }//END\| moods max size = 7
            counter = 0;
            editor.putInt(PREF_KEY_COUNTER, counter).apply();
            previousUserComment = null;
        } else
        {
            if (null != fromJsonMoods)
            {
                int size = moods.size();
                moods.remove(size - 1);
                mMood.setDate(mDate);
                moods.add(mMood);
                String jsonMoods = gson.toJson(moods);
                editor.putString(PREF_KEY_MOOD, jsonMoods).apply();
            } else
            {
                ArrayList<Mood> moods1 = new ArrayList<>();
                mMood.setDate(mDate);
                moods1.add(mMood);
                String jsonMoods = gson.toJson(moods1);
                editor.putString(PREF_KEY_MOOD, jsonMoods).apply();
            }
        }
        mMood.setDate(mDate);
        editor.putString(PREF_KEY_DATE, mDate).apply();
    }

    private void saveMood()
    {
        mMood.setDate(mDate);
        moods.add(mMood);
        String jsonMoods = gson.toJson(moods);
        editor.putString(PREF_KEY_MOOD, jsonMoods).apply();
    }

    private void displayingBehavior()
    {
        mSmiley.setImageResource(arraySmileys[counter]);
        mContraintLayout.setBackgroundColor(getResources().getColor(arrayBackgroundColors[counter]));
    }
}