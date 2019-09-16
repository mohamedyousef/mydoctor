package com.uniyapps.yadoctor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.uniyapps.yadoctor.Fragments.CropViewFragment;
import com.uniyapps.yadoctor.Fragments.HomeFragment;
import com.uniyapps.yadoctor.Fragments.LoginAndSignUpFragment;
import com.uniyapps.yadoctor.Fragments.NearByFragment;
import com.uniyapps.yadoctor.Fragments.ProfileFragment;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View viewActionBar = getLayoutInflater().inflate(R.layout.title_action_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView textviewTitle = viewActionBar.findViewById(R.id.mytext);
        textviewTitle.setText(getString(R.string.homepage));
        actionBar.setCustomView(viewActionBar,params);

        loadfragment(new HomeFragment());
        bottomNavigationView =  findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        menu.findItem(R.id.logout).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(getIntent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
        @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;

            switch (menuItem.getItemId()){
                case R.id.navigation_profile:
                    fragment = check();
                    break;

                case R.id.navigation_home :
                    fragment = new HomeFragment();
                    break;

                case R.id.navigation_nearby :
                    fragment  = new NearByFragment();
                    break;


            }
            loadfragment(fragment);
            return true;
        }
    private void loadfragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.my_nav_host_fragment, fragment)
                    .commit();
        }
    }
    private Fragment check(){
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            return  new ProfileFragment();
        else
            return  new LoginAndSignUpFragment();
    }


}
