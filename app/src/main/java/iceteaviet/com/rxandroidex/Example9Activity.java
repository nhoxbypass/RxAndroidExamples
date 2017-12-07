package iceteaviet.com.rxandroidex;

import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

// Blog: http://blog.danlew.net/2017/08/02/why-not-rxlifecycle/
// how easy it was to leak memory when using RxJava
//  Seemingly any Subscription you setup would leak unless you explicitly cleared it
// As such, we were constantly juggling Subscriptions and un-subscribing when we were done using them
// Repo: https://github.com/trello/RxLifecycle
// Similarity: https://github.com/uber/AutoDispose
public class Example9Activity extends RxAppCompatActivity {
    private static final String TAG = Example9Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example9);

        Observable.interval(1, TimeUnit.SECONDS)
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.getClass().toString() + ". Thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(@NonNull Long integer) {
                        Log.d(TAG, "onNext " + integer + ". Thread: " + Thread.currentThread().getName());
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

        //If we use
        // .compose(this.<Long>bindToLifecycle())
        // While the observable is executing, if we click BACK to stop activity
        // -> onNext() and onComplete() get called -> observable un-subscribe
        // If not
        // Nothing called -> observable will execute until the app forced stop!!!
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}
