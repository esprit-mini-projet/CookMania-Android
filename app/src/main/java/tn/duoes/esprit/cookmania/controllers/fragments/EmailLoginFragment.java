package tn.duoes.esprit.cookmania.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tn.duoes.esprit.cookmania.R;

public class EmailLoginFragment extends Fragment {

    private static final String TAG = "EmailLoginFragment";

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



        return v;
    }
}
