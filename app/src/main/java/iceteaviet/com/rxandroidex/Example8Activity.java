package iceteaviet.com.rxandroidex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iceteaviet.com.rxandroidex.adapter.SimpleStringAdapter;
import iceteaviet.com.rxandroidex.model.CombinedPojo;
import iceteaviet.com.rxandroidex.model.Post;
import iceteaviet.com.rxandroidex.model.User;
import iceteaviet.com.rxandroidex.rest.JsonPlaceholderService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
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
    @BindView(R.id.btn_zip)
    protected Button btnZip;
    @BindView(R.id.rv_example_list)

    protected RecyclerView recyclerView;
    private SimpleStringAdapter adapter;
    private Call<List<Post>> call;
    private Observable<List<Post>> postObservable;
    private Observable<User> userObservable;

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
        call = pojoCall.getListPost();

        //Observable
        postObservable = pojoCall.getListPostObservable();
        userObservable = pojoCall.getUserObservable();

        //Now, let's make things a bit more interesting.
        // Let's say you not only want to retrieve the userPosts, but you are developing an Facebook-clone,
        // and you want to retrieve 2 JSONs:  getListPost() 2. getUser()
        // You want to load these two JSONs in parallel, and when both are loaded, the page should be displayed
        // The callback variant will become a bit more difficult:
        //  you have to create 2 callbacks, store the data in the activity, and if all the data is loaded, display the page
        // There is 2 approach for this:
        //    - First: call getUser(), then when user loaded inside callback, call getListPost() -> Cause callback hell AND not optimal, not asynchronous
        //    - Second: call getUser() and getListPost() same time, inside callback of both of them
        //              check if the other data is finished loading -> process show data
        //              -> Cause boilerplate code that check finish loading and show data,
        //              -> hard to extend, needs to be adjusted in multiple places when we need more than 2 type of data in the future
        // SO we use RxJava to make the code is more robust -> This is also the main reason to use RxJava over normal callback !!!
        // Using zip() operator
        final Observable<CombinedPojo> combinedPojoObservable = Observable.zip(postObservable, userObservable, new BiFunction<List<Post>, User, CombinedPojo>() {
            @Override
            public CombinedPojo apply(List<Post> posts, User users) throws Exception {
                Log.d(TAG, "BiFunction apply()");
                CombinedPojo res = new CombinedPojo();
                res.setPosts(posts);
                res.setUser(users);

                return res;
            }
        });

        adapter = new SimpleStringAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Normal callback
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                call.enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        List<Post> pojoList = response.body();

                        List<String> title = new ArrayList<>();
                        for (int i = 0; i < pojoList.size(); i++) {
                            title.add(pojoList.get(i).getTitle());
                        }

                        adapter.setStrings(title);
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });


        //Observable normal
        btnObservableLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                postObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Post>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe");
                            }

                            @Override
                            public void onNext(List<Post> samplePojos) {
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

        //Combine 2 observable using zip() operator
        btnZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                combinedPojoObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CombinedPojo>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "onSubscribe");
                            }

                            @Override
                            public void onNext(CombinedPojo combinedPojo) {
                                Toast.makeText(Example8Activity.this,
                                        "User: " + combinedPojo.getUser().getName() + ". Posts size: " + combinedPojo.getPosts().size(),
                                        Toast.LENGTH_SHORT)
                                        .show();
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
