package iceteaviet.com.rxandroidex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class Example4Activity extends AppCompatActivity {
    private static final String TAG = Example4Activity.class.getSimpleName();

    @BindView(R.id.btn_subscribe) protected Button subscribeButton;
    @BindView(R.id.btn_counter) protected Button incrementButton;
    @BindView(R.id.tv_display) protected TextView displayView;

    //Subject acts both as an Observer and as an Observable
    // Because it is an observer, it can subscribe to one or more Observables,
    // and because it is an Observable, it can pass through the items it observes by reemitting them,
    // and it can also emit new items.
    //
    //There are several types of Subjects, but we’re going to use the simplest one: a PublishSubject.
    // With a PublishSubject, as soon as you put something in one end of the pipe
    // it **immediately** comes out the other.
    private PublishSubject<Integer> counterEmitter;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example4);
        ButterKnife.bind(this);

        counterEmitter = PublishSubject.create();

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we communicate that to the Observer over on the other end of the pipe
                counterEmitter.subscribe(new Observer<Integer> () {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.getClass().toString() + ". Thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Integer o) {
                        Log.d(TAG, "onNext" + o + ". Thread: " + Thread.currentThread().getName());
                        displayView.setText(String.valueOf(o));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage() + ". Thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete" + ". Thread: " + Thread.currentThread().getName());
                    }
                });
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                //on one end of the pipe we observe the increment button being clicked
                //Loop thru all subscribers/observers and call onNext() and pass value
                counterEmitter.onNext(count);
            }
        });

        //Result after click subscribe button
        //D/Example4Activity: onSubscribe: class io.reactivex.subjects.PublishSubject$PublishDisposable. Thread: main

        //Result after click increment button 3 times
        //D/Example4Activity: onNext1. Thread: main
        //D/Example4Activity: onNext2. Thread: main
        //D/Example4Activity: onNext3. Thread: main
    }
}
