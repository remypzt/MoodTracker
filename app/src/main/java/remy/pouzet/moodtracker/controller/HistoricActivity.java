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
    private SharedPreferences mPreferences;

    int a = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historic_activity);

        mPreferences = getSharedPreferences(PREF_KEY_MOOD, MODE_PRIVATE);
        String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);

        Gson gson = new Gson();
        final ArrayList<Mood> moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());

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

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int fifthWidth = width / 5;
        int fourWidth = fifthWidth * 4;
        int threeWidth = fifthWidth * 3;
        int twoWidth = fifthWidth * 2;

        Date now = new Date();
        long mDate = now.getTime();

        if (null != fromJsonMoods)
        {
            while (a + 1 <= moods.size() || moods.size() > 8)
            {
                if (moods.get(a) != null)
                {
                    long compare = (moods.get(a).getDate() - mDate) / 86400000;
                    String resultCompare;

                    if (compare == 0)
                    {
                        resultCompare = "Aujourd'hui";
                    } else if (compare == -1)
                    {
                        resultCompare = "Hier";
                    } else if (compare == -2)
                    {
                        resultCompare = "Avant-hier";
                    } else if (compare < -2 && compare > -7)
                    {
                        resultCompare = "Il y a " + (compare - compare * 2) + " jours";
                    } else if (compare == -7)
                    {
                        resultCompare = "Il y a une semaine";
                    } else if (compare < -7 && compare > -32)
                    {
                        resultCompare = "Il y a plus de 1 semaine";
                    } else
                    {
                        resultCompare = "Il y a plus de 1 mois";
                    }
                    baners.get(a).setText(resultCompare);

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

                    if (moods.get(a).getComment() != null)
                    {
                        baners.get(a).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                        baners.get(a).setForegroundGravity(21);

                        baners.get(a).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(final View v)
                            {
                                Toast.makeText(HistoricActivity.this, moods.get(a - 1).getComment(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    a++;
                }
            }
        }
    }
}