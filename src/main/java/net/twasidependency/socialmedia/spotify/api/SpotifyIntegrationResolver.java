package net.twasidependency.socialmedia.spotify.api;

import net.twasi.core.database.models.User;
import net.twasi.core.graphql.TwasiCustomResolver;

public class SpotifyIntegrationResolver extends TwasiCustomResolver {

    public SpotifyIntegrationDTO getSpotifyintegration(String token) {
        User user = getUser(token);
        if (user != null) return new SpotifyIntegrationDTO(user);
        return null;
    }

}
