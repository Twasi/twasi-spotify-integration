package net.twasidependency.socialmedia.spotify.api;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ProductType;
import net.twasi.core.database.models.User;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasidependency.socialmedia.spotify.SpotifyDependency;
import net.twasidependency.socialmedia.spotify.database.SpotifyAuthenticationRepository;

import static net.twasidependency.socialmedia.spotify.SpotifyDependency.service;

public class SpotifyIntegrationDTO {

    private final User user;
    private final SpotifyAuthenticationRepository repo;

    public SpotifyIntegrationDTO(User user) {
        this.user = user;
        this.repo = DataService.get().get(SpotifyAuthenticationRepository.class);
    }

    public SpotifyAccountDTO getAccount() {
        try {
            SpotifyApi client = service.getAuthorizedClient(user);
            com.wrapper.spotify.model_objects.specification.User user = service.execute(client.getCurrentUsersProfile().build(), client);
            return new SpotifyAccountDTO(user.getDisplayName(), user.getProduct() == ProductType.PREMIUM, repo, this.user);
        } catch (Exception e) {
            return null;
        }
    }

    public String getAuthenticationUri() {
        return ConfigService.get().getCatalog().webinterface.self + "/oauth/spotify";
    }
}
