package iceteaviet.com.rxandroidex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

//Map
//http://reactivex.io/documentation/operators/map.html
//transform the items emitted by an Observable by applying a function to each item
//You can think of map as a function that takes in one value and outputs another value.
public class Example5Activity extends AppCompatActivity {
    private static final String TAG = Example5Activity.class.getSimpleName();
    @BindView(R.id.btn_map)
    protected Button btnMap;
    @BindView(R.id.tv_display)
    protected TextView tvDisplay;
    private Observable<List<Integer>> observable;
    private List<Integer> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example5);
        ButterKnife.bind(this);

        data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        observable = Observable.just(data);

        //The function: <R> Observable<R> map(Function<? super T, ? extends R> mapper)
        //So the type of NEW Observable returned by map() function is R
        // which also the type of the result returned by `R apply()` method
        // which also generic type of Function<T, R>
        // And because of `apply()` only return single value type R
        // The NEW Observable also emit only 1 emission for each emission come to map() function (1 input map for 1 output)
        // This is also the basic different from .flatMap() function
        final Observable<String> newSingle = observable
                .flatMap(new Function<List<Integer>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(List<Integer> s) throws Exception {
                        //flatMap() can transform the items which emitted by an Observable into Observables
                        //Which mean take in one item and return an Observable
                        //This method will get called after onSubscribe and before map-apply()
                        // The "flat" mean this function can flatten the emissions from those into a single Observable
                        Log.d(TAG, "flatMap: apply(). Thread: " + Thread.currentThread().getName());

                        //Here we convert observable.just() to Observable.from() to emit multiple times
                        return Observable.fromIterable(s); //map() CANNOT do sth like this!
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        //map() can take in one value and output another
                        //This method will get called after onSubscribe
                        //Here we convert Integer to a String
                        //So it become a Observable<String> object
                        Log.d(TAG, "map: apply() " + integer + ". Thread: " + Thread.currentThread().getName());
                        return String.valueOf(integer);
                    }
                });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newSingle.subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.getClass().toString() + ". Thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String s) {
                        String currText = tvDisplay.getText().toString();
                        tvDisplay.setText(currText.concat(", ").concat(s));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });


        //Result when click map button
        //
        //D/Example5Activity: onSubscribe: class io.reactivex.internal.disposables.EmptyDisposable. Thread: main
        //D/Example5Activity: apply: 22. Thread: main
        //D/Example5Activity: onSuccess: 22. Thread: main
    }
}
