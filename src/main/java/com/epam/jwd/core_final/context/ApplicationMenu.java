package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
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

    Logger logger = Logger.getLogger(ApplicationMenu.class.getName());


    default void handleUserInput() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;
        List<FlightMission> missions = MissionServiceImpl.getInstance().findAllMissions();
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
                    String name = null;
                    try {
                        name = scanner.next();
                    } catch (Exception e) {
                        System.out.println("Something wrong.");
                        break;
                    }

                    System.out.println("Enter flight distance:");
                    Long distance = null;
                    try {
                        distance = scanner.nextLong();
                    } catch (Exception e) {
                        System.out.println("You entered wrong distance. Try again.");
                        break;
                    }

                    System.out.println("Enter start date in format " + applicationProperties.getDateTimeFormat());
                    scanner.nextLine();
                    LocalDateTime startDate = null;
                    try {
                        startDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                    } catch (Exception e) {
                        System.out.println("You entered wrong data. Try again");
                    }

                    System.out.println("Enter finish date in format " + applicationProperties.getDateTimeFormat());
                    LocalDateTime endDate = null;
                    try {
                        endDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                    } catch (Exception e) {
                        System.out.println("You entered wrong data. Try again");
                    }

                    FlightMission flightMission = MissionServiceImpl
                            .getInstance().createMission(name, startDate, endDate, distance);

                    Long finalDistance = distance;
                    List<Spaceship> spaceshipCollection = SpaceshipServiceImpl.getInstance()
                            .findAllSpaceshipsByCriteria(new SpaceshipCriteria.Builder() {{
                                flightDistance(finalDistance);
                                isReadyForNextMissions(true);
                            }}.build());

                    System.out.println("Choose spaceship for mission");
                    SpaceshipServiceImpl.getInstance().printAllAvailableSpaceships();
                    int spaceshipToAssignIndex = scanner.nextInt() - 1;

                    System.out.println("Spaceship " + spaceshipCollection.get(spaceshipToAssignIndex).getName() + " was assigned");

                    SpaceshipServiceImpl.getInstance().assignSpaceshipOnMission(flightMission, spaceshipCollection.get(spaceshipToAssignIndex));

                    CrewServiceImpl.getInstance().assignRandomCrewMembersOnMission(flightMission);

                    break;
                case 2:
                    handleViewMenu();
                    break;
                case 3:
                    handleUpdateMenu();
                    break;
                case 4:
                    handlePrintToJsonMenu();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }

    default void handleViewMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(printEntities());
        int choice = scanner.nextInt();

        switch (choice) {
            case 0:
                break;
            case 1:
                CrewServiceImpl.getInstance().printAllCrewMembers();
                break;
            case 2:
                SpaceshipServiceImpl.getInstance().printAllSpaceships();
                break;
            case 3:
                MissionServiceImpl.getInstance().printAllMissions();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }
    }

    default void handleUpdateMenu() {

        List<CrewMember> crewMembers = CrewServiceImpl.getInstance().findAllCrewMembers();
        List<Spaceship> spaceships = SpaceshipServiceImpl.getInstance().findAllSpaceships();
        List<FlightMission> missions = MissionServiceImpl.getInstance().findAllMissions();

        Scanner scanner = new Scanner(System.in);
        System.out.println(printEntities());

        int choice = scanner.nextInt();

        switch (choice) {
            case 0:
                break;
            case 1:
                CrewServiceImpl.getInstance().printAllCrewMembers();

                int crewMemberIndex = scanner.nextInt() - 1;

                CrewMember crewMemberToChange = null;
                try {
                    crewMemberToChange = crewMembers.get(crewMemberIndex);
                } catch (Exception e) {
                    System.out.println("Index out of bound");
                    break;
                }

                System.out.println("Choose new role (enter 0 to leave previous role): ");
                System.out.println(Arrays.toString(Role.values()));
                Long crewMemberNewRoleId = scanner.nextLong();
                crewMemberNewRoleId = crewMemberNewRoleId == 0L ? crewMemberToChange.getRole().getId() : crewMemberNewRoleId;
                Role crewMemberNewRole = Role.resolveRoleById(crewMemberNewRoleId);

                System.out.println("Choose new rank (enter 0 to leave previous rank): ");
                System.out.println(Arrays.toString(Rank.values()));
                Long crewMemberNewRankId = scanner.nextLong();
                crewMemberNewRankId = crewMemberNewRankId == 0L ? crewMemberToChange.getRank().getId() : crewMemberNewRankId;
                Rank crewMemberNewRank = Rank.resolveRankById(crewMemberNewRankId);

                CrewMember updatedCrewMember = CrewServiceImpl.getInstance().createTemporaryCrewMember(crewMemberNewRole, crewMemberToChange.getName(), crewMemberNewRank);
                CrewServiceImpl.getInstance().updateCrewMemberDetails(crewMemberToChange, updatedCrewMember);

                break;
            case 2:
                SpaceshipServiceImpl.getInstance().printAllSpaceships();

                int spaceshipToUpdateIndex = scanner.nextInt() - 1;

                Spaceship spaceshipToUpdate = null;
                try {
                    spaceshipToUpdate = spaceships.get(spaceshipToUpdateIndex);
                } catch (Exception e) {
                    System.out.println("Index out of bound");
                    break;
                }

                System.out.println("Enter new spaceship flight distance: ");
                Long spaceshipFlightDistance = scanner.nextLong();

                Spaceship updatedSpaceship = SpaceshipServiceImpl.getInstance().createTemporarySpaceship(spaceshipToUpdate.getName(), spaceshipFlightDistance, spaceshipToUpdate.getCrew());
                SpaceshipServiceImpl.getInstance().updateSpaceshipDetails(spaceshipToUpdate, updatedSpaceship);
                break;
            case 3:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }
    }

    default void handlePrintToJsonMenu() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        List<CrewMember> crewMembers = CrewServiceImpl.getInstance().findAllCrewMembers();
        List<Spaceship> spaceships = SpaceshipServiceImpl.getInstance().findAllSpaceships();
        List<FlightMission> missions = MissionServiceImpl.getInstance().findAllMissions();

        ApplicationProperties applicationProperties = NassaContext.getInstance().getApplicationProperties();

        Scanner scanner = new Scanner(System.in);

        System.out.println(printEntities());
        int choice = scanner.nextInt();

        String separator = FileSystems.getDefault().getSeparator();

        String outputPath = "src" + separator + "main" + separator + "resources" + separator + applicationProperties.getOutputRootDir() + separator;

        switch (choice) {
            case 0:
                break;
            case 1:
                System.out.println("Writing to crewmember.json");
                try (FileOutputStream crewmemberFile = new FileOutputStream(outputPath + "crewmember.json")) {
                    objectMapper.writeValue(crewmemberFile, crewMembers);
                }
                System.out.println("Finished");
                break;
            case 2:
                System.out.println("Writing to spaceship.json");
                try (FileOutputStream spaceshipFile = new FileOutputStream(outputPath + "spaceship.json")) {
                    objectMapper.writeValue(spaceshipFile, spaceships);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Finished");
                break;
            case 3:
                System.out.println("Writing to missions.json");

                try (FileOutputStream missionFile = new FileOutputStream(outputPath + "mission.json")) {
                    objectMapper.writeValue(missionFile, missions);
                }
                System.out.println("Finished");
                break;
        }
    }
}
