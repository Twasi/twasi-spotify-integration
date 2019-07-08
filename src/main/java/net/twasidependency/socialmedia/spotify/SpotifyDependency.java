package net.twasidependency.socialmedia.spotify;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.twasi.core.api.oauth.OAuthIntegrationController;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.services.ServiceRegistry;
import net.twasidependency.socialmedia.spotify.api.SpotifyIntegrationResolver;
import net.twasidependency.socialmedia.spotify.oauth.OAuthProvider;

public class SpotifyDependency extends TwasiDependency<SpotifyAPIConfiguration> {

    public static SpotifyService service;
    public static SpotifyAPIConfiguration config;

    @Override
    public void onActivate() {
        config = getConfiguration();
        service = new SpotifyService();
        ServiceRegistry.register(service);
        ServiceRegistry.get(OAuthIntegrationController.class).registerOauthIntegrationHandler(new OAuthProvider());
    }

    @Override
    public GraphQLQueryResolver getGraphQLResolver() {
        return new SpotifyIntegrationResolver();
    }
}
