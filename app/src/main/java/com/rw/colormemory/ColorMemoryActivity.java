package com.rw.colormemory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rw.colormemoryhelper.Score;
import com.rw.colormemoryhelper.ViewFlipperCustomClickEventListener;
import com.rw.com.rw.database.DatabaseHelper;
import com.rw.com.rw.database.Person;
import com.rw.com.rw.database.PersonDaoProvider;
import com.rw.com.rw.database.PersonRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Rakhita on 2/17/2018.
 */

public class ColorMemoryActivity extends Activity implements ViewFlipperCustomClickEventListener, View.OnClickListener {

    private GridView mGridViewColorMemory;
    private LinearLayout mLinearLayoutTop, mLinearLayoutHidden;
    private TextView textViewCurrentScore;
    private Context mContext;
    private Button mButtonHighestScore, mButtonNewGame, mButtonExitGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_color_memory);

            this.mContext = this;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            textViewCurrentScore = findViewById(R.id.textViewCurrentScore);
            textViewCurrentScore.setText("");
            Score.Instance().reset();

            mLinearLayoutTop = findViewById(R.id.linearLayoutTop);
            android.view.ViewGroup.LayoutParams layoutParams = mLinearLayoutTop.getLayoutParams();
            layoutParams.height = (height / 9);
            mLinearLayoutTop.setLayoutParams(layoutParams);

            mGridViewColorMemory = findViewById(R.id.gridViewColorMemory);
            int rowHeight = (height - (height / 9));
            ColorMemoryGridViewAdapter gva = new ColorMemoryGridViewAdapter(this, getColorMemoryList(), rowHeight / 4);
            gva.setSomeEventListener(this);
            mGridViewColorMemory.setAdapter(gva);

            mGridViewColorMemory.setColumnWidth(width / 4);

            mButtonHighestScore = findViewById(R.id.buttonHighestScores);
            mButtonHighestScore.setOnClickListener(this);

            mButtonNewGame = findViewById(R.id.buttonNewGame);
            mButtonNewGame.setOnClickListener(this);

            mButtonExitGame = findViewById(R.id.buttonExitGame);
            mButtonExitGame.setOnClickListener(this);

            mLinearLayoutHidden = findViewById(R.id.linearLayoutHidden);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewFlipperCustomClickEvent() {
        int currentScore = Score.Instance().getCurrentScore();
        textViewCurrentScore.setText(String.valueOf(currentScore));

        int count = Score.Instance().getMatchingCount();
        if (count == 8) {
            final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.applause);
            mediaPlayer.start();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLinearLayoutHidden.setVisibility(View.VISIBLE);
                    mGridViewColorMemory.setVisibility(View.GONE);

                    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                    View userInputView = layoutInflater.inflate(R.layout.user_input_dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mContext);
                    alertDialogBuilder.setView(userInputView);
                    alertDialogBuilder.setTitle(getString(R.string.youWon));

                    final TextView textViewCurrentScore = userInputView
                            .findViewById(R.id.textViewCurrentScore);
                    textViewCurrentScore.setText(String.format(getString(R.string.yourScoreIs), Score.Instance().getCurrentScore()));

                    final EditText editTextName = userInputView
                            .findViewById(R.id.editTextName);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.ok), null)
                            .setNegativeButton(getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mediaPlayer.stop();
                                            dialog.cancel();
                                        }
                                    });


                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    Button buttonPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    buttonPositive.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String name = editTextName.getText().toString();
                            int score = Score.Instance().getCurrentScore();

                            if (name == null || (name != null && name.isEmpty())) {
                                Toast.makeText(mContext, R.string.pleaseEnterYourName, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            try {
                                PersonRepo personRepo = new PersonRepo(new PersonDaoProvider(new DatabaseHelper(mContext)).get());
                                Person person = new Person();
                                person.setName(name.trim());
                                person.setScore(score);
                                personRepo.SavePerson(person);

                                Score.Instance().reset();
                                textViewCurrentScore.setText("");

                                mediaPlayer.stop();
                                alertDialog.dismiss();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, 2000);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonHighestScores) {
            Intent newIntent = new Intent(this, HighestScoresActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(newIntent);
        } else if (v.getId() == R.id.buttonNewGame) {
            Intent newIntent = new Intent(this, ColorMemoryActivity.class);
            this.finish();
            this.startActivity(newIntent);
        } else if (v.getId() == R.id.buttonExitGame) {
            finish();
            System.exit(0);
        }
    }

    private List<SparseIntArray> getColorMemoryList() {
        List<SparseIntArray> imageList = new ArrayList<SparseIntArray>();

        SparseIntArray element1 = new SparseIntArray();
        element1.append(1, R.drawable.colour1);
        imageList.add(element1);

        SparseIntArray element2 = new SparseIntArray();
        element2.append(1, R.drawable.colour1);
        imageList.add(element2);

        SparseIntArray element3 = new SparseIntArray();
        element3.append(2, R.drawable.colour2);
        imageList.add(element3);

        SparseIntArray element4 = new SparseIntArray();
        element4.append(2, R.drawable.colour2);
        imageList.add(element4);

        SparseIntArray element5 = new SparseIntArray();
        element5.append(3, R.drawable.colour3);
        imageList.add(element5);

        SparseIntArray element6 = new SparseIntArray();
        element6.append(3, R.drawable.colour3);
        imageList.add(element6);

        SparseIntArray element7 = new SparseIntArray();
        element7.append(4, R.drawable.colour4);
        imageList.add(element7);

        SparseIntArray element8 = new SparseIntArray();
        element8.append(4, R.drawable.colour4);
        imageList.add(element8);

        SparseIntArray element9 = new SparseIntArray();
        element9.append(5, R.drawable.colour5);
        imageList.add(element9);

        SparseIntArray element10 = new SparseIntArray();
        element10.append(5, R.drawable.colour5);
        imageList.add(element10);

        SparseIntArray element11 = new SparseIntArray();
        element11.append(6, R.drawable.colour6);
        imageList.add(element11);

        SparseIntArray element12 = new SparseIntArray();
        element12.append(6, R.drawable.colour6);
        imageList.add(element12);

        SparseIntArray element13 = new SparseIntArray();
        element13.append(7, R.drawable.colour7);
        imageList.add(element13);

        SparseIntArray element14 = new SparseIntArray();
        element14.append(7, R.drawable.colour7);
        imageList.add(element14);

        SparseIntArray element15 = new SparseIntArray();
        element15.append(8, R.drawable.colour8);
        imageList.add(element15);

        SparseIntArray element16 = new SparseIntArray();
        element16.append(8, R.drawable.colour8);
        imageList.add(element16);

        Collections.shuffle(imageList);
        return imageList;
    }
}