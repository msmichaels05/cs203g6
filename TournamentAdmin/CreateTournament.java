// Create a tournament

// Set up name of tournament 

// Terms and conditions of signing up for tournament (Gender, min ELO)

// Date range for potential participants to sign up for the tournament

// Publish it into upcoming tournament list once its start date is a month away from current date

// SHOULD BE AUTHORISED SUCH THAT ONLY ADMIN HAS ACCESS TO THIS

import java.util.Scanner;

public class CreateTournament {
    public static void main(String[] args) {
        System.out.println("Creating a tournament");
        Scanner sc = new Scanner(System.in);

        System.out.println("Name: ");
        String name = sc.nextLine();

        System.out.println("Gender: ");
        char gender = sc.nextLine().charAt(0);

        System.out.println("Minimum ELO requirement: ");
        int minELO = sc.nextInt();

        sc.close();

        // Allow admin to input registered start & end date, and also tournament start & end date

        LocalDate registerStartDate = LocalDate.of(2025, 1, 1);
        LocalDate registerEndDate = LocalDate.of(2025, 1, 15);
        LocalDate startDate = localDate.of(2025,2,1);
        LocalDate endDate = localDate.of(2025, 2, 8);

        Tournament tourny = new Tournament(name, minELO, gender, registerStartDate, registerEndDate, startDate, endDate);
        TournamentList upcomingtournaments = new TournamentList();
        upcomingtournaments.add(tourny);
    }
}
