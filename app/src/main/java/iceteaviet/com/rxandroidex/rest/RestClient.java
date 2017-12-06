package iceteaviet.com.rxandroidex.rest;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import iceteaviet.com.rxandroidex.R;

/**
 * Created by Genius Doan on 10/29/2017.
 * <p>
 * This is a mock REST Client. It simulates making blocking calls to an REST endpoint.
 */

public class RestClient {
    private final int DEFAULT_DELAY_TIME = 1000; //ms
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

    public List<String> getFavouriteMoviesWithExeption() {
        try {
            //Simulate the delay of network call
            Thread.sleep(DEFAULT_DELAY_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to fetch data");
    }

    private List<String> createFavouriteMovies() {
        return Arrays.asList(mContext.getResources().getStringArray(R.array.fav_movies));
    }

    public List<String> searchForCity(String searchString) {
        try {
            // "Simulate" the delay of network.
            Thread.sleep(DEFAULT_DELAY_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getMatchingCities(searchString);
    }

    private List<String> getMatchingCities(String searchString) {
        if (searchString.isEmpty()) {
            return new ArrayList<>();
        }

        String[] cities = mContext.getResources().getStringArray(R.array.city_list);
        List<String> toReturn = new ArrayList<>();
        for (String city : cities) {
            if (city.toLowerCase().startsWith(searchString.toLowerCase())) {
                toReturn.add(city);
            }
        }
        return toReturn;
    }
}
