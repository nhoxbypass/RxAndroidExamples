package iceteaviet.com.rxandroidex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iceteaviet.com.rxandroidex.adapter.ExampleListAdapter;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_example_list)
    protected RecyclerView rvExampleList;

    private ExampleListAdapter mAdapter;
    private List<String> exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        exampleList = createExampleList();
        mAdapter = new ExampleListAdapter(exampleList);
        rvExampleList.setAdapter(mAdapter);
        rvExampleList.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.setOnItemClickListener(new ExampleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, Example1Activity.class));
                        break;

                    case 1:
                        startActivity(new Intent(MainActivity.this, Example2Activity.class));
                        break;

                    case 2:
                        startActivity(new Intent(MainActivity.this, Example3Activity.class));
                        break;

                    case 3:
                        startActivity(new Intent(MainActivity.this, Example4Activity.class));
                        break;

                    case 4:
                        startActivity(new Intent(MainActivity.this, Example5Activity.class));
                        break;

                    case 5:
                        startActivity(new Intent(MainActivity.this, Summary1Activity.class));
                        break;

                    case 6:
                        startActivity(new Intent(MainActivity.this, Example6Activity.class));
                        break;

                    case 7:
                        startActivity(new Intent(MainActivity.this, Example7Activity.class));
                        break;

                    case 8:
                        startActivity(new Intent(MainActivity.this, Example8Activity.class));
                        break;

                    default:
                        break;
                }
            }
        });
    }

    public List<String> createExampleList() {
        List<String> list = new ArrayList<>();

        list.add("1 - Observable.just()");
        list.add("2 - Async network Observable.callable()");
        list.add("3 - Async network Single.callable()");
        list.add("4 - Subject");
        list.add("5 - map() function");
        list.add("Summary 1");
        list.add("6 - Schedulers");
        list.add("7 - Hot/cold observable");
        list.add("8 - Retrofit with Observable");

        return list;
    }
}
