package iceteaviet.com.rxandroidex;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius Doan on 10/29/2017.
 *
 * This is a mock REST Client. It simulates making blocking calls to an REST endpoint.
 */

public class RestClient {
    private final int DEFAULT_DELAY_TIME = 3000; //ms
    private Context mContext;

    public RestClient(Context context) {
        mContext = context;
    }

    public List<String> getFavouriteMovies() {
        try {
            //Simulate the delay of network call
            Thread.sleep(DEFAULT_DELAY_TIME);
            return createFavouriteMovies();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> createFavouriteMovies() {
        List<String> list = new ArrayList<>();

        list.add("Kingsman");
        list.add("Starwars");
        list.add("Harry Potter");
        list.add("Geo Storm");
        list.add("Spider man");
        list.add("Wonder woman");
        list.add("Thor");

        return list;
    }
}
