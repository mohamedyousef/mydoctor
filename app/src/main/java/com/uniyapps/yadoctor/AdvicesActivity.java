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

import com.uniyapps.yadoctor.Adapter.AdapterForAdvices;
import com.uniyapps.yadoctor.Adapter.MyAdapter;
import com.uniyapps.yadoctor.Controllers.QuestionsController;
import com.uniyapps.yadoctor.Interfaces.IAdvices;
import com.uniyapps.yadoctor.Model.Question;

import java.util.ArrayList;

public class AdvicesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private SearchView searchView;
    ArrayList<String>questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advices);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("النصائح المهمه للتعامل مع الامراض");
        recyclerView = findViewById(R.id.advices_recyclearview);
        final LinearLayout root = findViewById(R.id.progress_loading_advices);

        QuestionsController.getInstance().loadAdvices(new IAdvices() {
            @Override
            public void on_result(ArrayList<String> results) {
                AdapterForAdvices adapter = new AdapterForAdvices(results);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(AdvicesActivity.this,2));
                recyclerView.setVisibility(View.VISIBLE);
                root.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<String> questions2 = new ArrayList<>();
                for (String question : questions) {
                    if (question.trim().contains(s))
                        questions2.add(question);
                }
               // recyclerView.setAdapter(new MyAdapter(questions2,getSupportFragmentManager()));
                return false;
            }
        });
        return true;
    }

}
