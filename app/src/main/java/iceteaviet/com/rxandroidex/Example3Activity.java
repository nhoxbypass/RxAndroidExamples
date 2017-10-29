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
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Example3Activity extends AppCompatActivity {
    private static final String TAG = Example3Activity.class.getSimpleName();

    @BindView(R.id.btn_subscribe) protected Button btnSubscribe;
    @BindView(R.id.loader) protected ProgressBar progressBar;
    @BindView(R.id.rv_movie_list) protected RecyclerView rvMovieList;
    @BindView(R.id.tv_placeholder) protected TextView tvPlaceholder;

    private Single<List<String>> movieListSingle;
    private RestClient mRestClient;
    private SimpleStringAdapter movieAdapter;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example3);
        ButterKnife.bind(this);

        mRestClient = new RestClient(this);
        movieAdapter = new SimpleStringAdapter(this);

        rvMovieList.setAdapter(movieAdapter);
        rvMovieList.setLayoutManager(new LinearLayoutManager(this));

        /*A Single is something like an Observable
        * but instead of emitting a series of values — anywhere from none at all to an infinite number
        * it always either emits one value or an error notification.
        * For this reason:
        * instead of Observable (onNext, onError, and onCompleted)
        * there are only two callbacks: onSuccess() and onError()
        * @see: http://reactivex.io/documentation/single.html/
        */
        movieListSingle = Single.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                //return mRestClient.getFavouriteMovies();
                return mRestClient.getFavouriteMoviesWithExeption();
            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvMovieList.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tvPlaceholder.setVisibility(View.GONE);

                movieListSingle.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<String>>() {
                            public void onSubscribe(Disposable d) {
                                disposable = d;
                                Log.d(TAG, "onSubscribe: " + d.getClass().toString());
                            }

                            @Override
                            public void onSuccess(List<String> strings) {
                                Log.d(TAG, "onSuccess() thread: " + Thread.currentThread().getName());
                                displayMovies(strings);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(Example3Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                tvPlaceholder.setVisibility(View.VISIBLE);
                                rvMovieList.setVisibility(View.GONE);
                                Log.d(TAG, "onError: " + e.getMessage());
                            }
                        });
            }
        });
    }

    private void displayMovies(List<String> movies) {
        movieAdapter.setStrings(movies);
        progressBar.setVisibility(View.GONE);
        rvMovieList.setVisibility(View.VISIBLE);
    }
}
