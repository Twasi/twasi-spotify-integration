package net.twasidependency.socialmedia.spotify.api;

import net.twasi.core.database.models.User;
import net.twasidependency.socialmedia.spotify.database.SpotifyAuthenticationRepository;

public class SpotifyAccountDTO {

    private String userName;
    private boolean premium;
    private SpotifyAuthenticationRepository repo;
    private User user;

    public SpotifyAccountDTO(String userName, boolean premium, SpotifyAuthenticationRepository repo, User user) {
        this.userName = userName;
        this.premium = premium;
        this.repo = repo;
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isPremium() {
        return premium;
    }

    public boolean logout() {
        repo.unAuthententicate(user);
        return true;
    }
}
