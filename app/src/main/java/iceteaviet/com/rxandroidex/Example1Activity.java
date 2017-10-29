package iceteaviet.com.rxandroidex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private Observable<List<String>> listObservable;
    private RecyclerView kingsmanList;
    private SimpleStringAdapter kingsmanAdapter;
    private Button subscribeButton;
    private TextView tvPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example1);
        kingsmanList = (RecyclerView) findViewById(R.id.rv_kingsman_list);
        subscribeButton = (Button) findViewById(R.id.btn_subscribe);
        tvPlaceholder = findViewById(R.id.tv_placeholder);

        kingsmanList.setLayoutManager(new LinearLayoutManager(this));
        kingsmanAdapter = new SimpleStringAdapter(this);
        kingsmanList.setAdapter(kingsmanAdapter);

        listObservable = Observable.just(getKingsmanList());
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPlaceholder.setVisibility(View.GONE);
                kingsmanList.setVisibility(View.VISIBLE);
                listObservable.subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.getClass().toString());
                    }

                    @Override
                    public void onNext(@NonNull List<String> strings) {
                        Log.d(TAG, "onNext");
                        kingsmanAdapter.setStrings(strings);
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
