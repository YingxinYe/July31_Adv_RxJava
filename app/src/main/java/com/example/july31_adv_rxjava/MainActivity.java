package com.example.july31_adv_rxjava;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> observable = mObservable();
        Observer<String> observer = getObserver();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    //filter is the operator that will help us or change the emitted data
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("a");
                    }
                })
                .subscribeWith(observer);


    }

    private Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(String s) {
                Log.i("winnie", s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private Observable<String> mObservable() {
        return Observable.fromArray("baba", "nana", "apples", "winnie", "apple", "apply", "appini");
    }
}
