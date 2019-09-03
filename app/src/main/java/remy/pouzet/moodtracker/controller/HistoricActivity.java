package remy.pouzet.moodtracker.controller;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.Mood;

import static remy.pouzet.moodtracker.model.Util.TODAY;
import static remy.pouzet.moodtracker.model.Util.aMonthAgo;
import static remy.pouzet.moodtracker.model.Util.aWeekAgo;
import static remy.pouzet.moodtracker.model.Util.beforeYesterday;
import static remy.pouzet.moodtracker.model.Util.yesterday;

/**
 * Created by Remy Pouzet on 29/06/2019.
 */

public class HistoricActivity extends AppCompatActivity
{
    public static final String PREF_KEY_MOOD = "PREF_KEY_MOOD";

    TextView baner;
    TextView baner2;
    TextView baner3;
    TextView baner4;
    TextView baner5;
    TextView baner6;
    TextView baner7;

    //Util
    int index = 0;
    Date now;
    long mDate;
    ArrayList<TextView> baners;
    Gson gson;
    ArrayList<Mood> moods;
    //END Util

    private SharedPreferences mPreferences;


    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historic_activity);

        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        gson = new Gson();
        String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());

        now = new Date();
        mDate = now.getTime() / 86400000;

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
        while (index + 1 <= moods.size() || moods.size() > 8)
        {
            dispayingDateManagement();
            bannersBackgroundColorDisplayingManagement();
        }
        commentUserDisplayingManagement();
    }

    private void dispayingDateManagement()
    {
        if (moods.get(index) != null)
        {
            long longCompareCurrentDateToMoodDate = (moods.get(index).getDate() - mDate);
            int compareCurrentDateToMoodDate = (int) longCompareCurrentDateToMoodDate;

            String comparaisonResultatBetweenCurrentDateToMoodDate;

            switch (compareCurrentDateToMoodDate)
            {
                case TODAY:
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
                        comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a " + (compareCurrentDateToMoodDate - compareCurrentDateToMoodDate * 2) + " jours";
                    else if (compareCurrentDateToMoodDate < aWeekAgo && compareCurrentDateToMoodDate > aMonthAgo)
                        comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a plus de 1 semaine";
                    else
                        comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a plus de 1 mois";
            }
            baners.get(index).setText(comparaisonResultatBetweenCurrentDateToMoodDate);
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


        switch (moods.get(index).getCounter())
        {
            case 0:
                baners.get(index).setBackgroundColor(getResources().getColor(R.color.light_sage));
                baners.get(index).getLayoutParams().width = fourWidth;
                break;
            case 1:
                baners.get(index).setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                baners.get(index).getLayoutParams().width = threeWidth;
                break;
            case 2:
                baners.get(index).setBackgroundColor(getResources().getColor(R.color.warm_grey));
                baners.get(index).getLayoutParams().width = twoWidth;
                break;
            case 3:
                baners.get(index).setBackgroundColor(getResources().getColor(R.color.faded_red));
                baners.get(index).getLayoutParams().width = fifthWidth;
                break;
            default:
                baners.get(index).setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                baners.get(index).getLayoutParams().width = width;
        }
        index++;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void display(final int id)
    {
        if (moods.get(id).getComment() != null)
        {
            baners.get(id).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
            baners.get(id).setForegroundGravity(Gravity.CENTER_VERTICAL + Gravity.RIGHT);

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
        for (int id = 0; moods.size() >= (id + 1); id++)
        {
            display(id);
        }
    }
}





