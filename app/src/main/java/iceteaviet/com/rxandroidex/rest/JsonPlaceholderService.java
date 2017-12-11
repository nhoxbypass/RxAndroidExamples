package iceteaviet.com.rxandroidex.rest;

import java.util.List;

import iceteaviet.com.rxandroidex.model.Post;
import iceteaviet.com.rxandroidex.model.User;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Genius Doan on 12/4/2017.
 */

//Root will be https://jsonplaceholder.typicode.com/
public interface JsonPlaceholderService {
    @GET("posts")
    Call<List<Post>> getListPost();

    @GET("posts")
    Observable<List<Post>> getListPostObservable();

    @GET("users/1")
    Observable<User> getUserObservable();
}
