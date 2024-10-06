// Check function to allow or reject interested player to register for tourny
// Gender, Min ELO
// Assumers that Player class is created somewhere

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class Tournament {

    private String name;
    private int minELO;
    private char gender;
    private LocalDate registerStartDate;
    private LocalDate registerEndDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Player> participants = null;

    public Tournament(String name, int minELO, char gender, LocalDate registerStartDate, LocalDate registerEndDate, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.minELO = minELO;
        this.gender = gender;
        this.registerStartDate = registerStartDate;
        this.registerEndDate = registerEndDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = new ArrayList<>();
    }

    @Autowired
    public String getName() {return this.name;}

    @Autowired
    public int getMinELO() {return this.minELO;}

    @Autowired
    public char getGender() {return this.gender;}

    @Autowired
    public LocalDate getRegisterStartDate() {return this.registerStartDate;}

    @Autowired
    public LocalDate getRegisteredEndDate() {return this.registerEndDate;}

    @Autowired
    public LocalDate getStartDate() {return this.startDate;}

    @Autowired
    public LocalDate getEndDate() {return this.endDate;}

    @Autowired
    public List<Player> getParticipants() {return this.participants;}

    @Autowired
    public void register(Player player) {
        if (player.getGender != gender) {
            System.out.println("Failed to register " + player.getName() + ",must be gender " + gender);
        }
        if (player.minELO() < minELO) {
            System.out.println("Failed to register " + player.getName() + ",must have a minimum of " + minELO + " ranking points");
        }
        else {
            participants.add(player);
            System.out.println("Successfully registered " + player.getName() + " for the tournament");
        }
    }
}
