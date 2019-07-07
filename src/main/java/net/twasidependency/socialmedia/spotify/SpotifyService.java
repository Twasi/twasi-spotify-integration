package net.twasidependency.socialmedia.spotify;

import com.wrapper.spotify.SpotifyApi;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.IService;
import net.twasi.core.services.providers.DataService;
import net.twasidependency.socialmedia.spotify.database.SpotifyAuthenticationEntity;
import net.twasidependency.socialmedia.spotify.database.SpotifyAuthenticationRepository;

import java.net.URI;

public class SpotifyService implements IService {

    private final SpotifyAuthenticationRepository repo;
    private final SpotifyAPIConfiguration config;

    public SpotifyService(SpotifyAPIConfiguration config) {
        this.config = config;
        repo = DataService.get().get(SpotifyAuthenticationRepository.class);
    }

    public SpotifyApi getAuthorizedClient(User user) {
        SpotifyAuthenticationEntity entity = repo.getAuthenticationByUser(user);
        if (entity == null) return null;
        try {
            return getClientBuilder()
                    .setAccessToken(entity.getToken())
                    .build();
        } catch (Exception e) {
            TwasiLogger.log.error("Could not build Spotify API client for user " + user.getTwitchAccount().getDisplayName() + ".");
            TwasiLogger.log.debug("API Client could not be built.", e);
            return null;
        }
    }

    public SpotifyApi getUnauthorizedClient() {
        try {
            return getClientBuilder().build();
        } catch (Exception e) {
            return null;
        }
    }

    public SpotifyApi.Builder getClientBuilder() {
        try {
            return SpotifyApi.builder()
                    .setClientId(config.API.ClientID)
                    .setClientSecret(config.API.ClientSecret)
                    .setRedirectUri(new URI(config.API.RedirectURI));
        } catch (Exception e) {
            return null;
        }
    }
}
