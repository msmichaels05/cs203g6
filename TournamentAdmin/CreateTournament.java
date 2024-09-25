// Create a tournament

// Set up name of tournament 

// Terms and conditions of signing up for tournament (Gender, min ELO)

// Date range for potential participants to sign up for the tournament

// Publish it into upcoming tournament list once its start date is a month away from current date

// SHOULD BE AUTHORISED SUCH THAT ONLY ADMIN HAS ACCESS TO THIS

import java.util.Scanner;

public class createTournament {
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

        //Tournament tourny = new Tournament(name, minELO, gender, registerStartDate, registerEndDate, startDate, endDate)
        //upcomingtournaments.add(tourny)
    }
}
