package net.twasidependency.socialmedia.spotify.database;

import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.User;

public class SpotifyAuthenticationRepository extends Repository<SpotifyAuthenticationEntity> {

    public SpotifyAuthenticationEntity getAuthenticationByUser(User user) {
        try {
            return store.createQuery(SpotifyAuthenticationEntity.class).field("user").equal(user).get();
        } catch (Exception e) {
            return null;
        }
    }

    public SpotifyAuthenticationEntity authenticate(User user, String token, String refreshToken) {
        SpotifyAuthenticationEntity entity = getAuthenticationByUser(user);
        if (entity == null) {
            add(entity = new SpotifyAuthenticationEntity(user, token, refreshToken));
        } else {
            entity.setToken(token);
            commit(entity);
        }
        return entity;
    }

    public void unAuthententicate(User user) {
        try {
            store.delete(getAuthenticationByUser(user));
        } catch (Exception e) {
        }
    }
}
