package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

// todo replace Object with your own types
@FunctionalInterface
public interface ApplicationMenu {

    ApplicationContext getApplicationContext();

    default String printAvailableOptions() {
        String availableOptions = "1. Create mission\n" +
                "2. View all crewmembers\n" +
                "3. View all spaceships\n" +
                "4. Print all crewmembers to JSON\n" +
                "5. Print all spaceships to JSON\n" +
                "0. Close the application";
        return availableOptions;
    }

    default void handleUserInput(String o) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;
        Collection<CrewMember> crewMembers = CrewServiceImpl.getInstance().findAllCrewMembers();
        Collection<Spaceship> spaceships = SpaceshipServiceImpl.getInstance().findAllSpaceships();
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        while (true) {
            System.out.println(o);
            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    System.out.println("Closing app");
                    return;
                case 2:
                    crewMembers.stream()
                            .map(crewMember -> "{Name: " + crewMember.getName()
                                    + ", role: " + crewMember.getRole()
                                    + ", rank: " + crewMember.getRank() + "}")
                            .forEach(System.out::println);
                    break;
                case 3:
                    spaceships.stream()
                            .map(spaceship -> "{Name: " + spaceship.getName()
                                    + ", role: " + spaceship.getFlightDistance() + "}")
                            .forEachOrdered(System.out::println);
                    break;
                case 4:
                    System.out.println("Writing to crewmember.json");
                    try (FileOutputStream crewmemberFile = new FileOutputStream("src/main/resources/output/crewmember.json")) {
                        objectMapper.writeValue(crewmemberFile, crewMembers);
                    }
                    System.out.println("Finished");
                    break;
                case 5:
                    System.out.println("Writing to spaceship.json");
                    try (FileOutputStream spaceshipFile = new FileOutputStream("src/main/resources/output/spaceship.json")) {
                            objectMapper.writeValue(spaceshipFile, spaceships);
                        System.out.println("Finished");
                    }

                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }
}
