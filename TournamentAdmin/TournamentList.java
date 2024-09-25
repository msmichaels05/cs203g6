public class TournamentList {
    private List<Tournament>  tournamentlist = null;

    @Autowired
    public TournamentList() {this.tournamentlist = new ArrayList<Tournament>();}


    public void get_tournaments() {
        return tournamentlist;
    }

    public void add(Tournament tourny) {
        tournamentlist.add(tourny);
    }
}
