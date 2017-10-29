package iceteaviet.com.rxandroidex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Example2Activity extends AppCompatActivity {
    private static final String TAG = Example2Activity.class.getSimpleName();
    private Observable<List<String>> movieListObservable;
    private Button btnSubscribe;
    private ProgressBar progressBar;
    private RecyclerView rvMovieList;
    private TextView tvPlaceholder;

    private RestClient mRestClient;
    private SimpleStringAdapter movieAdapter;
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example2);
        btnSubscribe = (Button) findViewById(R.id.btn_subscribe);
        progressBar = (ProgressBar) findViewById(R.id.loader);
        rvMovieList = (RecyclerView) findViewById(R.id.rv_movie_list);
        tvPlaceholder = findViewById(R.id.tv_placeholder);

        mRestClient = new RestClient(this);
        movieAdapter = new SimpleStringAdapter(this);

        rvMovieList.setAdapter(movieAdapter);
        rvMovieList.setLayoutManager(new LinearLayoutManager(this));

        //If we use Observable.just() the item will be evaluated immediately and block UI thread
        //Observable.fromCallable() allows us to delay the creation of a value to be emitted by an Observable.
        // This is handy when the value you want to emit from your Observable
        // needs to be created off of the UI thread.
        movieListObservable = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                //This will be called whenever an Observer subscribe to this Observable
                //And this will run on another thread provided in subscribeOn()
                Log.d(TAG, "call() thread: " + Thread.currentThread().getName());
                //Thread.sleep(10000);
                return mRestClient.getFavouriteMovies();
            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvMovieList.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tvPlaceholder.setVisibility(View.GONE);

                Log.d(TAG, "onClick() thread: " + Thread.currentThread().getName());
                movieListObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<String>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable = d;
                                Log.d(TAG, "onSubscribe: " + d.getClass().toString());
                            }

                            @Override
                            public void onNext(@NonNull List<String> strings) {
                                Log.d(TAG, "onNext() thread: " + Thread.currentThread().getName());
                                displayMovies(strings);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, "onError: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete");
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        //unsubscribe our Observers
        //in order to prevent nasty things from happening
        // when we’re using Observables to load things asynchronously.
        //Such as: memory leaks and NullPointerExceptions.
        if (disposable != null && !disposable.isDisposed()) {
            Log.e(TAG, "dispose Observer");
            disposable.dispose();
        }
        super.onDestroy();
    }

    private void displayMovies(List<String> movies) {
        movieAdapter.setStrings(movies);
        progressBar.setVisibility(View.GONE);
        rvMovieList.setVisibility(View.VISIBLE);
    }
}
