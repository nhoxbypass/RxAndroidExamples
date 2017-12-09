package iceteaviet.com.rxandroidex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

// RxBinding is a set of libraries that allow you to react to user interface events via the RxJava paradigm
// RxBinding is for replace combination of both listeners, Handlers and AsyncTask
// -> With RxJava and RxBinding, compose asynchronous events with the trigger of multiple UI events
//    can be greatly simplified and you can describe the interaction patterns between multiple UI components.
public class Example10Activity extends AppCompatActivity {
    public static final String TAG = Example10Activity.class.getSimpleName();

    @BindView(R.id.button)
    protected Button button;
    @BindView(R.id.textview)
    protected TextView textView;
    @BindView(R.id.edittext)
    protected EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example10);
        ButterKnife.bind(this);

        RxView.clicks(button)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Toast.makeText(Example10Activity.this, "Button clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //Everything seem similar with clicks event
        // But with TextWatcher Android requires you to write a significant amount of boilerplate code
        //This lack of consistency can potentially add a lot of complexity to your code
        // AND
        // if you have UI components that depend on the output of other UI components, then get ready for things to get even more complicated
        // requires nested callbacks, which are notoriously difficult to implement and maintain. (Some people refer to nested callbacks as “callback hell.”)

        //So we convert it to Observable
        //We can set multiple RxTextView.textChanges() for a View
        RxTextView.textChanges(editText)
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        return charSequence.length() % 2 == 0; //Only emit when string have even length
                    }
                })
                .map(new Function<CharSequence, CharSequence>() {
                    @Override
                    public CharSequence apply(CharSequence charSequence) throws Exception {
                        return new StringBuilder(charSequence).reverse().toString();
                    }
                })
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        textView.setText(charSequence);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "textChanges: onComplete");
                    }
                });

        //Set another textChanges() operator for same edittext
        /*
        RxTextView.textChanges(editText)
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        Log.e(TAG, "2222");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        */

        RxTextView.textChangeEvents(editText)
                .debounce(1000, TimeUnit.MILLISECONDS) //Only emit after 1 sec
                .subscribe(new Observer<TextViewTextChangeEvent>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TextViewTextChangeEvent event) {
                        //Get more information about text change event
                        Log.e(TAG, "Before: " + event.before() + ", start: " + event.start() + ", count: " + event.count());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "textChangeEvents: onComplete");
                    }
                });

        // ***
        //There are a few things to be aware of when working with RxBinding.
        //  - weak references should not be used
        //  - various parts of the Android framework ,the UI events emit multiple values instead of a single argument like a click listener
        //    RxJava observables only emit a single object, not an array of values.
        //    Therefore, a wrapper object is needed to combine these values into a single object in these instances
        //    Ex: the scroll change listener returns multiple values such as scrollX, scrollY, oldScrollX, oldScrollY -> in RxBinding these values are combined into a wrapper object called ViewScrollChangeEvent

    }
}
