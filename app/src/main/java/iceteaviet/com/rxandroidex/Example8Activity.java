package iceteaviet.com.rxandroidex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iceteaviet.com.rxandroidex.adapter.SimpleStringAdapter;
import iceteaviet.com.rxandroidex.model.SamplePojo;
import iceteaviet.com.rxandroidex.rest.JsonPlaceholderService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Example8Activity extends AppCompatActivity {
    private static final String TAG = Example8Activity.class.getSimpleName();
    @BindView(R.id.btn_load)
    protected Button btnLoad;
    @BindView(R.id.btn_load_observable)
    protected Button btnObservableLoad;
    @BindView(R.id.rv_example_list)
    protected RecyclerView recyclerView;
    private SimpleStringAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example8);
        ButterKnife.bind(this);

        //Basic need for retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //Support RxJava for Retrofit2 (MUST HAVE)
                .build();

        //RxJava2CappAdapterFactory.create(): creates synchronous observables that by default do not operate on any scheduler
        //RxJava2CappAdapterFactory.createAsync(): creates asynchronous observables that IGNORE all Observable#subscribeOn that set later
        // -> If Apply subscribeOn(AndroidSchedulers.mainThread()) will take no effect,
        //and the retrofit observable still continue executing WITHOUT NetworkOnMainThread exception!!!


        final JsonPlaceholderService pojoCall = retrofit.create(JsonPlaceholderService.class);

        //Normal callback
        final Call<List<SamplePojo>> call = pojoCall.getListJson();

        //Observable
        final Observable<List<SamplePojo>> observable = pojoCall.getListJsonObservable();

        adapter = new SimpleStringAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Normal callback
                adapter.clear();
                call.enqueue(new Callback<List<SamplePojo>>() {
                    @Override
                    public void onResponse(Call<List<SamplePojo>> call, Response<List<SamplePojo>> response) {
                        List<SamplePojo> pojoList = response.body();

                        List<String> title = new ArrayList<>();
                        for (int i = 0; i < pojoList.size(); i++) {
                            title.add(pojoList.get(i).getTitle());
                        }

                        adapter.setStrings(title);
                    }

                    @Override
                    public void onFailure(Call<List<SamplePojo>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });


        btnObservableLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                //Observable
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<SamplePojo>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<SamplePojo> samplePojos) {
                                List<String> title = new ArrayList<>();
                                for (int i = 0; i < samplePojos.size(); i++) {
                                    title.add(samplePojos.get(i).getTitle());
                                }

                                adapter.setStrings(title);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete");
                            }
                        });
            }
        });
    }
}
