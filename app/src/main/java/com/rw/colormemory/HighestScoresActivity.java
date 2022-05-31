package com.rw.colormemory;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rw.com.rw.database.DatabaseHelper;
import com.rw.com.rw.database.Person;
import com.rw.com.rw.database.PersonDaoProvider;
import com.rw.com.rw.database.PersonRepo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Rakhita on 2/19/2018.
 */

public class HighestScoresActivity extends Activity {

    private ListView mListViewHighestScores;
    private LinearLayout mLinearLayoutTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_scores);

        mListViewHighestScores = findViewById(R.id.listViewHighestScore);
        View listViewHeader = getLayoutInflater().inflate(R.layout.highest_score_list_header, null);
        mListViewHighestScores.addHeaderView(listViewHeader);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        mLinearLayoutTop = findViewById(R.id.linearLayoutTop);
        android.view.ViewGroup.LayoutParams layoutParams = mLinearLayoutTop.getLayoutParams();
        layoutParams.height = (height / 9);
        mLinearLayoutTop.setLayoutParams(layoutParams);

        try {
            PersonRepo personRepo = new PersonRepo(new PersonDaoProvider(new DatabaseHelper(this)).get());
            List<Person> personList = personRepo.GetPersons();
            HighestScoresAdapter highestScoresAdapter = new HighestScoresAdapter(this, personList);
            mListViewHighestScores.setAdapter(highestScoresAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
