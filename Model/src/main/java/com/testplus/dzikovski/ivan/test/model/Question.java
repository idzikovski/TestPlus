package com.testplus.dzikovski.ivan.test.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ivan on 18.11.2014.
 */
public class Question implements Serializable {

    private static final long serialVersionUID = 722776147L;
    private String question;
    private ArrayList<Answer> answers;

    public Question(String question, ArrayList<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
}