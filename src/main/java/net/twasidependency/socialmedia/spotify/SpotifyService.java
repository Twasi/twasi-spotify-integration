package net.twasidependency.socialmedia.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.TooManyRequestsException;
import com.wrapper.spotify.exceptions.detailed.UnauthorizedException;
import com.wrapper.spotify.model_objects.AbstractModelObject;
import com.wrapper.spotify.requests.AbstractRequest;
import com.wrapper.spotify.requests.IRequest;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.IService;
import net.twasi.core.services.providers.DataService;
import net.twasidependency.socialmedia.spotify.database.SpotifyAuthenticationEntity;
import net.twasidependency.socialmedia.spotify.database.SpotifyAuthenticationRepository;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import static net.twasidependency.socialmedia.spotify.SpotifyDependency.config;

public class SpotifyService implements IService {

    private final SpotifyAuthenticationRepository repo;

    private Date rateLimitReset = Calendar.getInstance().getTime();

    public SpotifyService() {
        repo = DataService.get().get(SpotifyAuthenticationRepository.class);
    }

    public SpotifyApi getAuthorizedClient(User user) {
        SpotifyAuthenticationEntity entity = repo.getAuthenticationByUser(user);
        if (entity == null) return null;
        try {
            return getClientBuilder()
                    .setAccessToken(entity.getToken())
                    .setRefreshToken(entity.getRefreshToken())
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

    public <T extends AbstractModelObject> T execute(AbstractRequest request, SpotifyApi client, Class<T> responseType) {
        return execute(request, client, responseType, true);
    }

    private <T extends AbstractModelObject> T execute(AbstractRequest request, SpotifyApi client, Class<T> responseType, boolean firstTry) {
        if (this.rateLimitReset.getTime() > Calendar.getInstance().getTimeInMillis()) return null;
        try {
            return request.execute();
        } catch (TooManyRequestsException e) {
            this.rateLimitReset = Calendar.getInstance().getTime();
            this.rateLimitReset.setTime(this.rateLimitReset.getTime() + e.getRetryAfter() * 1000);
        } catch (UnauthorizedException e) {
            if (firstTry) {
                try {
                    client.authorizationCodeRefresh().build().execute();
                    execute(request, client, responseType, false);
                } catch (IOException | SpotifyWebApiException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SpotifyApi.Builder getClientBuilder() {
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
