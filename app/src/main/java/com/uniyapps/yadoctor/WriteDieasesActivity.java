package com.uniyapps.yadoctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uniyapps.yadoctor.Fragments.BottomResult;

import java.util.ArrayList;

public class WriteDieasesActivity extends AppCompatActivity {

    ArrayList<String>dieases = new ArrayList<>();
    ListView listView ;
    ArrayAdapter<String> arrayAdapter;
    AutoCompleteTextView title ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_dieases);
        listView = findViewById(R.id.list_a3rad);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,dieases);
        listView.setAdapter(arrayAdapter);
        title = findViewById(R.id.autoCompleteTextView);
    }


    public void BuAddListview(View view) {
        dieases.add(title.getText().toString());
        arrayAdapter.notifyDataSetChanged();
    }

    public void buOnSend(View view) {
        String txt  = "";
        for (String t : dieases)
            txt += ","+t;

        FirebaseFirestore.getInstance().collection("Write").whereEqualTo("ds",txt).limit(3).get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String a  = " " ;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments())
                {
                      a += documentSnapshot.getId()+",";
                }
                Show(a);
            }
        });
    }

    void Show(String res){
        final Bundle bundle = new Bundle();
        bundle.putString("res",res);
        BottomResult bottomResult = new BottomResult();
        bottomResult.setArguments(bundle);
        bottomResult.show(getSupportFragmentManager(),"bottomres");
    }
}
