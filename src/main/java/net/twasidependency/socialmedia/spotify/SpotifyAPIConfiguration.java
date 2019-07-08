package net.twasidependency.socialmedia.spotify;

public class SpotifyAPIConfiguration {

    public APICredentials API = new APICredentials();

    public class APICredentials {
        public String ClientID = "Client_ID";
        public String ClientSecret = "Client_Secret";
        public String RedirectURI = "Redirect_URI";
        public String[] Scopes = {
                "user-read-playback-state",
                "user-read-recently-played",
                "user-read-currently-playing",
                "user-read-private",
                "streaming",
                "user-modify-playback-state",
                "playlist-modify-private",
                "playlist-modify-public"
        };
    }
}