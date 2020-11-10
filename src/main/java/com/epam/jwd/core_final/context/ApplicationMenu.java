package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EndDateIsBeforeStartDateException;
import com.epam.jwd.core_final.exception.UnknownEntityException;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        List<CrewMember> crewMembers = CrewServiceImpl.getInstance().findAllCrewMembers();
        List<Spaceship> spaceships = SpaceshipServiceImpl.getInstance().findAllSpaceships();

        ApplicationProperties applicationProperties = NassaContext.getInstance().getApplicationProperties();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(applicationProperties.getDateTimeFormat());
        LocalDateTime lastCheckTime = LocalDateTime.now();
        while (true) {
            if (Duration.between(lastCheckTime, LocalDateTime.now()).toMinutes() > applicationProperties.getFileRefreshRate()) {
                crewMembers.clear();
                spaceships.clear();
                NassaContext.getInstance().init();
                lastCheckTime = LocalDateTime.now();
            }

            missions.forEach(mission -> MissionServiceImpl.getInstance().missionStatusUpdate(mission));
            System.out.println(printAvailableOptions());
            choice = scanner.nextInt();

            switch (choice) {
                case 0:
                    logger.info("App was closed");
                    System.out.println("Closing app");
                    return;
                case 1: //create mission
                    System.out.println("Enter mission name:");
                    scanner.nextLine();
                    String name = null;
                    while (name == null) {
                        try {
                            name = scanner.nextLine();
                            String finalName = name;
                            Optional<FlightMission> optionalFlightMission = MissionServiceImpl.getInstance().findMissionByCriteria(
                                    new FlightMissionCriteria.Builder() {{
                                        name(finalName);
                                    }}.build());
                            if (optionalFlightMission.isPresent()) {
                                name = null;
                                System.out.println("Mission with this name already exists");
                            }
                        } catch (Exception e) {
                            System.out.println("Something wrong.");
                            break;
                        }

                    }
                    System.out.println("Enter flight distance:");
                    Long distance = null;
                    while (distance == null) {
                        try {
                            distance = scanner.nextLong();
                        } catch (Exception e) {
                            System.out.println("Distance should be a number");
                            scanner.nextLine();
                        }
                    }

                    System.out.println("Enter start date in format " + applicationProperties.getDateTimeFormat());
                    scanner.nextLine();
                    LocalDateTime startDate = null;
                    while (startDate == null) {
                        try {
                            startDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                        } catch (Exception e) {
                            System.out.println("You entered wrong data. Try again");
                        }
                    }

                    System.out.println("Enter finish date in format " + applicationProperties.getDateTimeFormat());
                    LocalDateTime endDate = null;
                    while (endDate == null) {
                        try {
                            endDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                            if (!startDate.isBefore(endDate)) {
                                endDate = null;
                                throw new EndDateIsBeforeStartDateException("Finish time has to be after beginning.");
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("You entered wrong data. Try again");
                        } catch (EndDateIsBeforeStartDateException e) {
                            System.out.println("Finish time has to be after beginning. Try again");
                        }
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

                    SpaceshipServiceImpl.getInstance().assignSpaceshipOnMission(flightMission, spaceshipCollection.get(spaceshipToAssignIndex));
                    System.out.println("Spaceship " + spaceshipCollection.get(spaceshipToAssignIndex).getName() + " was assigned");
                    logger.info("Spaceship " + spaceshipCollection.get(spaceshipToAssignIndex).getName() + " was assigned");

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

        ApplicationProperties applicationProperties = NassaContext.getInstance().getApplicationProperties();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(applicationProperties.getDateTimeFormat());

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
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Index out of bound");
                    System.out.println("Index out of bound");
                    break;
                }

                System.out.println("Choose new role (enter 0 to leave previous role): ");
                System.out.println(Arrays.toString(Role.values()));
                Long crewMemberNewRoleId = scanner.nextLong();
                crewMemberNewRoleId = crewMemberNewRoleId == 0L ? crewMemberToChange.getRole().getId() : crewMemberNewRoleId;
                Role crewMemberNewRole = null;
                try {
                    crewMemberNewRole = Role.resolveRoleById(crewMemberNewRoleId);
                } catch (UnknownEntityException e) {
                    logger.error("There is no role with entered id");
                }

                System.out.println("Choose new rank (enter 0 to leave previous rank): ");
                System.out.println(Arrays.toString(Rank.values()));
                Long crewMemberNewRankId = scanner.nextLong();
                crewMemberNewRankId = crewMemberNewRankId == 0L ? crewMemberToChange.getRank().getId() : crewMemberNewRankId;
                Rank crewMemberNewRank = null;
                try {
                    crewMemberNewRank = Rank.resolveRankById(crewMemberNewRankId);
                } catch (UnknownEntityException e) {
                    logger.error("There is no rank with entered id");
                    System.out.println("There is no rank with entered id");
                }

                CrewMember updatedCrewMember = CrewServiceImpl.getInstance().createTemporaryCrewMember(crewMemberNewRole, crewMemberNewRank);
                CrewServiceImpl.getInstance().updateCrewMemberDetails(crewMemberToChange, updatedCrewMember);
                logger.info("CrewMember " + crewMemberToChange.getName() + " was updated");
                break;
            case 2:
                SpaceshipServiceImpl.getInstance().printAllSpaceships();

                int spaceshipToUpdateIndex = scanner.nextInt() - 1;

                Spaceship spaceshipToUpdate = null;
                try {
                    spaceshipToUpdate = spaceships.get(spaceshipToUpdateIndex);
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Index out of bound");
                    System.out.println("Index out of bound");
                    break;
                }


                System.out.println("Enter new spaceship flight distance: ");
                Long spaceshipFlightDistance = null;
                while (spaceshipFlightDistance == null) {
                    try {
                        spaceshipFlightDistance = scanner.nextLong();
                    } catch (Exception e) {
                        System.out.println("Distance has to be a number");
                        logger.error("Distance has to be a number");
                    }
                }
                Spaceship updatedSpaceship = SpaceshipServiceImpl.getInstance().createTemporarySpaceship(spaceshipFlightDistance);
                SpaceshipServiceImpl.getInstance().updateSpaceshipDetails(spaceshipToUpdate, updatedSpaceship);
                logger.info("Spaceship " + spaceshipToUpdate.getName() + " was updated");
                break;
            case 3:
                MissionServiceImpl.getInstance().printAllMissions();

                int missionToUpdateIndex = scanner.nextInt() - 1;

                FlightMission missionToUpdate = null;
                try {
                    missionToUpdate = missions.get(missionToUpdateIndex);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Index out of bound");
                    break;
                }

                System.out.println("Enter updated flight distance:");
                Long updatedDistance = null;
                while (updatedDistance == null) {
                    try {
                        updatedDistance = scanner.nextLong();
                    } catch (Exception e) {
                        System.out.println("Distance has to be a number");
                        logger.error("Distance has to be a number");
                    }
                }

                System.out.println("Enter start date in format " + applicationProperties.getDateTimeFormat());
                scanner.nextLine();
                LocalDateTime updatedStartDate = null;
                while (updatedStartDate == null) {
                    try {
                        updatedStartDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("You entered wrong data. Try again");
                    }
                }
                System.out.println("Enter finish date in format " + applicationProperties.getDateTimeFormat());
                LocalDateTime updatedEndDate = null;
                while (updatedEndDate == null) {
                    try {
                        updatedEndDate = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);
                        if (!updatedStartDate.isBefore(updatedEndDate)) {
                            updatedEndDate = null;
                            throw new EndDateIsBeforeStartDateException("Finish time has to be after beginning.");
                        }

                    } catch (Exception e) {
                        System.out.println("You entered wrong data. Try again");

                    }
                }
                FlightMission updatedMission = MissionServiceImpl.getInstance().createTemporaryMission(updatedStartDate, updatedEndDate, updatedDistance);
                MissionServiceImpl.getInstance().updateMissionDetails(missionToUpdate, updatedMission);

                missionToUpdate.getAssignedSpaceship().setReadyForNextMissions(true);

                Long finalDistance = updatedDistance;
                List<Spaceship> spaceshipCollection = SpaceshipServiceImpl.getInstance()
                        .findAllSpaceshipsByCriteria(new SpaceshipCriteria.Builder() {{
                            flightDistance(finalDistance);
                            isReadyForNextMissions(true);
                        }}.build());

                System.out.println("Choose spaceship for mission");
                SpaceshipServiceImpl.getInstance().printAllAvailableSpaceships();
                int spaceshipToAssignIndex = scanner.nextInt() - 1;

                SpaceshipServiceImpl.getInstance().assignSpaceshipOnMission(missionToUpdate, spaceshipCollection.get(spaceshipToAssignIndex));
                System.out.println("Spaceship " + spaceshipCollection.get(spaceshipToAssignIndex).getName() + " was assigned");
                logger.info("Mission " + missionToUpdate.getName() + " was updated");
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

        try {
            Files.createDirectory(Path.of(outputPath));
        } catch (FileAlreadyExistsException e) {
            logger.warn("Output directory already exists");
        }

        switch (choice) {
            case 0:
                break;
            case 1:
                System.out.println("Writing to crewmember.json");
                try (FileOutputStream crewmemberFile = new FileOutputStream(outputPath + "crewmember.json")) {
                    objectMapper.writeValue(crewmemberFile, crewMembers);
                } catch (IOException e) {
                    logger.error("Error while writing to crewmember.json");
                    e.printStackTrace();
                }
                logger.info("Info about crewmembers was written to crewmember.json");
                System.out.println("Finished");
                break;
            case 2:
                System.out.println("Writing to spaceship.json");
                try (FileOutputStream spaceshipFile = new FileOutputStream(outputPath + "spaceship.json")) {
                    objectMapper.writeValue(spaceshipFile, spaceships);
                } catch (IOException e) {
                    logger.error("Error while writing to spaceship.json");
                    e.printStackTrace();
                }
                logger.info("Info about spaceships was written to spaceship.json");
                System.out.println("Finished");
                break;
            case 3:
                System.out.println("Writing to missions.json");

                try (FileOutputStream missionFile = new FileOutputStream(outputPath + "mission.json")) {
                    objectMapper.writeValue(missionFile, missions);
                    logger.error("Error while writing to mission.json");
                }
                logger.info("Info about mission was written to mission.json");
                System.out.println("Finished");
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }
    }
}
