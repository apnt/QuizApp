package com.example.android.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Extends the QuestionActivity class to implement EditText questions
 */
public class TextQuestionActivity extends QuestionActivity {

    TextView answerText;
    EditText answerEditText;

    TextQuestion question;

    /**
     * Saves the state of the activity
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("answered", question.getAnswered());
    }

    /**
     * Restores the state of the of the activity based on the savedInstanceState
     * Depending on this state it may also use setColors() to restore some views
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        question.setAnswered(savedInstanceState.getBoolean("answered"));

        if (question.getAnswered()) {
            answerEditText.setFocusable(false);
            answerEditText.setClickable(false);

            String answer = answerEditText.getText().toString();
            boolean correct = answer.equals(question.getCorrectAnswer());
            setColors(correct);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_question);

        // Initialize the views
        questionText = (TextView) findViewById(R.id.question_text_view);
        answerText = (TextView) findViewById(R.id.answer_text_view);
        answerEditText = (EditText) findViewById(R.id.question_answer_edit_text);
        submitButton = (Button) findViewById(R.id.submit_button);
        continueButton = (Button) findViewById(R.id.continue_button);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Identify the caller activity and extract the keys for the extras
        String callingActivity = identifyCallerActivity();
        String[] callerMessages = getCallerMessages(callingActivity);

        // Use the extras to initialize the question's variables
        String[] questionStrings = intent.getStringArrayExtra(callerMessages[0]);
        questionID = intent.getIntExtra(callerMessages[1],0);
        scoreArray = intent.getBooleanArrayExtra(callerMessages[2]);
        questionsSummary = intent.getStringArrayExtra(callerMessages[3]);

        // Create a new TextQuestion and update the summary
        question = new TextQuestion(questionStrings);
        questionsSummary[questionID-1] = question.getQuestionText();

        // Set the title of the app to the question's number
        setTitle(getString(R.string.question) + questionID);

        //Update the gui
        questionText.setText(question.getQuestionText());
        answerText.setText(getString(R.string.correct_answer, question.getCorrectAnswer()));

        if (questionID == NUMBER_OF_QUESTIONS)
            continueButton.setText(getString(R.string.finish));
    }

    /**
     * This method is called when the submit button is pressed.
     * It checks the user's answer and its correctness and
     * updates the views and the question and score variables.
     */
    public void submit(View view) {
        // If the question is already answered inform the user with a toast message
        if (question.getAnswered()) {
            Toast.makeText(this, getString(R.string.submit_toast), Toast.LENGTH_SHORT).show();
        } else {
            question.setAnswered(true);

            String correctAnswer = question.getCorrectAnswer();
            String answer = answerEditText.getText().toString();
            boolean correct = answer.equals(correctAnswer);

            if (correct)
                scoreArray[questionID - 1] = true;

            updateViews(correct);
        }
    }

    /**
     * Updates the views after the user's answer
     */
    public void updateViews(boolean correct) {
        answerEditText.setFocusable(false);
        answerEditText.setClickable(false);
        setColors(correct);
    }

    /**
     * Sets the colors for the correct and wrong answers
     *
     * @param correct holds whether the answer was answered corerctly or not
     */
    public void setColors (boolean correct) {
        if (correct)
            answerEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.colorCorrect));
        else {
            answerText.setVisibility(View.VISIBLE);
            answerEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWrong));
        }
    }

    /**
     * This method is called when the continue or finish button is pressed
     */
    public void continueQuiz(View view) {
        // If the question was not already answered call the standard
        // parent method else inform the user with a toast message.
        if (question.getAnswered())
            super.continueQuiz(view);
        else
            Toast.makeText(this, getString(R.string.continue_toast), Toast.LENGTH_SHORT).show();
    }

}
