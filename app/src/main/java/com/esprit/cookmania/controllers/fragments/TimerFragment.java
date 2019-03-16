package com.esprit.cookmania.controllers.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.esprit.cookmania.R;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class TimerFragment extends Fragment {

    private static final String ARGS_MINUTES = "minutes";

    private CircularProgressIndicator mTimer;
    private ImageView mPlayImage;
    private ImageView mPauseImage;
    private TimerState mTimerState = TimerState.PLAY;
    private CountDownTimer mCountDownTimer;
    private TimerFragmentCallBack mCallBack;
    private MediaPlayer mMediaPlayer;

    private enum TimerState{
        PLAY, PAUSE, STOP, DONE
    }

    public static TimerFragment newInstance(int minutes, TimerFragmentCallBack callBack) {

        Bundle args = new Bundle();
        args.putInt(ARGS_MINUTES, minutes);
        TimerFragment fragment = new TimerFragment();
        fragment.setArguments(args);
        fragment.mCallBack = callBack;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onViewClicked();
            }
        });

        mTimer = view.findViewById(R.id.fragment_timer_timer);
        mPlayImage = view.findViewById(R.id.fragment_timer_play_button);
        mPauseImage = view.findViewById(R.id.fragment_timer_pause_button);
        final ImageView stopImage = view.findViewById(R.id.fragment_timer_stop_button);

        final int minutes = getArguments().getInt(ARGS_MINUTES);
        mTimer.setMaxProgress(minutes * 60);
        mTimer.setCurrentProgress(minutes * 60);
        mTimer.setProgressTextAdapter(new CircularProgressIndicator.ProgressTextAdapter() {
            @NonNull
            @Override
            public String formatText(double time) {
                if (time == 0) {
                    return "Time up!";
                }
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
        mTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Just to intercept the click event
            }
        });

        mPauseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerState != TimerState.DONE) {
                    mCountDownTimer.cancel();
                    mTimerState = TimerState.PAUSE;
                } else {
                    stopAlarm();
                }
                mPauseImage.setVisibility(View.INVISIBLE);
                mPlayImage.setVisibility(View.VISIBLE);
            }
        });

        stopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
                mCountDownTimer.cancel();
                mTimerState = TimerState.STOP;
                mTimer.setCurrentProgress(mTimer.getMaxProgress());
                mTimer.setFillBackgroundEnabled(false);
                mPauseImage.setVisibility(View.INVISIBLE);
                mPlayImage.setVisibility(View.VISIBLE);
            }
        });

        mPlayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long duration = (long) mTimer.getProgress() * 1000;
                if (mTimerState == TimerState.DONE) {
                    duration = (long) (mTimer.getMaxProgress() * 1000);
                    mTimer.setCurrentProgress(mTimer.getMaxProgress());
                }
                mTimerState = TimerState.PLAY;
                startTimer(duration);
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
                mTimerState = TimerState.DONE;
                mTimer.setCurrentProgress(0);
                launchAlarm();
            }
        };
        mCountDownTimer.start();
    }

    private void launchAlarm() {
        mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.alarm);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    private void stopAlarm() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
        stopAlarm();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCountDownTimer.cancel();
        stopAlarm();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mTimerState == TimerState.PLAY) startTimer((long) mTimer.getProgress() * 1000);
    }

    public interface TimerFragmentCallBack {
        void onViewClicked();
    }
}
