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
    private SharedPreferences mPreferences;
    final int yesterday = -1;
    final int beforeYesterday = -2;
    final int aWeekAgo = -7;
    final int aMonthAgo = -32;
    Gson gson = new Gson();
    String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
    final ArrayList<Mood> moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
    {
    }.getType());
    //Util
    int a = 0;
    int centerRight = 21;
    Date now = new Date();
    long mDate = now.getTime() / 86400000;
    //END Util

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historic_activity);

        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);

        TextView baner = findViewById(R.id.imageView);
        TextView baner2 = findViewById(R.id.imageView2);
        TextView baner3 = findViewById(R.id.imageView3);
        TextView baner4 = findViewById(R.id.imageView4);
        TextView baner5 = findViewById(R.id.imageView5);
        TextView baner6 = findViewById(R.id.imageView6);
        TextView baner7 = findViewById(R.id.imageView7);

        ArrayList<TextView> baners = new ArrayList<>();
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
                case ( < beforeYesterday:
                    comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a " + (compareCurrentDateToMoodDate - compareCurrentDateToMoodDate * 2) + " jours";
                    break;

                case aWeekAgo:
                    comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a une semaine";
                    break;
                default:
                    comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a plus de 1 mois";


            } else
            if (compareCurrentDateToMoodDate < beforeYesterday && compareCurrentDateToMoodDate > aWeekAgo)
            {
                comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a " + (compareCurrentDateToMoodDate - compareCurrentDateToMoodDate * 2) + " jours";

            } else if (compareCurrentDateToMoodDate < aWeekAgo && compareCurrentDateToMoodDate > aMonthAgo)
            {
                comparaisonResultatBetweenCurrentDateToMoodDate = "Il y a plus de 1 semaine";

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

    private void commentUserDisplayingManagement()
    {
        if (moods.size() >= 1)
        {
            if (moods.get(0).getComment() != null)
            {
                baners.get(0).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                baners.get(0).setForegroundGravity(centerRight);

                baners.get(0).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                    {
                        Toast.makeText(HistoricActivity.this, moods.get(0).getComment(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            if (moods.size() >= 2)
            {
                if (moods.get(1).getComment() != null)
                {
                    baners.get(1).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                    baners.get(1).setForegroundGravity(centerRight);

                    baners.get(1).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(final View v)
                        {
                            Toast.makeText(HistoricActivity.this, moods.get(1).getComment(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if (moods.size() >= 3)
                {
                    if (moods.get(2).getComment() != null)
                    {
                        baners.get(2).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                        baners.get(2).setForegroundGravity(centerRight);

                        baners.get(2).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(final View v)
                            {
                                Toast.makeText(HistoricActivity.this, moods.get(2).getComment(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (moods.size() >= 4)
                    {
                        if (moods.get(3).getComment() != null)
                        {
                            baners.get(3).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                            baners.get(3).setForegroundGravity(centerRight);

                            baners.get(3).setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(final View v)
                                {
                                    Toast.makeText(HistoricActivity.this, moods.get(3).getComment(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        if (moods.size() >= 5)
                        {
                            if (moods.get(4).getComment() != null)
                            {
                                baners.get(4).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                                baners.get(4).setForegroundGravity(centerRight);

                                baners.get(4).setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(final View v)
                                    {
                                        Toast.makeText(HistoricActivity.this, moods.get(4).getComment(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            if (moods.size() >= 6)
                            {
                                if (moods.get(5).getComment() != null)
                                {
                                    baners.get(5).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                                    baners.get(5).setForegroundGravity(centerRight);

                                    baners.get(5).setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(final View v)
                                        {
                                            Toast.makeText(HistoricActivity.this, moods.get(5).getComment(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                if (moods.size() >= 7)
                                {
                                    if (moods.get(6).getComment() != null)
                                    {
                                        baners.get(6).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                                        baners.get(6).setForegroundGravity(centerRight);

                                        baners.get(6).setOnClickListener(new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(final View v)
                                            {
                                                Toast.makeText(HistoricActivity.this, moods.get(6).getComment(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




