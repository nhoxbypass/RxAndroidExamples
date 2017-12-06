package iceteaviet.com.rxandroidex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iceteaviet.com.rxandroidex.adapter.SimpleStringAdapter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Example6Activity extends AppCompatActivity {
    private static final String TAG = Example6Activity.class.getSimpleName();
    @BindView(R.id.rv_kingsman_list)
    protected RecyclerView kingsmanList;
    @BindView(R.id.btn_subscribe)
    protected Button subscribeButton;
    @BindView(R.id.tv_placeholder)
    protected TextView tvPlaceholder;

    private Observable<String> listObservable;
    private SimpleStringAdapter kingsmanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example6);
        ButterKnife.bind(this);

        kingsmanList.setLayoutManager(new LinearLayoutManager(this));
        kingsmanAdapter = new SimpleStringAdapter(this);
        kingsmanList.setAdapter(kingsmanAdapter);

        listObservable = Observable.fromIterable(getKingsmanList());

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPlaceholder.setVisibility(View.GONE);
                kingsmanList.setVisibility(View.VISIBLE);
                listObservable
                        .observeOn(Schedulers.trampoline()) //Replace with other schedulers
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                Log.d(TAG, "onSubscribe: " + d.getClass().toString() + ". Thread: " + Thread.currentThread().getName());
                            }

                            @Override
                            public void onNext(@NonNull String string) {
                                Log.d(TAG, "onNext: " + string + ". Thread: " + Thread.currentThread().getName());
                                kingsmanAdapter.addString(string);
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
            }
        });

        //RESULT
        //io():  Thread: RxCachedThreadScheduler-1 for all emission.
        //computation(): Thread: RxComputationThreadPool-1 for all emission
        // newThread(): Only display Thread: RxNewThreadScheduler-1 for the first emission then app CRASHED
        //              Error: android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        // single(): Thread: RxSingleScheduler-1 for all emission
        // trampoline(): Thread: main for all emission (like the legacy Schedulers.immediate())

        //Explain:
        //newThread(): Creates and returns a Scheduler that creates a new Thread for each unit of work.
        //          -> then you are up for THREAD CREATION overhead and context switching overhead as threads vie for a processor
        //              and it's potentially a big performance hit.
        //              threads are expensive to construct and tear down AND IT'S THE REASON WHY THREAD POOL EXISTS.
        //          -> Hardly use

        //computation(): Creates and returns a Scheduler intended for computational work (event-loops, processing callbacks and other computational work...)
        //              BUT
        //              it's backed by a bounded thread-pool with size equal to the number of available processors.
        //              SO
        //              It's best to leave computation() for CPU intensive work only otherwise you won't get good CPU utilization (limitation of Thread)

        //    io():     Creates and returns a Scheduler intended for IO-bound work which backed by an Executor thread-pool that will GROW AS NEEDED
        //               BECAUSE io() threads is unbounded so you can create a thousand of Thread (like newThread())
        //              SO
        //              If we use io() for computation work, it will cause overhead of THREAD CREATION and context switching
        //              AND DIFF between .newThread() and io() is
        //              Schedulers.io() is better by using a thread pool, whereas Schedulers.newThread() does not
        //             -> thread pools is that they maintain a number of pre-created threads that are idle and waiting for work.
        //              you don't need to go through the overhead of CREATING a thread.
        //              Once your work is done, that thread can also be re-used for future work instead of constantly creating and destroying threads.

        //trampoline(): Creates and returns a Scheduler that queues work on the CURRENT THREAD to be executed after the current work completes.


    }

    private List<String> getKingsmanList() {
        return Arrays.asList(getResources().getStringArray(R.array.kingsman));
    }
}
