package iceteaviet.com.rxandroidex.model;

import java.util.List;

/**
 * Created by Genius Doan on 12/11/2017.
 */

public class CombinedPojo {
    private List<Post> posts;
    private User user;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
