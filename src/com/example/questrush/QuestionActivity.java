package com.example.questrush;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private String qestionId;

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
                if (e == null) {
                    if (list.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "This Quest doesn not contain tasks!", Toast.LENGTH_LONG).show();
                        finish();
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
        TextView description = (TextView) findViewById(R.id.questionNubmer);
        description.setText("Question #" + currentQuestionNumber);
        ++currentQuestionNumber;
        TextView questionText = (TextView) findViewById(R.id.question_text);
        questionText.setText(nextQuestion.getString("question"));
        qestionId = nextQuestion.getObjectId();
        return true;
    }

    public void Scan(View view) {
        Intent scan = new Intent(getApplicationContext(), AnswerScan.class);
        scan.putExtra("result", qestionId);
        startActivityForResult(scan, 1, null);
    }


    public void onSubmit() {
//          get next question from list and fill form
        if (true) {
            if (!setNextQuestion()) {
                startActivity(new Intent(getApplicationContext(), FinishActivity.class));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
//            Toast.makeText(getApplicationContext(), data.getExtras().getString("code"), Toast.LENGTH_SHORT).show();
            System.out.println("ok");
            onSubmit();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle(getString(R.string.quit_title));
        quitDialog.setMessage(getString(R.string.quit_message));
        quitDialog.setCancelable(false);
        quitDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        quitDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                QuestionActivity.this.finish();
            }
        });

        AlertDialog quit = quitDialog.create();
        quit.show();
    }
}