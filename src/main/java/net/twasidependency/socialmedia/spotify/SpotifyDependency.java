package net.twasidependency.socialmedia.spotify;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.services.ServiceRegistry;
import net.twasidependency.socialmedia.spotify.api.SpotifyIntegrationResolver;

public class SpotifyDependency extends TwasiDependency<SpotifyAPIConfiguration> {

    public static SpotifyService service;

    @Override
    public void onActivate() {
        service = new SpotifyService(getConfiguration());
        ServiceRegistry.register(service);
    }

    @Override
    public GraphQLQueryResolver getGraphQLResolver() {
        return new SpotifyIntegrationResolver();
    }
}
