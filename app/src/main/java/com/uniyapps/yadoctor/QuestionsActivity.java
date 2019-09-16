package com.uniyapps.yadoctor;

import android.app.SearchManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import com.uniyapps.yadoctor.Adapter.MyAdapter;
import com.uniyapps.yadoctor.Controllers.QuestionsController;
import com.uniyapps.yadoctor.Interfaces.IResultQuestions;
import com.uniyapps.yadoctor.Model.Question;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private SearchView searchView;
    ArrayList<Question>questions = new ArrayList<>();
    boolean state ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("اختبار من خلال الاسئلة");
        final LinearLayout root = findViewById(R.id.progress_loading);
        state = getIntent().getBooleanExtra("state",false);
        recyclerView = findViewById(R.id.questionslist);

        QuestionsController.getInstance().loadQuestions(new IResultQuestions() {
            @Override
            public void on_result(ArrayList<Question> results) {
                MyAdapter adapter = new MyAdapter(results,getSupportFragmentManager(),state);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(QuestionsActivity.this,2));
                recyclerView.setVisibility(View.VISIBLE);
                root.setVisibility(View.GONE);
                questions = results;
                if (state) {
                   String title =getIntent().getStringExtra("title");
                    search(title.trim());
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview_menu,menu);
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return false;
            }
        });
        return true;
}
    void search(String s){
    ArrayList<Question>questions2 = new ArrayList<>();
    for (Question question : questions) {
        if (question.getTitle().trim().contains(s))
            questions2.add(question);
    }
    recyclerView.setAdapter(new MyAdapter(questions2,getSupportFragmentManager(),state));

}

}
