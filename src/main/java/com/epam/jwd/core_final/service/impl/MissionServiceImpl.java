package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.MissionService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MissionServiceImpl implements MissionService {

    private MissionServiceImpl() {

    }

    private final static MissionServiceImpl INSTANCE = new MissionServiceImpl();

    public static MissionServiceImpl getInstance() {
        return INSTANCE;
    }


    @Override
    public List<FlightMission> findAllMissions() {
        return (List<FlightMission>) NassaContext.getInstance().retrieveBaseEntityList(FlightMission.class);
    }

    @Override
    public List<FlightMission> findAllMissionsByCriteria(Criteria<? extends FlightMission> criteria) {
        return null;
    }

    @Override
    public Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria) {
        List<FlightMission> missions = findAllMissions();
        FlightMissionCriteria flightMissionCriteria = (FlightMissionCriteria) criteria;

        return missions.stream().filter(mission -> (
                        (flightMissionCriteria.getName() == null || mission.getName().equals(flightMissionCriteria.getName()))
                                && (flightMissionCriteria.getDistance() == null || flightMissionCriteria.getDistance().equals(mission.getDistance()))
                                && (flightMissionCriteria.getStartDate() == null || flightMissionCriteria.getStartDate().equals(mission.getStartDate()))
                )
        ).findFirst();
    }

    @Override
    public void printAllMissions() {
        List<FlightMission> flightMissions = findAllMissions();
        AtomicInteger i = new AtomicInteger();
        flightMissions.stream()
                .map(flightMission -> (i.incrementAndGet()) + ". " + flightMission.toString())
                .forEachOrdered(System.out::println);
    }

    @Override
    public FlightMission updateMissionDetails(FlightMission oldFlightMission, FlightMission updatedFlightMission) {
        oldFlightMission.setDistance(updatedFlightMission.getDistance());
        oldFlightMission.setStartDate(updatedFlightMission.getStartDate());
        oldFlightMission.setEndDate(updatedFlightMission.getEndDate());
        return oldFlightMission;
    }

    @Override
    public FlightMission createMission(String name, LocalDateTime startDate, LocalDateTime endDate, Long distance) {
        FlightMission flightMission = FlightMissionFactory.getInstance().create(name, startDate, endDate, distance);
        Collection<FlightMission> flightMissions = NassaContext.getInstance().retrieveBaseEntityList(FlightMission.class);
        flightMissions.add(flightMission);
        return flightMission;
    }

    @Override
    public Double calculateMissionProgress(FlightMission flightMission) {
        double progress = (double) Duration.between(LocalDateTime.now(), flightMission.getStartDate()).toMillis()
                / (double) Duration.between(flightMission.getEndDate(), flightMission.getStartDate()).toMillis();

        return progress * 100 > 100 ? 100 : progress < 0 ? 0 : progress * 100;
    }

    @Override
    public void missionStatusUpdate(FlightMission mission) {
        if (mission.getStartDate().isBefore(LocalDateTime.now()) && mission.getMissionResult() == MissionResult.PLANNED) {
            mission.setMissionResult(MissionResult.IN_PROGRESS);
        }
        if (mission.getEndDate().isBefore(LocalDateTime.now()) && mission.getMissionResult() == MissionResult.IN_PROGRESS) {
            finishMission(mission);
        }
    }

    @Override
    public void finishMission(FlightMission mission) {
        Random rand = new Random();
        MissionResult missionResult = rand.nextBoolean() ? MissionResult.COMPLETED : MissionResult.FAILED;
        mission.setMissionResult(missionResult);
        mission.getAssignedCrew()
                .forEach(crewMember -> crewMember.setReadyForNextMissions(missionResult == MissionResult.COMPLETED));
        mission.getAssignedSpaceship().setReadyForNextMissions(missionResult == MissionResult.COMPLETED);
    }

    @Override
    public FlightMission createTemporaryMission(LocalDateTime startDate, LocalDateTime endDate, Long distance) {
        return new FlightMission("", startDate, endDate, distance);
    }

    @Override
    public List<FlightMission> getMissionsWithCrewMember(CrewMember crewMember) {
        List<FlightMission> flightMissionsWithCrewMember;
        List<FlightMission> flightMissions = findAllMissions();

        flightMissionsWithCrewMember = flightMissions.stream()
                .filter(flightMission -> flightMission.getAssignedCrew().stream()
                        .anyMatch(crewMember::equals))
                .collect(Collectors.toList());


        return flightMissionsWithCrewMember;
    }
}
