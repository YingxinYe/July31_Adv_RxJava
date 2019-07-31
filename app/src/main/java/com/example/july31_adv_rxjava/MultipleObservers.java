package com.example.july31_adv_rxjava;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MultipleObservers extends AppCompatActivity {

    CompositeDisposable disposable; //don't have to dispose one by one. It will dispose a number of subscrption at one time

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable=new CompositeDisposable();

        //Observable<String> mObservable = getObservable();
        Observable<User> mObservable = getObservable();


//        DisposableObserver<String> mObserver1=getObserverA();
//        DisposableObserver<String> mObserver2=getObserverB();
        DisposableObserver<User> mObserver1=getObserverA();
        DisposableObserver<User> mObserver2=getObserverB();


        disposable.add(mObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(
                new Predicate<User>() {
                    @Override
                    public boolean test(User user) throws Exception {
                        return user.name.toLowerCase().startsWith("m");
                    }
                }
        ).subscribeWith(mObserver1));

        disposable.add(mObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<User>() {
            @Override
            public boolean test(User user) throws Exception {
                return user.name.toLowerCase().startsWith("h");
            }
        }).subscribeWith(mObserver2));

//        disposable.add(mObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<String>() {
//            @Override
//            public boolean test(String s) throws Exception {
//                return s.toLowerCase().startsWith("a");
//            }
//        }).subscribeWith(mObserver1));
//
//
//        disposable.add(mObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<String>() {
//            @Override
//            public boolean test(String s) throws Exception {
//                return s.toLowerCase().startsWith("b");
//            }
//        }).map(new Function<String, String>() {  //modify the filter, is a operator for the result
//
//            @Override
//            public String apply(String s) throws Exception {
//                return s.replace('B','C').toUpperCase(); //oldChar是对照原来的，不是filter过的
//            }
//        }).subscribeWith(mObserver2));
    }

    private DisposableObserver<User> getObserverA(){
        return new DisposableObserver<User>(){

            @Override
            public void onNext(User user) {
                Log.i("winnie", user.name+","+user.age);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<User> getObserverB(){
       return new DisposableObserver<User>() {
           @Override
           public void onNext(User user) {
               Log.i("winnie", user.name+","+user.age);
           }

           @Override
           public void onError(Throwable e) {

           }

           @Override
           public void onComplete() {

           }
       };
    }

//    private DisposableObserver<String> getObserverA(){
//        return new DisposableObserver<String>() {
//            @Override
//            public void onNext(String s) {
//                Log.i("winnie1","A:"+s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                Log.i("winnie1","A: Complete");
//
//            }
//        };
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

//    private DisposableObserver<String> getObserverB(){   //there is no onSubcribe method b/c we subscribe with the disposable
//        return new DisposableObserver<String>() {
//            @Override
//            public void onNext(String s) {
//                Log.i("winnie2","B:"+s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                Log.i("winnie1","B: Complete");
//            }
//        };
//    }
    private Observable<User> getObservable(){

        final ArrayList<User> mlist=new ArrayList<>();
        User user1=new User("miki","22");
        User user2=new User("mikcy","25");
        User user3=new User("haha","33");
        User user4=new User("henry","33");
        User user5=new User("wenry","33");
        mlist.add(user1);
        mlist.add(user2);
        mlist.add(user3);
        mlist.add(user4);
        mlist.add(user5);
        //return Observable.fromArray(user1, user2, user3, user4);
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                for(User u: mlist){
                    if(!emitter.isDisposed()){
                        emitter.onNext(u);
                    }
                }
                if(!emitter.isDisposed()){
                    emitter.onComplete();
                }
            }
        });
        //return Observable.fromArray("Apple","Ankle","Azon","Aman","Altanta","Boy","Big","Ballon","Cana","Can","Cat");

    }
}
