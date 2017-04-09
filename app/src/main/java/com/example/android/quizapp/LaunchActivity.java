package com.example.android.quizapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LaunchActivity extends AppCompatActivity {

    private static final int NUMBER_OF_QUESTIONS = 8;

    public static final String EXTRA_MESSAGE = "com.example.android.quizapp.MESSAGE";
    public static final String EXTRA_ID = "com.example.android.quizapp.ID";
    public static final String EXTRA_SCORE = "com.example.android.quizapp.SCORE";
    public static final String EXTRA_SUMMARY = "com.example.android.quizapp.SUMMARY";

    private int questionID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        getSupportActionBar().hide();
    }

    /**
     * Called when the user taps the Start button
     */
    public void startQuiz(View view) {
        Resources res = this.getResources();

        String[] nextQuestion = getNextQuestion(res);                   // gets the next question from resources
        String nextQuestionClass = nextQuestion[0];                     // gets the question's class
        Intent intent = initializeIntent(nextQuestionClass);            // initialize the intent to call the next activity

        questionID++;                                                   // increment the question counter
        boolean[] scoreArray = new boolean[NUMBER_OF_QUESTIONS];        // initialize the array with the score
        String[] questionsSummary = new String[NUMBER_OF_QUESTIONS];    // initialize the question's summary
        for (int i=0; i<scoreArray.length; i++)
            scoreArray[i] = false;

        intent.putExtra(EXTRA_MESSAGE, nextQuestion);                   // put Extras in the intent
        intent.putExtra(EXTRA_ID, questionID);
        intent.putExtra(EXTRA_SCORE, scoreArray);
        intent.putExtra(EXTRA_SUMMARY, questionsSummary);

        startActivityForResult(intent,0);                               // start activity

        this.finish();                                                  // kill this activity
    }

    /**
     * This function is called find the next question in the
     * resources and return its data as an array of strings
     *
     * @param res contains the resources of the app
     * @return an array of string containing the data for the next question
     */
    private String[] getNextQuestion(Resources res) {
        String[] questionStrings;

        // get the question_ID of the arrays.xml
        String questionNumber = getString(R.string.question_array_identifier) + (questionID+1);
        int id = res.getIdentifier(questionNumber, "array", this.getPackageName());
        questionStrings = res.getStringArray(id);

        return questionStrings;
    }

    /**
     * This method is used to initialize the intent in order to call
     * the appropriate activity based on the next question's class
     *
     * @param questionClass the class of the next question
     * @return the intent that will start the next activity
     */
    protected Intent initializeIntent (String questionClass) {
        Intent intent;

        if(questionClass.equals("radio")) {
            intent = new Intent(this, RadioButtonQuestionActivity.class);
        } else if (questionClass.equals("check")) {
            intent = new Intent(this, CheckBoxQuestionActivity.class);
        } else if (questionClass.equals("text")){
            intent = new Intent(this, TextQuestionActivity.class);
        } else intent = new Intent(this, LaunchActivity.class);

        return intent;
    }

}

