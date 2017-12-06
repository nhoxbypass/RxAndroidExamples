package iceteaviet.com.rxandroidex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import iceteaviet.com.rxandroidex.adapter.SimpleStringAdapter;
import iceteaviet.com.rxandroidex.rest.RestClient;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

//Let’s make an Activity that can help a user search for cities by name
//We’re also going to add a new concept: debounce
//We don’t want to send out a request to the server on every single keystroke when user typing too fast
//We’d like to wait a little bit for the user to stop typing (so that we’re sure we’ve got a good query)
public class Summary1Activity extends AppCompatActivity {
    private static final String TAG = Summary1Activity.class.getSimpleName();

    @BindView(R.id.ed_search)
    protected EditText edSearch;
    @BindView(R.id.rv_search_result)
    protected RecyclerView rvSearchResult;
    @BindView(R.id.tv_no_result)
    protected TextView tvNoResult;

    private PublishSubject<String> searchSubject;
    private Disposable disposable;
    private RestClient restClient;
    private SimpleStringAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary1);
        ButterKnife.bind(this);

        cityAdapter = new SimpleStringAdapter(this);
        rvSearchResult.setAdapter(cityAdapter);
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        restClient = new RestClient(this);
        searchSubject = PublishSubject.create();


        //Here we use debounce for our PublishSubject
        // tells PublishSubject to only emit the last value that came into it
        // after nothing new has come into this PublishSubject for N milliseconds
        // this means our subject won’t emit the search string until the user hasn’t changed the string for N milliseconds
        // this means the space between 2 request is at least N milliseconds
        //  at the end of the 400 milliseconds it will only emit the latest search string the user entered

        //Eg: We typed a -> (emit 'a' event to PublishSubject) -> ab -> abc -> abcd
        // But the b,c,d we type too fast within N milis
        // So 'ab', 'abc' will not be emitted
        // After N milis since 'a' event emitted to PublishSubject -> 'abcd' is the latest and it will be emit to PublishSubject
        final Observable<List<String>> newSearchSubject = searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io()) //Since querying is an IO operation we need to observe the emissions of debounce on the IO Scheduler
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(String s) throws Exception {
                        //Only call after N milis delay between 2 call
                        //This means: onTextChange -> push new value every time type, but apply() is not get called every time like this :)
                        Log.d(TAG, "apply" + s + ". Thread: " + Thread.currentThread().getName());
                        //“map” our search queries to a list of search results
                        //Because map can run any arbitrary function
                        // we’ll use our RestClient to transform our search query into the list of actual results we want to display.
                        return restClient.searchForCity(s);
                    }
                });

        //Since our map was run on the IO Scheduler
        //BUT
        // we want to use the results it emits to populate our views,  we then need to switch back to the UI thread
        // SO
        // We split to another observable (you can merge too but I split for easy to understand, because it's do 2 separate job)
        // we add an observeOn(AndroidSchedulers.mainThread())
        // Now we’ve got the search results being emitted on the UI thread

        // Note the ordering of all our observerOn()s here.
        // They’re critical.
        newSearchSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        Log.d(TAG, "onSubscribe. Thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        Log.d(TAG, "onNext. Thread: " + Thread.currentThread().getName());
                        handleSearchResults(strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage() + ". Thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete. Thread: " + Thread.currentThread().getName());
                    }
                });


        //The flow of 2 observable:
        //
        //   Receive onTextChange
        //           |
        //      searchSubject
        //           |
        //           |
        //           V
        //        debounce
        //          |||
        //          |||
        //           V
        //          map (use RestClient)
        //           |
        //           |
        //           V
        // (finish stage 1 pass to another to finish the rest: update UI)
        //           |
        //           |
        //           V
        //        observer (receive stuff from previous observable which create by map() and update UI
        //

        //NOTE
        //The | represents emissions happening on the UI Thread
        // and the ||| represents emissions happening on the IO Scheduler.


        //Attach text change listener
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: " + charSequence.toString() + ". Thread: " + Thread.currentThread().getName());
                //Called every time user typed or deleted
                // new value is going to come into our PublishSubject every, single time the user adds
                // or removes a character from their search
                //If we call the search functions multiple times in a short amount of time
                //It will decrease app's performance

                //So we will wait a bit, then send request to server
                //But how??? We cannot use Thread.sleep()
                //This is what debounce() allows us to do
                searchSubject.onNext(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    private void handleSearchResults(List<String> cities) {
        if (cities.isEmpty()) {
            tvNoResult.setVisibility(View.VISIBLE);
            rvSearchResult.setVisibility(View.GONE);
        } else {
            tvNoResult.setVisibility(View.GONE);
            rvSearchResult.setVisibility(View.VISIBLE);
            cityAdapter.setStrings(cities);
        }
    }
}
