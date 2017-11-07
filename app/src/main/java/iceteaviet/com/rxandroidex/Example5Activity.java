package iceteaviet.com.rxandroidex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
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
    private Single<Integer> single;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example5);
        ButterKnife.bind(this);

        single = Single.just(22);
        final Single<String> newSingle = single.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                //maps can take in one value and output another
                //This method will get called after onSubscribe
                //Here we convert integer from Single<Integer>.just() to a String
                //So it become a Single<String> object
                Log.d(TAG, "apply: " + integer + ". Thread: " + Thread.currentThread().getName());
                return String.valueOf(integer);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newSingle.subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.getClass().toString() + ". Thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onSuccess(String o) {
                        Log.d(TAG, "onSuccess: " + o + ". Thread: " + Thread.currentThread().getName());
                        tvDisplay.setText(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage() + ". Thread: " + Thread.currentThread().getName());
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
