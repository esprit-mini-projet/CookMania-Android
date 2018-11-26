package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import tn.duoes.esprit.cookmania.R;

public class EmailLoginFragment extends Fragment {

    private static final String TAG = "EmailLoginFragment";

    private TextInputLayout mEmailLayout;
    private TextInputLayout mUsernameLayout;
    private TextInputLayout mPasswordLayout;
    private TextInputLayout mConfirmPasswordLayout;
    private Button mActionButton;

    public static EmailLoginFragment newInstance() {

        Bundle args = new Bundle();

        EmailLoginFragment fragment = new EmailLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_email_login, container, false);

        mEmailLayout = v.findViewById(R.id.activity_main_email_input_layout);
        mUsernameLayout = v.findViewById(R.id.activity_main_username_input_layout);
        mPasswordLayout = v.findViewById(R.id.activity_main_password_input_layout);
        mConfirmPasswordLayout = v.findViewById(R.id.activity_main_password_confirm_input_layout);
        mActionButton = v.findViewById(R.id.activity_main_action_button);

        

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
