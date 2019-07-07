package remy.pouzet.moodtracker.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import remy.pouzet.moodtracker.R;
import remy.pouzet.moodtracker.model.Mood;


/**
 * Created by Remy Pouzet on 29/06/2019.
 */
public class HistoricActivity extends AppCompatActivity
{

    public static final String PREF_KEY_MOOD = "PREF_KEY_MOOD";
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historic_activity);

        // Get moods
        mPreferences = getPreferences(MODE_PRIVATE);
        String fromJsonMoods = mPreferences.getString(PREF_KEY_MOOD, null);
        Gson gson = new Gson();
        ArrayList<Mood> moods = gson.fromJson(fromJsonMoods, new TypeToken<ArrayList<Mood>>()
        {
        }.getType());
        //END\| Get moods



    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }
}