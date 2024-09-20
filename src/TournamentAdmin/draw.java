//create draw

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class draw {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> participants = new ArrayList<>();

        System.out.println("Enter the number of participants:");
        int numberOfParticipants = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Getting participants' names
        for (int i = 1; i <= numberOfParticipants; i++) {
            System.out.println("Enter the name of participant " + i + ":");
            String name = scanner.nextLine();
            participants.add(name);
        }

        // Randomize the list of participants
        Collections.shuffle(participants);

        System.out.println("\nRandomized Tournament Draw:");
        for (int i = 0; i < participants.size(); i++) {
            System.out.println((i + 1) + ". " + participants.get(i));
        }

        scanner.close();
    }
}
