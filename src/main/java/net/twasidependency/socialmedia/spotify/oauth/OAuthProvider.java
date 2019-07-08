package net.twasidependency.socialmedia.spotify.oauth;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import net.twasi.core.api.oauth.IOauthIntegrationHandler;
import net.twasi.core.database.models.User;
import net.twasi.core.services.providers.DataService;
import net.twasidependency.socialmedia.spotify.SpotifyDependency;
import net.twasidependency.socialmedia.spotify.database.SpotifyAuthenticationRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static net.twasidependency.socialmedia.spotify.SpotifyDependency.config;

public class OAuthProvider implements IOauthIntegrationHandler {

    private final SpotifyApi client;
    private final SpotifyAuthenticationRepository repo;

    public OAuthProvider() {
        client = SpotifyDependency.service.getUnauthorizedClient();
        repo = DataService.get().get(SpotifyAuthenticationRepository.class);
    }

    @Override
    public String getOauthServiceName() {
        return "spotify";
    }

    @Override
    public String getOauthUri(String state) {
        return client.authorizationCodeUri()
                .scope(String.join(" ", Arrays.asList(config.API.Scopes)))
                .state(state)
                .build()
                .execute()
                .toString();
    }

    @Override
    public String getStateParameterName() {
        return "state";
    }

    @Override
    public void handleResponse(Map<String, String[]> map, User user) {
        try {
            AuthorizationCodeCredentials code = client.authorizationCode(map.get("code")[0]).build().execute();
            repo.authenticate(user, code.getAccessToken(), code.getRefreshToken());
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }
    }
}
