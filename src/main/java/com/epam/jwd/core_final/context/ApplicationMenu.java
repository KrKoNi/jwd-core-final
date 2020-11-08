package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.FreeSpaceshipAbsentException;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

// todo replace Object with your own types
@FunctionalInterface
public interface ApplicationMenu {

    ApplicationContext getApplicationContext();

    default String printAvailableOptions() {
        String availableOptions = "1. Create mission\n" +
                "2. View all crew members\n" +
                "3. View all spaceships\n" +
                "4. View all missions\n" +
                "5. Print all crew members to JSON\n" +
                "6. Print all spaceships to JSON\n" +
                "7. Print all missions to JSON\n" +
                "0. Close the application";
        return availableOptions;
    }


    default void handleUserInput(String o) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;
        Collection<CrewMember> crewMembers = CrewServiceImpl.getInstance().findAllCrewMembers();
        Collection<Spaceship> spaceships = SpaceshipServiceImpl.getInstance().findAllSpaceships();
        Collection<FlightMission> missions = MissionServiceImpl.getInstance().findAllMissions();

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        ApplicationProperties applicationProperties = NassaContext.getInstance().getApplicationProperties();


        while (true) {
            System.out.println(o);
            choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    System.out.println("Closing app");
                    return;
                case 1:
                    System.out.println("Enter mission name:");
                    String name = scanner.next();
                    System.out.println("Enter flight distance:");
                    Long distance = scanner.nextLong();
//                    System.out.println("Enter start date in format " + applicationProperties.getDateTimeFormat());
//                    LocalDateTime startDate = LocalDateTime.parse(scanner.next(), DateTimeFormatter.ofPattern(applicationProperties.getDateTimeFormat()));
//                    LocalDateTime endDate = LocalDateTime.parse(scanner.next(), DateTimeFormatter.ofPattern(applicationProperties.getDateTimeFormat()));
                    FlightMission flightMission = MissionServiceImpl.getInstance().createMission(name, LocalDateTime.now(), LocalDateTime.MAX, distance);

                    List<Spaceship> spaceshipCollection = SpaceshipServiceImpl.getInstance()
                            .findAllSpaceshipsByCriteria(new SpaceshipCriteria.Builder() {{
                                flightDistance(distance);
                                isReadyForNextMissions(true);
                            }}.build());
                    int i = 0;
                    System.out.println("Choose spaceship for mission");
                    for (Spaceship spaceship : spaceshipCollection) {
                        i++;
                        System.out.println(i + ". " + spaceship.getName() + ", number of members " +
                                (spaceship.getCrew().get(Role.MISSION_SPECIALIST)
                                        + spaceship.getCrew().get(Role.FLIGHT_ENGINEER)
                                        + spaceship.getCrew().get(Role.PILOT)
                                        + spaceship.getCrew().get(Role.COMMANDER))
                        );
                    }
                    int choice2 = scanner.nextInt();
                    System.out.println("Spaceship " + spaceshipCollection.get(choice2 - 1).getName() + " was assigned");

                    SpaceshipServiceImpl.getInstance().assignSpaceshipOnMission(flightMission, spaceshipCollection.get(choice2 - 1));

                    CrewServiceImpl.getInstance().assignCrewMemberOnMission(flightMission);

                    break;
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
                                    + ", flight distance: " + spaceship.getFlightDistance() + "}")
                            .forEachOrdered(System.out::println);
                    break;
                case 4:
                    missions.stream()
                            .map(mission -> "{" +
                                    "Name: " + mission.getName()
                                    + ", flight distance: " + mission.getDistance()
                                    + ", start date: " + mission.getStartDate()
                                    + ", end date: " + mission.getEndDate()
                                    + "}")
                            .forEachOrdered(System.out::println);
                    break;
                case 5:
                    System.out.println("Writing to crewmember.json");
                    try (FileOutputStream crewmemberFile = new FileOutputStream("src/main/resources/" + applicationProperties.getOutputRootDir() + "/crewmember.json")) {
                        objectMapper.writeValue(crewmemberFile, crewMembers);
                    }
                    System.out.println("Finished");
                    break;
                case 6:
                    System.out.println("Writing to spaceship.json");
                    try (FileOutputStream spaceshipFile = new FileOutputStream("src/main/resources/" + applicationProperties.getOutputRootDir() + "/spaceship.json")) {
                        objectMapper.writeValue(spaceshipFile, spaceships);
                    }
                    System.out.println("Finished");
                    break;
                case 7:
                    System.out.println("Writing to missions.json");
                    try (FileOutputStream missionFile = new FileOutputStream("src/main/resources/" + applicationProperties.getOutputRootDir() + "/mission.json")) {
                        objectMapper.writeValue(missionFile, missions);
                    }
                    System.out.println("Finished");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }
}
