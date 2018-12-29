package tn.duoes.esprit.cookmania.controllers.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import tn.duoes.esprit.cookmania.R;

public class TimerFragment extends Fragment {

    private static final String TAG = "TimerFragment";
    private static final String ARGS_MINUTES = "minutes";

    private CircularProgressIndicator mTimer;
    private ImageView mPlayImage;
    private ImageView mPauseImage;
    private ImageView mStopImage;
    private TimerState mTimerState = TimerState.PLAY;
    private CountDownTimer mCountDownTimer;

    private enum TimerState{
        PLAY, PAUSE, STOP
    }

    public static TimerFragment newInstance(int minutes) {

        Bundle args = new Bundle();
        args.putInt(ARGS_MINUTES, minutes);
        TimerFragment fragment = new TimerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        mTimer = view.findViewById(R.id.fragment_timer_timer);
        mPlayImage = view.findViewById(R.id.fragment_timer_play_button);
        mPauseImage = view.findViewById(R.id.fragment_timer_pause_button);
        mStopImage = view.findViewById(R.id.fragment_timer_stop_button);

        final int minutes = getArguments().getInt(ARGS_MINUTES);
        mTimer.setMaxProgress(minutes * 60);
        mTimer.setCurrentProgress(minutes * 60);
        mTimer.setProgressTextAdapter(new CircularProgressIndicator.ProgressTextAdapter() {
            @NonNull
            @Override
            public String formatText(double time) {
                int minutes = (int) (time / 60);
                int seconds = (int) (time % 60);
                StringBuilder sb = new StringBuilder();
                if (minutes < 10) {
                    sb.append(0);
                }
                sb.append(minutes).append(":");
                if (seconds < 10) {
                    sb.append(0);
                }
                sb.append(seconds);
                return sb.toString();
            }
        });

        mPauseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                mTimerState = TimerState.PAUSE;
                mPauseImage.setVisibility(View.INVISIBLE);
                mPlayImage.setVisibility(View.VISIBLE);
            }
        });

        mStopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                mTimerState = TimerState.STOP;
                mTimer.setCurrentProgress(minutes * 60);
                mPauseImage.setVisibility(View.INVISIBLE);
                mPlayImage.setVisibility(View.VISIBLE);
            }
        });

        mPlayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerState = TimerState.PLAY;
                startTimer((long)mTimer.getProgress() * 1000);
                mPauseImage.setVisibility(View.VISIBLE);
                mPlayImage.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void startTimer(long totalMillis){
        mCountDownTimer = new CountDownTimer(totalMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimer.setCurrentProgress(mTimer.getProgress() - 1);
            }

            @Override
            public void onFinish() {

            }
        };
        mCountDownTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCountDownTimer.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mTimerState == TimerState.PLAY) startTimer((long) mTimer.getProgress() * 1000);
    }
}
