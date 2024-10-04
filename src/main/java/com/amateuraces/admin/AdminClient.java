package com.amateuraces.admin;
import com.amateuraces.player.Player;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AdminClient {

    private RestTemplate template;

    AdminClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }

    /**
     * Get an Admin with the given ID
     * 
     * @param URI the base URI for the Admin resource
     * @param id  the ID of the Admin
     * @return the Admin object
     */
    public Admin getAdmin(final String URI, final Long id) {
        final Admin admin = template.getForObject(URI + "/" + id, Admin.class);
        return admin;
    }

    /**
     * Add a new Admin
     * 
     * @param URI      the base URI for the Admin resource
     * @param newAdmin the new Admin to add
     * @return the added Admin object
     */
    public Admin addAdmin(final String URI, final Admin newAdmin) {
        final Admin returned = template.postForObject(URI, newAdmin, Admin.class);
        return returned;
    }

    /**
     * Update the ELO of a Player
     * 
     * @param URI        the base URI for the Player resource
     * @param playerId   the ID of the Player whose ELO will be updated
     * @param newElo     the new ELO score to set for the Player
     * @return the updated Player object
     */
    public Player updatePlayerElo(final String URI, final Long playerId, final int newElo) {
        String updateUri = URI + "/" + playerId + "/elo"; // Assuming there's an endpoint for updating ELO
        Player updatedPlayer = template.patchForObject(updateUri, newElo, Player.class); // Patch is typically used for partial updates
        return updatedPlayer;
    }
}
