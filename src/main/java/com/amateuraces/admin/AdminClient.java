package com.amateuraces.admin;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.amateuraces.player.Player;


@Component
public class AdminClient {

    private final RestTemplate restTemplate;

    public AdminClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * Get an Admin with the given ID
     *
     * @param URI the base URI for the Admin resource
     * @param id  the ID of the Admin
     * @return the Admin object
     */
    public Admin getAdmin(final String URI, final Long id) {
        return restTemplate.getForObject(URI + "/" + id, Admin.class);
    }

    /**
     * Add a new Admin
     *
     * @param URI      the base URI for the Admin resource
     * @param newAdmin the new Admin to add
     * @return the added Admin object
     */
    public Admin addAdmin(final String URI, final Admin newAdmin) {
        return restTemplate.postForObject(URI, newAdmin, Admin.class);
    }

    /**
     * Update the ELO of a Player (example method, adjust as per your API)
     *
     * @param URI      the base URI for the Player resource
     * @param playerId the ID of the Player whose ELO will be updated
     * @param newElo   the new ELO score to set for the Player
     * @return the updated Player object
     */
    public Player updatePlayerElo(final String URI, final Long playerId, final int newElo) {
        String updateUri = URI + "/" + playerId + "/elo"; // Assuming there's an endpoint for updating ELO
        return restTemplate.patchForObject(updateUri, newElo, Player.class);
    }
}
