package iceteaviet.com.rxandroidex.rest;

import java.util.List;

import iceteaviet.com.rxandroidex.model.SamplePojo;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Genius Doan on 12/4/2017.
 */

//Root will be https://jsonplaceholder.typicode.com/
public interface JsonPlaceholderService {
    @GET("posts")
    Call<List<SamplePojo>> getListJson();

    @GET("posts")
    Observable<List<SamplePojo>> getListJsonObservable();
}
