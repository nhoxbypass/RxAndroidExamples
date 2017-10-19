package iceteaviet.com.rxandroidex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class Example1Activity extends AppCompatActivity {
    private static final String TAG = Example1Activity.class.getSimpleName();
    private Observable<List<String>> listObservable = Observable.just(getKingsmanList());
    private RecyclerView mColorListView;
    private KingsmanAdapter mKingsmanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example1);
        mColorListView = (RecyclerView) findViewById(R.id.color_list);
        mColorListView.setLayoutManager(new LinearLayoutManager(this));
        mKingsmanAdapter = new KingsmanAdapter(this);
        mColorListView.setAdapter(mKingsmanAdapter);

        listObservable.subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(@NonNull List<String> strings) {
                Log.d(TAG, "onNext");
                mKingsmanAdapter.setStrings(strings);
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
