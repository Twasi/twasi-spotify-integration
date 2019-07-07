package net.twasidependency.socialmedia.spotify.database;

import net.twasi.core.database.models.BaseEntity;
import net.twasi.core.database.models.User;
import org.mongodb.morphia.annotations.Reference;

public class SpotifyAuthenticationEntity extends BaseEntity {

    @Reference
    private User user;

    private String token;

    public SpotifyAuthenticationEntity(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public SpotifyAuthenticationEntity() {
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
