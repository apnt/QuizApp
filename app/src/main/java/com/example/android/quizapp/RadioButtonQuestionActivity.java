package com.example.android.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Extends the QuestionActivity class to implement RadioButton questions
 */
public class RadioButtonQuestionActivity extends QuestionActivity {

    RadioGroup choicesRadioGroup;
    RadioButton[] questionChoices = new RadioButton[4];

    RadioButtonQuestion question;

    /**
     * Saves the state of the activity
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("answered", question.getAnswered());
        savedInstanceState.putBoolean("option1_check", questionChoices[0].isChecked());
        savedInstanceState.putBoolean("option1_click", questionChoices[0].isClickable());
        savedInstanceState.putBoolean("option2_check", questionChoices[1].isChecked());
        savedInstanceState.putBoolean("option2_click", questionChoices[1].isClickable());
        savedInstanceState.putBoolean("option3_check", questionChoices[2].isChecked());
        savedInstanceState.putBoolean("option3_click", questionChoices[2].isClickable());
        savedInstanceState.putBoolean("option4_check", questionChoices[3].isChecked());
        savedInstanceState.putBoolean("option4_click", questionChoices[3].isClickable());
    }

    /**
     * Restores the state of the of the activity based on the savedInstanceState
     * Depending on this state it may also use setColors() to restore some views
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        question.setAnswered(savedInstanceState.getBoolean("answered"));

        if (question.getAnswered())
            setColors();

        questionChoices[0].setChecked(savedInstanceState.getBoolean("option1_check"));
        questionChoices[0].setClickable(savedInstanceState.getBoolean("option1_click"));
        questionChoices[1].setChecked(savedInstanceState.getBoolean("option2_check"));
        questionChoices[1].setClickable(savedInstanceState.getBoolean("option2_click"));
        questionChoices[2].setChecked(savedInstanceState.getBoolean("option3_check"));
        questionChoices[2].setClickable(savedInstanceState.getBoolean("option3_click"));
        questionChoices[3].setChecked(savedInstanceState.getBoolean("option4_check"));
        questionChoices[3].setClickable(savedInstanceState.getBoolean("option4_click"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_button_question);

        // Initialize the views
        questionText = (TextView) findViewById(R.id.question_text_view);
        choicesRadioGroup = (RadioGroup) findViewById(R.id.choices_radiogroup);
        questionChoices[0] = (RadioButton) findViewById(R.id.choice_1_text);
        questionChoices[1] = (RadioButton) findViewById(R.id.choice_2_text);
        questionChoices[2] = (RadioButton) findViewById(R.id.choice_3_text);
        questionChoices[3] = (RadioButton) findViewById(R.id.choice_4_text);
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

        // Create a new RadioButtonQuestion and update the summary
        question = new RadioButtonQuestion(questionStrings);
        questionsSummary[questionID-1] = question.getQuestionText();

        String[] choices = question.getQuestionChoices();

        // Set the title of the app to the question's number
        setTitle(getString(R.string.question) + questionID);

        // Update the gui
        questionText.setText(question.getQuestionText());
        questionChoices[0].setText(choices[0]);
        questionChoices[1].setText(choices[1]);
        questionChoices[2].setText(choices[2]);
        questionChoices[3].setText(choices[3]);
        
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

            if(questionChoices[question.getCorrectAnswer()].isChecked())
                scoreArray[questionID-1] = true;

            updateViews();
        }
    }

    /**
     * Updates the views after the user's answer
     */
    private void updateViews() {
        for(int i = 0; i<questionChoices.length; i++)
            questionChoices[i].setClickable(false);

        setColors();
    }

    /**
     * Sets the colors for the correct and wrong answers
     */
    private void setColors () {
        questionChoices[question.getCorrectAnswer()].setBackgroundColor(ContextCompat.getColor(this, R.color.colorCorrect));

        if (!questionChoices[question.getCorrectAnswer()].isChecked()) {
            int userAnswerId = choicesRadioGroup.getCheckedRadioButtonId();
            //Log.e("userAnswerId", "i = " + userAnswerId);
            if (userAnswerId > 0) {
                RadioButton wrongRadioButton = (RadioButton) findViewById(userAnswerId);
                wrongRadioButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWrong));
            }
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