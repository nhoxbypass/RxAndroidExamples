package iceteaviet.com.rxandroidex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iceteaviet.com.rxandroidex.adapter.SimpleStringAdapter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/*
* Created by Genius Doan on 20/10/2017
*
* Observables are primarily defined by their behavior upon subscription.
* */
public class Example1Activity extends AppCompatActivity {
    private static final String TAG = Example1Activity.class.getSimpleName();
    @BindView(R.id.rv_kingsman_list)
    protected RecyclerView kingsmanList;
    @BindView(R.id.btn_subscribe)
    protected Button subscribeButton;
    @BindView(R.id.tv_placeholder)
    protected TextView tvPlaceholder;
    /*
    * Observable.just() method creates an Observable such that when an Observer subscribes,
    * the onNext() of the Observer is immediately called
    * with the argument provided to Observable.just().
    * */
    private Observable<List<String>> listObservable;
    private SimpleStringAdapter kingsmanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example1);
        ButterKnife.bind(this);

        kingsmanList.setLayoutManager(new LinearLayoutManager(this));
        kingsmanAdapter = new SimpleStringAdapter(this);
        kingsmanList.setAdapter(kingsmanAdapter);

        listObservable = Observable.just(getKingsmanList());
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPlaceholder.setVisibility(View.GONE);
                kingsmanList.setVisibility(View.VISIBLE);
                listObservable.subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.getClass().toString() + ". Thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(@NonNull List<String> strings) {
                        Log.d(TAG, "onNext" + ". Thread: " + Thread.currentThread().getName());
                        kingsmanAdapter.setStrings(strings);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage() + ". Thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete" + ". Thread: " + Thread.currentThread().getName());
                    }
                });
            }
        });

        //Result after click subscribe button
        //D/Example1Activity: onSubscribe: class io.reactivex.internal.operators.observable.ObservableScalarXMap$ScalarDisposable. Thread: main
        //D/Example1Activity: onNext. Thread: main
        //D/Example1Activity: onComplete. Thread: main

    }

    private List<String> getKingsmanList() {
        return Arrays.asList(getResources().getStringArray(R.array.kingsman));
    }
}
