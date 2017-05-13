package com.example.android.quizapp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Base class for the activities used for implementing the questions
 * This class does not have a layout attached to it
 */
public class QuestionActivity extends AppCompatActivity {

    protected static final int NUMBER_OF_QUESTIONS = 8;

    public static final String EXTRA_MESSAGE = "com.example.android.quizapp.MESSAGE";
    public static final String EXTRA_ID = "com.example.android.quizapp.ID";
    public static final String EXTRA_SCORE = "com.example.android.quizapp.SCORE";
    public static final String EXTRA_SUMMARY = "com.example.android.quizapp.SUMMARY";

    protected TextView questionText;
    protected Button submitButton;
    protected Button continueButton;

    protected int questionID;
    protected String[] nextQuestion;
    protected boolean[] scoreArray;
    protected String[] questionsSummary;

    /**
     * Override this method so that when the back button
     * is pressed the app show a dialog in which the user
     * will confirm quitting the quiz or cancel and continue
     */
    @Override
    public void onBackPressed() {
        showQuitConfirmationDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This method is called to identify the activity
     * that created the intent to start this activity
     *
     * @return a string containing the caller activity
     */
    protected String identifyCallerActivity() {
        ComponentName callingActivity = getCallingActivity();
        String caller = callingActivity.toString();

        int callerPath = caller.lastIndexOf(".");
        caller = caller.substring(callerPath + 1, caller.length() - 1);

        return caller;
    }

    /**
     * This method is called to get the keys of the
     * extras put  in the intent by the caller activity
     *
     * @param callerActivity a string with the caller activity's name
     * @return an array of string containing the keys of the intent's extras
     */
    protected String[] getCallerMessages(String callerActivity) {
        String[] message = new String[4];

        if(callerActivity.equals("LaunchActivity")) {
            message[0] = LaunchActivity.EXTRA_MESSAGE;
            message[1] = LaunchActivity.EXTRA_ID;
            message[2] = LaunchActivity.EXTRA_SCORE;
            message[3] = LaunchActivity.EXTRA_SUMMARY;
        } else if(callerActivity.equals("RadioButtonQuestionActivity") ||
                  callerActivity.equals("TextQuestionActivity") ||
                  callerActivity.equals("CheckBoxQuestionActivity")) {
            message[0] = QuestionActivity.EXTRA_MESSAGE;
            message[1] = QuestionActivity.EXTRA_ID;
            message[2] = QuestionActivity.EXTRA_SCORE;
            message[3] = QuestionActivity.EXTRA_SUMMARY;
        }

        return message;
    }

    /**
     * This method is called when the continue or finish button is pressed
     */
    protected void continueQuiz(View view) {
        String nextQuestionClass = "";

        if ( questionID < NUMBER_OF_QUESTIONS ) {               // if not the last question repeat
            Resources res = this.getResources();                // what launch activity did on startQuiz()

            nextQuestion = getNextQuestion(res);
            nextQuestionClass = nextQuestion[0];
            Intent intent = initializeIntent(nextQuestionClass);

            questionID++;

            intent.putExtra(EXTRA_MESSAGE, nextQuestion);
            intent.putExtra(EXTRA_ID, questionID);
            intent.putExtra(EXTRA_SCORE, scoreArray);
            intent.putExtra(EXTRA_SUMMARY, questionsSummary);
            startActivityForResult(intent, 0);

            finish();
        } else if ( questionID == NUMBER_OF_QUESTIONS ) {       // if last question
            Intent intent = initializeIntent(nextQuestionClass);// initialize intent
            intent.putExtra(EXTRA_SCORE, scoreArray);           // put the score and summary as extras
            intent.putExtra(EXTRA_SUMMARY, questionsSummary);
            startActivity(intent);                              // start activity
            finish();                                           // kill this activity
        }
    }

    /**
     * This function is called find the next question in the
     * resources and return its data as an array of strings
     *
     * @param res contains the resources of the app
     * @return an array of string containing the data for the next question
     */
    protected String[] getNextQuestion(Resources res) {
        String[] questionStrings;

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
        } else intent = new Intent(this, ResultsActivity.class);    // if none of the above call the quiz has
                                                                    // finished -> call the results activity

        return intent;
    }

    /**
     * Prompt the user to confirm that they want to quit the quiz.
     */
    private void showQuitConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quit_message);
        builder.setPositiveButton(R.string.quit_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Confirm" button, so quit the quiz.
                Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(R.string.quit_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so continue with the quiz
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
