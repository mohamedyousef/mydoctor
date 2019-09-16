package com.uniyapps.yadoctor.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.uniyapps.yadoctor.Controllers.UserController;
import com.uniyapps.yadoctor.Interfaces.IUserResult;
import com.uniyapps.yadoctor.R;
import com.uniyapps.yadoctor.UserActivity;

import java.util.Arrays;

public class LoginAndSignUpFragment extends Fragment {

    private static final int RC_SIGN_IN = 123;
    RelativeLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        return inflater.inflate(R.layout.fragment_login_and_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button google = view.findViewById(R.id.google_btn)  , phone = view.findViewById(R.id.phone_button);
        layout = view.findViewById(R.id.root);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build()
                                ))
                                .build(),
                        RC_SIGN_IN);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.PhoneBuilder().build()
                                ))
                                .build(),
                        RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == getActivity().RESULT_OK) {
                final String uid = FirebaseAuth.getInstance().getUid();
                UserController.getInstance().checkUserId(uid, new IUserResult() {
                    @Override
                    public void on_result(boolean result) {
                        if (result==false)
                        {
                            Intent intent = new Intent(getActivity(), UserActivity.class);
                            startActivity(intent);
                           getActivity().finish();
                        }else
                            startActivity(getActivity().getIntent());

                    }
                });
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("تاكد من اتصلاك بالانترنت");
                    return;
                }
            }
        }
    }
    @SuppressLint("WrongConstant")
    void showSnackbar(String txt ) {
        Snackbar.make(layout, txt, BaseTransientBottomBar.LENGTH_SHORT).show();
    }
         //switching fragment
}
