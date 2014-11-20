package com.testplus.dzikovski.ivan.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;


public class QuestionActivity extends Activity {

    public ProgressDialog progressWaitingQuestion;
    public ViewGroup container;
    public AutoResizeTextView tvQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        progressWaitingQuestion=new ProgressDialog(this);
        progressWaitingQuestion.setTitle(getString(R.string.dialog_waiting_title));
        progressWaitingQuestion.setMessage("Waiting for question...");
        progressWaitingQuestion.setCanceledOnTouchOutside(false);
        progressWaitingQuestion.show();

        //ViewGroup container=(ViewGroup)findViewById(R.id.flQuestion);

        //AutoResizeTextView questionText=new AutoResizeTextView(this);

        /*questionText.setMaxLines(20);
        questionText.setTextSize(150);
        questionText.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        questionText.setText("A machine took 200 sec to sort 200 names, using bubble sort. In 800 sec, it can approximately sort?");

        container.addView(questionText);*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
