package com.example.questrush;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Random;

/**
 * Created by skyjack on 20.02.15.
 */
public class QuestionActivity extends Activity {
    private List<ParseObject> questionList = null;
    private Random randMachine = null;
    private int currentQuestionNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);

        currentQuestionNumber = 1;
        final ParseQuery<ParseObject> questions = new ParseQuery<ParseObject>("questions");
        questions.whereEqualTo("quest_id", getIntent().getStringExtra("quest_id"));
        questions.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if ( e == null) {
                    if (list.isEmpty()) {
                        throw new RuntimeException("Empty question list received");
                    }
                    questionList = list;
                    randMachine = new Random();
                    setNextQuestion();

                } else {

                    // TODO: Add relevant exception handling
                    Toast.makeText(getApplicationContext(), "Failed ot retrieve quest questions", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private boolean setNextQuestion() {
        if (questionList.isEmpty()) {
            return false;
        }
        int nextQuestionNumber = randMachine.nextInt(questionList.size());
        ParseObject nextQuestion = questionList.get(nextQuestionNumber);
        questionList.remove(nextQuestionNumber);
        TextView description = (TextView)findViewById(R.id.questionDescription);
        description.setText("Question #" + currentQuestionNumber);
        ++currentQuestionNumber;
        TextView questionText = (TextView)findViewById(R.id.questionText);
        questionText.setText(nextQuestion.getString("question"));
        return true;
    }

    public void onSubmit(View view) {
        // get next question from list and fill form
        if(true) {
            if( !setNextQuestion() ) {
                Toast.makeText(getApplicationContext(),
                        "You could drink or smoke but you decided to play n Quest!" +
                                "Congratulations! You have finished it!!",
                        Toast.LENGTH_LONG).show();
                //TODO: Show congratulations activity
            }
        }
    }
}