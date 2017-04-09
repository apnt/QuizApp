package com.example.android.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class to implement the results presentation
 */
public class ResultsActivity extends AppCompatActivity {

    private TextView resultsText;
    private TextView[] questionsText = new TextView[8];
    private ImageView[] questionsImage = new ImageView[8];

    int totalScore = 0;

    /**
     * Override this method so that when the back button
     * is pressed the app returns to the launch screen
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        initializeViews();

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Extract the extras to initialize the summary and score
        String[] questions = intent.getStringArrayExtra(QuestionActivity.EXTRA_SUMMARY);
        boolean[] score = intent.getBooleanArrayExtra(QuestionActivity.EXTRA_SCORE);

        // Update the gui
        for (int i=0; i<questions.length; i++) {
            questionsText[i].setText(questions[i]);
            if (score[i]) {
                totalScore++;
                questionsImage[i].setImageResource(R.drawable.correct);
            }
            else
                questionsImage[i].setImageResource(R.drawable.wrong);
        }

        // Create and show the score message and the toast
        String resultsMessage = "";
        if(totalScore >= 7)
            resultsMessage = getString(R.string.congrats);

        resultsMessage += getString(R.string.final_score, totalScore);
        resultsText.setText(resultsMessage);

        Toast.makeText(this, getString(R.string.finish_toast, totalScore), Toast.LENGTH_SHORT).show();
    }

    /**
     * Initializes the views at the creation of the activity
     */
    private void initializeViews() {
        resultsText = (TextView) findViewById(R.id.final_result_text);

        questionsText[0] = (TextView) findViewById(R.id.question_1_text);
        questionsText[1] = (TextView) findViewById(R.id.question_2_text);
        questionsText[2] = (TextView) findViewById(R.id.question_3_text);
        questionsText[3] = (TextView) findViewById(R.id.question_4_text);
        questionsText[4] = (TextView) findViewById(R.id.question_5_text);
        questionsText[5] = (TextView) findViewById(R.id.question_6_text);
        questionsText[6] = (TextView) findViewById(R.id.question_7_text);
        questionsText[7] = (TextView) findViewById(R.id.question_8_text);

        questionsImage[0] = (ImageView) findViewById(R.id.question_1_image);
        questionsImage[1] = (ImageView) findViewById(R.id.question_2_image);
        questionsImage[2] = (ImageView) findViewById(R.id.question_3_image);
        questionsImage[3] = (ImageView) findViewById(R.id.question_4_image);
        questionsImage[4] = (ImageView) findViewById(R.id.question_5_image);
        questionsImage[5] = (ImageView) findViewById(R.id.question_6_image);
        questionsImage[6] = (ImageView) findViewById(R.id.question_7_image);
        questionsImage[7] = (ImageView) findViewById(R.id.question_8_image);
    }

    /**
     * Called when the return button is called
     */
    public void returnToStart(View view) {
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
        finish();
    }
}
