package com.uniyapps.yadoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.uniyapps.yadoctor.Controllers.UserController;
import com.uniyapps.yadoctor.Interfaces.IUserResult;


public class UserActivity extends AppCompatActivity {

    String uid;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        uid = FirebaseAuth.getInstance().getUid();
        editText = findViewById(R.id.user_name_txt);
    }
    public void bu_finish(View view) {
        UserController.getInstance().addUser(uid,editText.getText().toString(), new IUserResult() {
            @Override
            public void on_result(boolean result) {
                if (result) {
                    Intent intent = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                }else
                Toast.makeText(UserActivity.this, "تاكد من اتصالاك باالانترنت", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
