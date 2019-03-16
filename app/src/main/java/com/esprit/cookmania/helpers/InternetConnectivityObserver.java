package com.esprit.cookmania.helpers;

import android.util.Log;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InternetConnectivityObserver {

    private static final String TAG = "InternetConnectivityObs";

    private static InternetConnectivityObserver instance;
    private Disposable mDisposable;
    private Consumer mConsumer;

    public static InternetConnectivityObserver get() {
        if (instance == null) {
            instance = new InternetConnectivityObserver();
        }
        return instance;
    }

    private InternetConnectivityObserver() {

    }

    public void start() {
        mDisposable = ReactiveNetwork
                .observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> getConsumer().accept(isConnected));
        Log.i(TAG, "start: ");
    }

    public void startOnce(Consumer consumer) {
        Single<Boolean> single = ReactiveNetwork.checkInternetConnectivity();
        mDisposable = single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
    }

    public void stop() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        Log.i(TAG, "stop: ");
    }

    public Consumer getConsumer() {
        return mConsumer;
    }

    public void setConsumer(Consumer consumer) {
        mConsumer = consumer;
    }

    public interface Consumer {
        void accept(boolean isConnected);
    }
}
