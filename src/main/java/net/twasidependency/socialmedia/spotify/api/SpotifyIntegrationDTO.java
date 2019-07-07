package net.twasidependency.socialmedia.spotify.api;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ProductType;
import net.twasi.core.database.models.User;
import net.twasi.core.services.providers.DataService;
import net.twasidependency.socialmedia.spotify.SpotifyDependency;
import net.twasidependency.socialmedia.spotify.database.SpotifyAuthenticationRepository;

public class SpotifyIntegrationDTO {

    private final User user;
    private final SpotifyAuthenticationRepository repo;

    public SpotifyIntegrationDTO(User user) {
        this.user = user;
        this.repo = DataService.get().get(SpotifyAuthenticationRepository.class);
    }

    public SpotifyAccountDTO getAccount() {
        try {
            SpotifyApi client = SpotifyDependency.service.getAuthorizedClient(user);
            com.wrapper.spotify.model_objects.specification.User user = client.getCurrentUsersProfile().build().execute();
            return new SpotifyAccountDTO(user.getDisplayName(), user.getProduct() == ProductType.PREMIUM);
        } catch (Exception e) {
            return null;
        }
    }

    public SpotifyAccountDTO authenticate(String token) {
        try {
            SpotifyApi client = SpotifyDependency.service.getClientBuilder().setAccessToken(token).build();
            com.wrapper.spotify.model_objects.specification.User user = client.getCurrentUsersProfile().build().execute();
            SpotifyAccountDTO acc = new SpotifyAccountDTO(user.getDisplayName(), user.getProduct() == ProductType.PREMIUM);
            repo.authenticate(this.user, token);
            return acc;
        } catch (Exception e) {
            return null;
        }
    }
}
