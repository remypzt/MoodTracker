package remy.pouzet.moodtracker.controller;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.Mood;

/**
 * Created by Remy Pouzet on 29/06/2019.
 */

public class HistoricActivity extends AppCompatActivity
{
    public static final String PREF_KEY_MOOD = "PREF_KEY_MOOD";
    final int today = 0;
    final int yesterday = -1;
    final int beforeYesterday = -2;
    final int aWeekAgo = -7;
    final int aMonthAgo = -32;
    TextView baner;
    TextView baner2;
    TextView baner3;
    TextView baner4;
    TextView baner5;
    TextView baner6;
    TextView baner7;
    //Util
    int a = 0;
    int centerRight = 21;
    Date now = new Date();
    long mDate = now.getTime() / 86400000;
    ArrayList<TextView> baners;
    //END Util
    private SharedPreferences mPreferences;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historic_activity);

        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        Gson gson = new Gson();
        String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        final ArrayList<Mood> moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());

        baner = findViewById(R.id.imageView);
        baner2 = findViewById(R.id.imageView2);
        baner3 = findViewById(R.id.imageView3);
        baner4 = findViewById(R.id.imageView4);
        baner5 = findViewById(R.id.imageView5);
        baner6 = findViewById(R.id.imageView6);
        baner7 = findViewById(R.id.imageView7);

        baners = new ArrayList<>();
        baners.add(baner);
        baners.add(baner2);
        baners.add(baner3);
        baners.add(baner4);
        baners.add(baner5);
        baners.add(baner6);
        baners.add(baner7);

        assert moods != null;
        while (a + 1 <= moods.size() || moods.size() > 8)
        {
            dispayingDateManagement();
            bannersBackgroundColorDisplayingManagement();
        }
        commentUserDisplayingManagement();
    }

    private void dispayingDateManagement()
    {
        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        Gson gson = new Gson();
        String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        final ArrayList<Mood> moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());

        if (moods.get(a) != null)
        {
            long longCompareCurrentDateToMoodDate = (moods.get(a).getDate() - mDate);
            int compareCurrentDateToMoodDate = (int) longCompareCurrentDateToMoodDate;

            String comparaisonResultatBetweenCurrentDateToMoodDate;

            switch (compareCurrentDateToMoodDate)
            {
                case today:
                    comparaisonResultatBetweenCurrentDateToMoodDate = "Aujourd'hui";
                    break;
                case yesterday:
                    comparaisonResultatBetweenCurrentDateToMoodDate = "Hier";
                    break;
                case beforeYesterday:
                    comparaisonResultatBetweenCurrentDateToMoodDate = "Avant-hier";
                    break;
                case aWeekAgo:
                    comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a une semaine";
                    break;
                default:
                    if (compareCurrentDateToMoodDate < beforeYesterday && compareCurrentDateToMoodDate > aWeekAgo)
                    {
                        comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a " + (compareCurrentDateToMoodDate - compareCurrentDateToMoodDate * 2) + " jours";
                    } else if (compareCurrentDateToMoodDate < aWeekAgo && compareCurrentDateToMoodDate > aMonthAgo)
                    {
                        comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a plus de 1 semaine";
                    } else
                    {
                        comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a plus de 1 mois";
                    }

            }

            baners.get(a).setText(comparaisonResultatBetweenCurrentDateToMoodDate);
        }
    }

    private void bannersBackgroundColorDisplayingManagement()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int fifthWidth = width / 5;
        int fourWidth = fifthWidth * 4;
        int threeWidth = fifthWidth * 3;
        int twoWidth = fifthWidth * 2;

        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        Gson gson = new Gson();
        String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        final ArrayList<Mood> moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());

        switch (moods.get(a).getCounter())
        {
            case 0:
                baners.get(a).setBackgroundColor(getResources().getColor(R.color.light_sage));
                baners.get(a).getLayoutParams().width = fourWidth;
                break;
            case 1:
                baners.get(a).setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                baners.get(a).getLayoutParams().width = threeWidth;
                break;
            case 2:
                baners.get(a).setBackgroundColor(getResources().getColor(R.color.warm_grey));
                baners.get(a).getLayoutParams().width = twoWidth;
                break;
            case 3:
                baners.get(a).setBackgroundColor(getResources().getColor(R.color.faded_red));
                baners.get(a).getLayoutParams().width = fifthWidth;
                break;
            default:
                baners.get(a).setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                baners.get(a).getLayoutParams().width = width;
        }
        a++;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void display(final int id)
    {
        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        Gson gson = new Gson();
        String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        final ArrayList<Mood> moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());

        if (moods.get(id).getComment() != null)
        {
            baners.get(id).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
            baners.get(id).setForegroundGravity(centerRight);

            baners.get(id).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    Toast.makeText(HistoricActivity.this, moods.get(id).getComment(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void commentUserDisplayingManagement()
    {
        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        Gson gson = new Gson();
        String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        final ArrayList<Mood> moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());

        if (moods.size() >= 1)
        {
            display(0);
            if (moods.size() >= 2)
            {
                display(1);
                if (moods.size() >= 3)
                {
                    display(2);
                    if (moods.size() >= 4)
                    {
                        display(3);
                        if (moods.size() >= 5)
                        {
                            display(4);
                            if (moods.size() >= 6)
                            {
                                display(5);
                                if (moods.size() >= 7)
                                {
                                    display(6);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


