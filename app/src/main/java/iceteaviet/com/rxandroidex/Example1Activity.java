package iceteaviet.com.rxandroidex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

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

    /*
    * Observable.just() method creates an Observable such that when an Observer subscribes,
    * the onNext() of the Observer is immediately called
    * with the argument provided to Observable.just().
    * */
    private Observable<List<String>> listObservable = Observable.just(getKingsmanList());
    private RecyclerView kingsmanList;
    private KingsmanAdapter kingsmanAdapter;
    private Button subscribeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example1);
        kingsmanList = (RecyclerView) findViewById(R.id.color_list);
        subscribeButton = (Button) findViewById(R.id.btn_subscribe);

        kingsmanList.setLayoutManager(new LinearLayoutManager(this));
        kingsmanAdapter = new KingsmanAdapter(this);
        kingsmanList.setAdapter(kingsmanAdapter);

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listObservable.subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull List<String> strings) {
                        Log.d(TAG, "onNext");
                        kingsmanAdapter.setStrings(strings);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
            }
        });
    }

    private List<String> getKingsmanList() {
        List<String> colors = new ArrayList<>();
        colors.add("Lancelot");
        colors.add("Galahad");
        colors.add("Gawain");
        colors.add("Percival");
        colors.add("Gareth");
        return colors;
    }
}
