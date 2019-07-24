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

import java.text.DateFormat;
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
        DateFormat dateformatter = DateFormat.getDateInstance(DateFormat.SHORT);
        final String mDate = dateformatter.format(now);






        if (null != fromJsonMoods)
            if (moods.size() >= 1)
        {
            if (moods.get(0) != null)
            {
                int compare = mDate.compareTo(moods.get(0).getDate());
                String resultCompare = new String();
                if (compare == 0)
                {
                    resultCompare = "Aujourd'hui";
                } else if (compare == 1)
                {
                    resultCompare = "Hier";
                } else if (compare == 2)
                {
                    resultCompare = "Avant-hier";
                } else if (compare == 3)
                {
                    resultCompare = "Il y a 3 jours";
                } else if (compare == 4)
                {
                    resultCompare = "Il y a 4 jours";
                } else if (compare == 5)
                {
                    resultCompare = "Il y a 5 jours";
                } else if (compare == 6)
                {
                    resultCompare = "Il y a 6 jours";
                } else if (compare == 7)
                {
                    resultCompare = "Il y a une semaine";
                } else if (compare > 7 && compare < 32)
                {
                    resultCompare = "Il y a plus de 1 semaine";
                } else if (compare > 31)
                {
                    resultCompare = "Il y a plus de 1 mois";
                }
                baners.get(0).setText(resultCompare);


                if (moods.get(0).getCounter() == 0)
                {
                    baners.get(0).setBackgroundColor(getResources().getColor(R.color.light_sage));
                    baners.get(0).getLayoutParams().width = fourWidth;
                } else if (moods.get(0).getCounter() == 1)
                {
                    baners.get(0).setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                    baners.get(0).getLayoutParams().width = threeWidth;
                } else if (moods.get(0).getCounter() == 2)
                {
                    baners.get(0).setBackgroundColor(getResources().getColor(R.color.warm_grey));
                    baners.get(0).getLayoutParams().width = twoWidth;
                } else if (moods.get(0).getCounter() == 3)
                {
                    baners.get(0).setBackgroundColor(getResources().getColor(R.color.faded_red));
                    baners.get(0).getLayoutParams().width = fifthWidth;
                } else
                {
                    baners.get(0).setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                    baners.get(0).getLayoutParams().width = width;
                }
                if (moods.get(0).getComment() != null)
                {
                    baners.get(0).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                    baners.get(0).setForegroundGravity(21);

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
                    int compare1 = mDate.compareTo(moods.get(1).getDate());
                    String resultCompare1 = new String();
                    if (compare1 == 0)
                    {
                        resultCompare1 = "Aujourd'hui";
                    } else if (compare1 == 1)
                    {
                        resultCompare1 = "Hier";
                    } else if (compare1 == 2)
                    {
                        resultCompare1 = "Avant-hier";
                    } else if (compare1 == 3)
                    {
                        resultCompare1 = "Il y a 3 jours";
                    } else if (compare1 == 4)
                    {
                        resultCompare1 = "Il y a 4 jours";
                    } else if (compare1 == 5)
                    {
                        resultCompare1 = "Il y a 5 jours";
                    } else if (compare1 == 6)
                    {
                        resultCompare1 = "Il y a 6 jours";
                    } else if (compare1 == 7)
                    {
                        resultCompare1 = "Il y a une semaine";
                    } else if (compare1 > 7 && compare < 32)
                    {
                        resultCompare1 = "Il y a plus de 1 semaine";
                    } else if (compare1 > 31)
                    {
                        resultCompare1 = "Il y a plus de 1 mois";
                    }
                    baners.get(1).setText(resultCompare1);

                    if (moods.get(1).getCounter() == 0)
                    {
                        baners.get(1).setBackgroundColor(getResources().getColor(R.color.light_sage));
                        baners.get(1).getLayoutParams().width = fourWidth;
                    } else if (moods.get(1).getCounter() == 1)
                    {
                        baners.get(1).setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                        baners.get(1).getLayoutParams().width = threeWidth;
                    } else if (moods.get(1).getCounter() == 2)
                    {
                        baners.get(1).setBackgroundColor(getResources().getColor(R.color.warm_grey));
                        baners.get(1).getLayoutParams().width = twoWidth;
                    } else if (moods.get(1).getCounter() == 3)
                    {
                        baners.get(1).setBackgroundColor(getResources().getColor(R.color.faded_red));
                        baners.get(1).getLayoutParams().width = fifthWidth;
                    } else
                    {
                        baners.get(1).setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                        baners.get(1).getLayoutParams().width = width;
                    }
                    if (moods.get(1).getComment() != null)
                    {
                        baners.get(1).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                        baners.get(1).setForegroundGravity(21);

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
                        int compare2 = mDate.compareTo(moods.get(2).getDate());
                        String resultCompare2 = new String();
                        if (compare2 == 0)
                        {
                            resultCompare2 = "Aujourd'hui";
                        } else if (compare2 == 1)
                        {
                            resultCompare2 = "Hier";
                        } else if (compare2 == 2)
                        {
                            resultCompare2 = "Avant-hier";
                        } else if (compare2 == 3)
                        {
                            resultCompare2 = "Il y a 3 jours";
                        } else if (compare2 == 4)
                        {
                            resultCompare2 = "Il y a 4 jours";
                        } else if (compare2 == 5)
                        {
                            resultCompare2 = "Il y a 5 jours";
                        } else if (compare2 == 6)
                        {
                            resultCompare2 = "Il y a 6 jours";
                        } else if (compare2 == 7)
                        {
                            resultCompare2 = "Il y a une semaine";
                        } else if (compare2 > 7 && compare < 32)
                        {
                            resultCompare2 = "Il y a plus de 1 semaine";
                        } else if (compare2 > 31)
                        {
                            resultCompare2 = "Il y a plus de 1 mois";
                        }
                        baners.get(2).setText(resultCompare2);


                        if (moods.get(2).getCounter() == 0)
                        {
                            baners.get(2).setBackgroundColor(getResources().getColor(R.color.light_sage));
                            baners.get(2).getLayoutParams().width = fourWidth;
                        } else if (moods.get(2).getCounter() == 1)
                        {
                            baners.get(2).setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                            baners.get(2).getLayoutParams().width = threeWidth;
                        } else if (moods.get(2).getCounter() == 2)
                        {
                            baners.get(2).setBackgroundColor(getResources().getColor(R.color.warm_grey));
                            baners.get(2).getLayoutParams().width = twoWidth;
                        } else if (moods.get(2).getCounter() == 3)
                        {
                            baners.get(2).setBackgroundColor(getResources().getColor(R.color.faded_red));
                            baners.get(2).getLayoutParams().width = fifthWidth;
                        } else
                        {
                            baners.get(2).setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                            baners.get(2).getLayoutParams().width = width;
                        }

                        if (moods.get(2).getComment() != null)
                        {
                            baners.get(2).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                            baners.get(2).setForegroundGravity(21);

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
                            int compare3 = mDate.compareTo(moods.get(3).getDate());
                            String resultCompare3 = new String();
                            if (compare == 0)
                            {
                                resultCompare3 = "Aujourd'hui";
                            } else if (compare3 == 1)
                            {
                                resultCompare3 = "Hier";
                            } else if (compare3 == 2)
                            {
                                resultCompare3 = "Avant-hier";
                            } else if (compare3 == 3)
                            {
                                resultCompare3 = "Il y a 3 jours";
                            } else if (compare3 == 4)
                            {
                                resultCompare3 = "Il y a 4 jours";
                            } else if (compare3 == 5)
                            {
                                resultCompare3 = "Il y a 5 jours";
                            } else if (compare3 == 6)
                            {
                                resultCompare3 = "Il y a 6 jours";
                            } else if (compare3 == 7)
                            {
                                resultCompare3 = "Il y a une semaine";
                            } else if (compare3 > 7 && compare < 32)
                            {
                                resultCompare3 = "Il y a plus de 1 semaine";
                            } else if (compare3 > 31)
                            {
                                resultCompare3 = "Il y a plus de 1 mois";
                            }
                            baners.get(3).setText(resultCompare3);

                            if (moods.get(3).getCounter() == 0)
                            {
                                baners.get(3).setBackgroundColor(getResources().getColor(R.color.light_sage));
                                baners.get(3).getLayoutParams().width = fourWidth;
                            } else if (moods.get(3).getCounter() == 1)
                            {
                                baners.get(3).setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                                baners.get(3).getLayoutParams().width = threeWidth;
                            } else if (moods.get(3).getCounter() == 2)
                            {
                                baners.get(3).setBackgroundColor(getResources().getColor(R.color.warm_grey));
                                baners.get(3).getLayoutParams().width = twoWidth;
                            } else if (moods.get(3).getCounter() == 3)
                            {
                                baners.get(3).setBackgroundColor(getResources().getColor(R.color.faded_red));
                                baners.get(3).getLayoutParams().width = fifthWidth;
                            } else
                            {
                                baners.get(3).setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                                baners.get(3).getLayoutParams().width = width;
                            }
                            if (moods.get(3).getComment() != null)
                            {
                                baners.get(3).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                                baners.get(3).setForegroundGravity(21);
                            }
                            baners.get(3).setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(final View v)
                                {
                                    Toast.makeText(HistoricActivity.this, moods.get(3).getComment(), Toast.LENGTH_LONG).show();
                                }
                            });

                            if (moods.size() >= 5)
                            {
                                int compare4 = mDate.compareTo(moods.get(4).getDate());
                                String resultCompare4 = new String();
                                if (compare4 == 0)
                                {
                                    resultCompare4 = "Aujourd'hui";
                                } else if (compare4 == 1)
                                {
                                    resultCompare4 = "Hier";
                                } else if (compare4 == 2)
                                {
                                    resultCompare4 = "Avant-hier";
                                } else if (compare4 == 3)
                                {
                                    resultCompare4 = "Il y a 3 jours";
                                } else if (compare4 == 4)
                                {
                                    resultCompare4 = "Il y a 4 jours";
                                } else if (compare4 == 5)
                                {
                                    resultCompare4 = "Il y a 5 jours";
                                } else if (compare4 == 6)
                                {
                                    resultCompare4 = "Il y a 6 jours";
                                } else if (compare4 == 7)
                                {
                                    resultCompare4 = "Il y a une semaine";
                                } else if (compare4 > 7 && compare < 32)
                                {
                                    resultCompare4 = "Il y a plus de 1 semaine";
                                } else if (compare4 > 31)
                                {
                                    resultCompare4 = "Il y a plus de 1 mois";
                                }
                                baners.get(4).setText(resultCompare4);


                                if (moods.get(4).getCounter() == 0)
                                {
                                    baners.get(4).setBackgroundColor(getResources().getColor(R.color.light_sage));
                                    baners.get(4).getLayoutParams().width = fourWidth;
                                } else if (moods.get(4).getCounter() == 1)
                                {
                                    baners.get(4).setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                                    baners.get(4).getLayoutParams().width = threeWidth;
                                } else if (moods.get(4).getCounter() == 2)
                                {
                                    baners.get(4).setBackgroundColor(getResources().getColor(R.color.warm_grey));
                                    baners.get(4).getLayoutParams().width = twoWidth;
                                } else if (moods.get(4).getCounter() == 3)
                                {
                                    baners.get(4).setBackgroundColor(getResources().getColor(R.color.faded_red));
                                    baners.get(4).getLayoutParams().width = fifthWidth;
                                } else
                                {
                                    baners.get(4).setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                                    baners.get(4).getLayoutParams().width = width;
                                }
                                if (moods.get(4).getComment() != null)
                                {
                                    baners.get(4).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                                    baners.get(4).setForegroundGravity(21);


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
                                    int compare5 = mDate.compareTo(moods.get(5).getDate());
                                    String resultCompare5 = new String();
                                    if (compare == 0)
                                    {
                                        resultCompare5 = "Aujourd'hui";
                                    } else if (compare5 == 1)
                                    {
                                        resultCompare5 = "Hier";
                                    } else if (compare5 == 2)
                                    {
                                        resultCompare5 = "Avant-hier";
                                    } else if (compare5 == 3)
                                    {
                                        resultCompare5 = "Il y a 3 jours";
                                    } else if (compare5 == 4)
                                    {
                                        resultCompare5 = "Il y a 4 jours";
                                    } else if (compare5 == 5)
                                    {
                                        resultCompare5 = "Il y a 5 jours";
                                    } else if (compare5 == 6)
                                    {
                                        resultCompare5 = "Il y a 6 jours";
                                    } else if (compare5 == 7)
                                    {
                                        resultCompare5 = "Il y a une semaine";
                                    } else if (compare5 > 7 && compare < 32)
                                    {
                                        resultCompare5 = "Il y a plus de 1 semaine";
                                    } else if (compare5 > 31)
                                    {
                                        resultCompare5 = "Il y a plus de 1 mois";
                                    }
                                    baners.get(5).setText(resultCompare5);

                                    if (moods.get(5).getCounter() == 0)
                                    {
                                        baners.get(5).setBackgroundColor(getResources().getColor(R.color.light_sage));
                                        baners.get(5).getLayoutParams().width = fourWidth;
                                    } else if (moods.get(5).getCounter() == 1)
                                    {
                                        baners.get(5).setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                                        baners.get(5).getLayoutParams().width = threeWidth;
                                    } else if (moods.get(5).getCounter() == 2)
                                    {
                                        baners.get(5).setBackgroundColor(getResources().getColor(R.color.warm_grey));
                                        baners.get(5).getLayoutParams().width = twoWidth;
                                    } else if (moods.get(5).getCounter() == 3)
                                    {
                                        baners.get(5).setBackgroundColor(getResources().getColor(R.color.faded_red));
                                        baners.get(5).getLayoutParams().width = fifthWidth;
                                    } else
                                    {
                                        baners.get(5).setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                                        baners.get(5).getLayoutParams().width = width;
                                    }

                                    if (moods.get(5).getComment() != null)
                                    {
                                        baners.get(5).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                                        baners.get(5).setForegroundGravity(21);

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
                                        int compare6 = mDate.compareTo(moods.get(6).getDate());
                                        String resultCompare6 = new String();
                                        if (compare6 == 0)
                                        {
                                            resultCompare6 = "Aujourd'hui";
                                        } else if (compare6 == 1)
                                        {
                                            resultCompare6 = "Hier";
                                        } else if (compare6 == 2)
                                        {
                                            resultCompare6 = "Avant-hier";
                                        } else if (compare6 == 3)
                                        {
                                            resultCompare6 = "Il y a 3 jours";
                                        } else if (compare6 == 4)
                                        {
                                            resultCompare6 = "Il y a 4 jours";
                                        } else if (compare6 == 5)
                                        {
                                            resultCompare6 = "Il y a 5 jours";
                                        } else if (compare6 == 6)
                                        {
                                            resultCompare6 = "Il y a 6 jours";
                                        } else if (compare6 == 7)
                                        {
                                            resultCompare6 = "Il y a une semaine";
                                        } else if (compare6 > 7 && compare < 32)
                                        {
                                            resultCompare6 = "Il y a plus de 1 semaine";
                                        } else if (compare6 > 31)
                                        {
                                            resultCompare6 = "Il y a plus de 1 mois";
                                        }
                                        baners.get(6).setText(resultCompare6);


                                        if (moods.get(6).getCounter() == 0)
                                        {
                                            baners.get(6).setBackgroundColor(getResources().getColor(R.color.light_sage));
                                            baners.get(6).getLayoutParams().width = fourWidth;
                                        } else if (moods.get(6).getCounter() == 1)
                                        {
                                            baners.get(6).setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                                            baners.get(6).getLayoutParams().width = threeWidth;
                                        } else if (moods.get(6).getCounter() == 2)
                                        {
                                            baners.get(6).setBackgroundColor(getResources().getColor(R.color.warm_grey));
                                            baners.get(6).getLayoutParams().width = twoWidth;
                                        } else if (moods.get(6).getCounter() == 3)
                                        {
                                            baners.get(6).setBackgroundColor(getResources().getColor(R.color.faded_red));
                                            baners.get(6).getLayoutParams().width = fifthWidth;
                                        } else
                                        {
                                            baners.get(6).setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                                            baners.get(6).getLayoutParams().width = width;
                                        }
                                        if (moods.get(6).getComment() != null)
                                        {
                                            baners.get(6).setForeground(getResources().getDrawable(R.mipmap.ic_comment_black_48px));
                                            baners.get(6).setForegroundGravity(21);

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
}