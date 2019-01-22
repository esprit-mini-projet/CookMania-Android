package tn.duoes.esprit.cookmania.helpers;

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

    public static InternetConnectivityObserver get() {
        if (instance == null) {
            instance = new InternetConnectivityObserver();
        }
        return instance;
    }

    private InternetConnectivityObserver() {

    }

    public void start(Consumer consumer) {
        mDisposable = ReactiveNetwork
                .observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
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

    public interface Consumer {
        void accept(boolean isConnected);
    }
}
