package net.twasidependency.socialmedia.spotify.api;

public class SpotifyAccountDTO {

    private String userName;
    private boolean premium;

    public SpotifyAccountDTO(String userName, boolean premium) {
        this.userName = userName;
        this.premium = premium;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isPremium() {
        return premium;
    }
}
