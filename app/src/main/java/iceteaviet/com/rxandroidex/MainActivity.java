package iceteaviet.com.rxandroidex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_example_list) protected RecyclerView rvExampleList;

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

        return list;
    }
}
