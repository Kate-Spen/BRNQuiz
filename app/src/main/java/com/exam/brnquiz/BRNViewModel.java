package com.exam.brnquiz;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BRNViewModel extends AndroidViewModel {

    public final MutableLiveData<String> question = new MutableLiveData<>();
    public MutableLiveData<List<String>> answers = new MutableLiveData<>();
    public final MutableLiveData<Integer> score = new MutableLiveData<>();
    public final List<Question> questionPool = new ArrayList<>();
    private int currentQuestionIndex = 0;
    public int correctIndex = 0;
    private final SharedPreferences prefs;
    private static final String PREFS_NAME = "quizPrefs";
    private static final String KEY_SCORE = "score";

    public BRNViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences(PREFS_NAME, Application.MODE_PRIVATE);
        score.setValue(prefs.getInt(KEY_SCORE, 0));

        prepareQuestions();
        shuffleQuestions();
        loadCurrentQuestion();
    }
    private void prepareQuestions() {
        questionPool.add(new Question("What is the capital of Colombia?", new String[]{"Medellin", "Madrid", "Paris", "Bogota"}, 1));
        questionPool.add(new Question("Which language runs on the JVM?", new String[]{"C++", "Python", "Java", "Go"}, 2));
        questionPool.add(new Question("2 + 2 * 2 = ?", new String[]{"6", "8", "4", "2"}, 0));
        questionPool.add(new Question("Which is an Android UI element?", new String[]{"div", "ListView", "span", "fieldset"}, 1));
    }

    private void shuffleQuestions(){
        Collections.shuffle(questionPool);
        currentQuestionIndex = 0;
    }
    public void loadCurrentQuestion() {
        if (questionPool.isEmpty()){
            question.setValue("No questions");
            answers.setValue(Collections.singletonList("N/A"));
            correctIndex = 0;
            return;
        }
        Question q = questionPool.get(currentQuestionIndex);
        question.setValue(q.text);

        List<String> ansList = new ArrayList<>();
        Collections.addAll(ansList, q.answers);
        List<Integer> idxs = new ArrayList<>();
        for (int i = 0; i < ansList.size(); i++) idxs.add(i);
        Collections.shuffle(idxs);

        List<String> shuffled = new ArrayList<>();
        int newCorrect = 0;
        for (int i = 0; i < idxs.size(); i++){
            int orig = idxs.get(i);
            shuffled.add(ansList.get(orig));
            if (orig == q.correct) newCorrect = i;
        }
        answers.setValue(shuffled);
        correctIndex = newCorrect;
    }
    public void nextQuestion(){
        currentQuestionIndex++;
        if(currentQuestionIndex >= questionPool.size()){
            shuffleQuestions();
        }
        loadCurrentQuestion();
    }

    public void submitAnswer(int selectedIndex){
        if(selectedIndex == correctIndex){
            int newScore = getScore() + 1;
            score.setValue(newScore);
            prefs.edit().putInt(KEY_SCORE, newScore).apply();
        }
    }
    public int getScore(){
        return score.getValue() == null ? 0 : score.getValue();
    }

    public void resetScore(){
        score.setValue(0);
        prefs.edit().putInt(KEY_SCORE, 0).apply();
    }
//    public boolean getDarkModePref() {
//        return prefs.getBoolean(KEY_DARK_MODE, false);
//    }
//
//    public void setDarkModePref(boolean enabled) {
//        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
//    }

    private static class Question{
        final String text;
        final String[] answers;
        final int correct;

        Question(String text, String[] answers, int correct){
            this.text = text;
            this.answers = answers;
            this.correct = correct;
        }
    }
}
