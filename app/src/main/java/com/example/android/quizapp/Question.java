package com.example.android.quizapp;

/**
 * The questions' classes are defined in this file.
 */

/**
 * Question class is the basic class for all types of questions
 */
public class Question {
    protected String questionText;      // the question itself
    protected String questionClass;     // the class of the question (radio, text, check)
    protected boolean answered;         // the boolean holding if the question was answered by the user

    public Question() {}

    public String getQuestionText() {
        return questionText;
    }

    public String getQuestionClass() {
        return questionClass;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean getAnswered() {
        return answered;
    }
}

/**
 * CheckBoxQuestion class extends the Question class in order to implement
 * questions with multiple choices and multiple correct answers
 */
class CheckBoxQuestion extends Question {
    private String [] questionChoices;
    private int [] correctAnswer;

    public CheckBoxQuestion(String [] question) {
        questionClass = question[0];
        questionText = question[1];

        questionChoices = new String[4];
        for(int i=0; i<4; i++) {
            questionChoices[i] = question[i+2];
        }

        correctAnswer = new int[question.length - 6];
        for(int i=0; i<correctAnswer.length; i++) {
            correctAnswer[i] = Integer.valueOf(question[i+6]);
        }
    }

    public int[] getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getQuestionChoices() {
        return questionChoices;
    }

}

/**
 * RadioButtonQuestion class extends the Question class in order to implement
 * questions with multiple choices and only one correct answer
 */
class RadioButtonQuestion extends Question {
    private String[] questionChoices;
    private int correctAnswer;

    public RadioButtonQuestion(String[] question) {
        questionClass = question[0];
        questionText = question[1];

        questionChoices = new String[4];
        for(int i=0; i<4; i++) {
            questionChoices[i] = question[i+2];
        }

        correctAnswer = Integer.valueOf(question[6]);
        answered = false;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getQuestionChoices() {
        return questionChoices;
    }
}

/**
 * TextQuestion class extends the Question class in order to
 * implement questions that accept their answer via text
 */
class TextQuestion extends Question {
    private String correctAnswer;

    public TextQuestion(String[] question) {
        //Log.e("Inside text constructor", "question size = " + question.length);
        questionClass = question[0];
        questionText = question[1];
        correctAnswer = question[2];
        answered = false;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
