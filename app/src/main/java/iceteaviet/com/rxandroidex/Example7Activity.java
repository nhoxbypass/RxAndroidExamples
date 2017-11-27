package iceteaviet.com.rxandroidex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

public class Example7Activity extends AppCompatActivity {
    private static final String TAG = Example7Activity.class.getSimpleName();
    @BindView(R.id.rv_kingsman_list)
    protected RecyclerView kingsmanList;
    @BindView(R.id.btn_subscribe)
    protected Button subscribeButton;
    @BindView(R.id.btn_connect)
    protected Button connectButton;
    @BindView(R.id.btn_subscribe_cold)
    protected Button coldSubscribeButton;
    @BindView(R.id.btn_convert)
    protected Button convertButton;

    private Observable<List<String>> listObservable;
    private SimpleStringAdapter kingsmanAdapter;
    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example7);

        ButterKnife.bind(this);

        kingsmanList.setLayoutManager(new LinearLayoutManager(this));
        kingsmanAdapter = new SimpleStringAdapter(this);
        kingsmanList.setAdapter(kingsmanAdapter);

        restClient = new RestClient(this);

        listObservable = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                Log.d(TAG, "call() thread: " + Thread.currentThread().getName());
                return restClient.getFavouriteMovies();
            }
        });
        //// convert Observable to ConnectedObservable, get a reference so we can call connect() later
        final ConnectableObservable<List<String>> connectableObservable = listObservable.publish();
        listObservable = null;

        // define 1st observer
        final Observer<List<String>> observer1 = new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: " + 1 + ". Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(List<String> s) {
                Log.d(TAG, "onNext 1: " + s + ". Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError 1: " + e.getMessage() + ". Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete 1" + ". Thread: " + Thread.currentThread().getName());
            }
        };

        // define 2nd observer here
        final Observer<List<String>> observer2 = new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: " + 2 + ". Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(List<String> s) {
                Log.d(TAG, "onNext 2: " + s + ". Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError 2: " + e.getMessage() + ". Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete 2" + ". Thread: " + Thread.currentThread().getName());
            }
        };

        // define 3rd observer here
        final Observer<List<String>> observer3 = new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: " + 3 + ". Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(List<String> s) {
                Log.d(TAG, "onNext 3: " + s + ". Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError 3: " + e.getMessage() + ". Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete 3" + ". Thread: " + Thread.currentThread().getName());
            }
        };

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // observer is subscribing
                connectableObservable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer1);
                connectableObservable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer2);
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initiate the network request
                connectableObservable.connect();
            }
        });

        coldSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listObservable != null) {
                    listObservable.subscribe(observer3);
                } else {
                    Log.e(TAG, "Observable not convert to COLD yet");
                }
            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listObservable = connectableObservable.autoConnect();
                Log.d(TAG, "Converted back to normal COLD observable");
            }
        });

        //Hot observable is ...
        //When click subscribe button before click connect
        //                  -> onSubscribe: 1. Thread: main
        //                  -> onSubscribe: 2. Thread: main
        //Here if it's a normal observable it will emit event that call() and return a List<String> IMMEDIATELY
        //BUT
        // it's not because it's a HOT observable, it does not related to the subscription, but emit value whenever connect() get called
        //SO
        // When click connect later
        //                  ->  call() thread: main
        //                  -> onNext 1: [Kingsman, Starwars, Harry Potter, Geo Storm, Spider man, Wonder woman, Thor]. Thread: main
        //                     onComplete 1. Thread: main
        //                  -> onNext 2: [Kingsman, Starwars, Harry Potter, Geo Storm, Spider man, Wonder woman, Thor]. Thread: main
        //                     onComplete 2. Thread: main
        // IF
        // Call connect() before subscribing -> no onNext() will be called.

        //Cold observable is ...
        // Convert back to COLD observable from HOT observable using autoConnect()
        //After this, everything will become the Example 2.
    }
}
