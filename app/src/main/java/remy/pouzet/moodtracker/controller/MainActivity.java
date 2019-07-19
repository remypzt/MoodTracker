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
    public static final String PREF_KEY_INDEX = "PREF_KEY_INDEX";

    int counter = 0;
    int mIndex = 0;

    private ConstraintLayout mContraintLayout;
    private ImageView mSmiley;
    private String userComment;
    private String mDate;
    private SharedPreferences mPreferences;
    /*private MediaPlayer mMediaPlayer;*/
    private Mood mMood;

    @SuppressLint({"ClickableViewAccessibility", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //mPreferences management
        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        int previousCounter = mPreferences.getInt(PREF_KEY_COUNTER, 0);
        String previousDate = mPreferences.getString(PREF_KEY_DATE, null);
        final String[] previousUserComment = {mPreferences.getString(PREF_KEY_COMMENT, null)};
        int previousIndex = mPreferences.getInt(PREF_KEY_INDEX, 0);
        //END\|mPreferences management

        //mMood Management
        counter = previousCounter;
        userComment = previousUserComment[0];
        mIndex = previousIndex;
        mMood = new Mood(counter, userComment, mDate, mIndex);
        //END\| mMood Management

        //Date management : if it's new day, then save previous mood
        Date now = new Date();
        DateFormat dateformatter = DateFormat.getDateInstance(DateFormat.LONG);
        final String mDate = dateformatter.format(now);
        if (!mDate.equals(previousDate))
        {
            String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
            Gson gson1 = new Gson();
            ArrayList<Mood> moods = gson1.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
            {
            }.getType());

            if (null == fromJsonMoods)
            {
                ArrayList<Mood> moods1 = new ArrayList<>();

                mIndex = 0;
                SharedPreferences.Editor editor1 = mPreferences.edit();
                editor1.putInt(PREF_KEY_INDEX, mIndex).apply();
                mMood.setIndex(mIndex);
                mMood.setDate(mDate);

                moods1.add(mMood);
                Gson gson = new Gson();
                String jsonMoods = gson.toJson(moods1);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(PREF_KEY_MOOD, jsonMoods).apply();
            } else if (moods.size() != 7)
            {
                mIndex = moods.size() - 1;
                SharedPreferences.Editor editor1 = mPreferences.edit();
                editor1.putInt(PREF_KEY_INDEX, mIndex).apply();
                mMood.setIndex(mIndex);
                mMood.setDate(mDate);

                moods.add(mMood);
                Gson gson = new Gson();
                String jsonMoods = gson.toJson(moods);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(PREF_KEY_MOOD, jsonMoods).apply();
            } else // moods max size = 7
            {
                moods.remove(0);
                mIndex = 0;
                SharedPreferences.Editor editor1 = mPreferences.edit();
                editor1.putInt(PREF_KEY_INDEX, mIndex).apply();
                mMood.setIndex(mIndex);
                mMood.setDate(mDate);
                moods.add(mMood);
                Gson gson = new Gson();
                String jsonMoods = gson.toJson(moods);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(PREF_KEY_MOOD, jsonMoods).apply();
            }//END\| moods max size = 7
            counter = 0;
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(PREF_KEY_COUNTER, counter).apply();
            previousUserComment[0] = null;
        }
        mMood.setDate(mDate);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_KEY_DATE, mDate).apply();
        //END\\ Date management : if it's new day, then save previous mood


        // Sound
        final MediaPlayer mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.clak);

        /*String url = "http://s1download-universal-soundbank.com/mp3/sounds/13971.mp3";
        final MediaPlayer mMediaPlayer= new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try
        {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            mMediaPlayer.prepare();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        mMediaPlayer.start();*/

        /*try
        {
            String url = "http://s1download-universal-soundbank.com/mp3/sounds/13971.mp3";
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/
        //END\| Sound

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
                mMediaPlayer.start();
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
                mMediaPlayer.start();
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
}