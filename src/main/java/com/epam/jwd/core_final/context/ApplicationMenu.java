package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// todo replace Object with your own types
@FunctionalInterface
public interface ApplicationMenu {

    ApplicationContext getApplicationContext();

    default String printAvailableOptions() {
        return "1. Create mission\n" +
                "2. View\n" +
                "3. Update\n" +
                "4. Print to JSON\n" +
                "0. Close the app";
    }



    default String printEntities() {
        return "1. Crewmembers\n" +
                "2. Spaceships\n" +
                "3. Missions\n" +
                "0. Back";
    }

    default void handleUserInput(String o) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int choice, choice2;
        Collection<CrewMember> crewMembers = CrewServiceImpl.getInstance().findAllCrewMembers();
        Collection<Spaceship> spaceships = SpaceshipServiceImpl.getInstance().findAllSpaceships();
        Collection<FlightMission> missions = MissionServiceImpl.getInstance().findAllMissions();



        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        ApplicationProperties applicationProperties = NassaContext.getInstance().getApplicationProperties();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(applicationProperties.getDateTimeFormat());

        while (true) {
            missions.forEach(mission -> MissionServiceImpl.getInstance().missionStatusUpdate(mission));
            System.out.println(printAvailableOptions());
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

                    System.out.println("Enter start date in format " + applicationProperties.getDateTimeFormat());
                    scanner.nextLine();
                    LocalDateTime startDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);

                    System.out.println("Enter finish date in format " + applicationProperties.getDateTimeFormat());
                    LocalDateTime endDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);

                    FlightMission flightMission = MissionServiceImpl
                            .getInstance().createMission(name, startDate, endDate, distance);

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
                    int spaceshipIndex = scanner.nextInt() - 1;
                    System.out.println("Spaceship " + spaceshipCollection.get(spaceshipIndex).getName() + " was assigned");

                    SpaceshipServiceImpl.getInstance().assignSpaceshipOnMission(flightMission, spaceshipCollection.get(spaceshipIndex));

                    CrewServiceImpl.getInstance().assignCrewMemberOnMission(flightMission);

                    break;
                case 2:

                    System.out.println(printEntities());
                    choice2 = scanner.nextInt();

                    switch (choice2) {
                        case 0:
                            break;
                        case 1:
                            crewMembers.stream()
                                    .map(crewMember -> "{Name: " + crewMember.getName()
                                            + ", role: " + crewMember.getRole()
                                            + ", rank: " + crewMember.getRank() + "}")
                                    .forEachOrdered(System.out::println);
                            break;
                        case 2:
                            spaceships.stream()
                                    .map(spaceship -> "{Name: " + spaceship.getName()
                                            + ", flight distance: " + spaceship.getFlightDistance() + "}")
                                    .forEachOrdered(System.out::println);
                            break;
                        case 3:
                            missions.stream()
                                    .map(FlightMission::toString)
                                    .forEachOrdered(System.out::println);
                            break;
                    }
                    break;
                case 3:

                case 4:

                    System.out.println(printEntities());
                    choice2 = scanner.nextInt();

                    switch (choice2) {
                        case 0:
                            break;
                        case 1:
                            System.out.println("Writing to crewmember.json");
                            try (FileOutputStream crewmemberFile = new FileOutputStream("src/main/resources/" + applicationProperties.getOutputRootDir() + "/crewmember.json")) {
                                objectMapper.writeValue(crewmemberFile, crewMembers);
                            }
                            System.out.println("Finished");
                            break;
                        case 2:
                            System.out.println("Writing to spaceship.json");
                            try (FileOutputStream spaceshipFile = new FileOutputStream("src/main/resources/" + applicationProperties.getOutputRootDir() + "/spaceship.json")) {
                                objectMapper.writeValue(spaceshipFile, spaceships);
                            }
                            System.out.println("Finished");
                            break;
                        case 3:
                            System.out.println("Writing to missions.json");
                            try (FileOutputStream missionFile = new FileOutputStream("src/main/resources/" + applicationProperties.getOutputRootDir() + "/mission.json")) {
                                objectMapper.writeValue(missionFile, missions);
                            }
                            System.out.println("Finished");
                            break;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }
}
