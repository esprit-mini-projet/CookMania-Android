package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.services.UserService;

public class EmailLoginFragment extends Fragment {

    private static final String TAG = "EmailLoginFragment";

    private TextInputLayout mEmailLayout;
    private Button mNextButton;
    private LinearLayout mProgressBar;
    private EmailLoginFragmentCallBack mCallBack;
    private ImageButton mBackButton;

    public static EmailLoginFragment newInstance(EmailLoginFragmentCallBack callBack) {

        Bundle args = new Bundle();

        EmailLoginFragment fragment = new EmailLoginFragment();
        fragment.setArguments(args);
        fragment.setCallBack(callBack);
        return fragment;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_email_login, container, false);

        mEmailLayout = v.findViewById(R.id.activity_main_email_input_layout);
        mNextButton = v.findViewById(R.id.activity_main_next_button);
        mProgressBar = v.findViewById(R.id.fragment_email_login_progress_bar);
        mBackButton = v.findViewById(R.id.fragment_email_login_back_btn);

        mBackButton.setOnClickListener(v1 -> {
            hideSoftKeyboard();
            getActivity().onBackPressed();
        });

        mEmailLayout.requestFocus();
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailLayout.getEditText().getText().toString();
                if(email.isEmpty()) {
                    mEmailLayout.getEditText().setError(getString(R.string.empty_field_warning));
                    return;
                }
                if(!isValidEmail(email)){
                    mEmailLayout.getEditText().setError(getString(R.string.email_not_valid));
                    return;
                }
                showProgressBar();
                UserService.getInstance().checkEmail(email, new UserService.CheckEmailCallBack() {
                    @Override
                    public void onCompletion(Boolean exists) {
                        hideProgressBar();
                        if(exists == null){
                            showAlert();
                            return;
                        }
                        if(!exists){
                            mCallBack.register(email);
                            return;
                        }
                        Log.d(TAG, "onCompletion: user exists");
                        mCallBack.continueToPassword(email);
                    }
                });
            }
        });

        return v;
    }

    private void showAlert(){
        new AlertDialog.Builder(getActivity())
                .setMessage("We're sorry. An error has occured!")
                .setTitle("Connection problem")
                .setPositiveButton("ok", null)
                .show();
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        showSoftKeyboard();
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        mEmailLayout.getEditText().requestFocus();
        imm.showSoftInput(mEmailLayout.getEditText(), InputMethodManager.SHOW_FORCED);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailLayout.getEditText().getWindowToken(), 0);
    }

    public interface EmailLoginFragmentCallBack{
        void register(String email);
        void continueToPassword(String email);
    }

    public EmailLoginFragmentCallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(EmailLoginFragmentCallBack callBack) {
        mCallBack = callBack;
    }
}
