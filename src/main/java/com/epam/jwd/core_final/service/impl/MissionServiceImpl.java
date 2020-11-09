package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.MissionService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
        return Optional.empty();
    }

    public void printAllMissions() {
        List<FlightMission> flightMissions = findAllMissions();
        AtomicInteger i = new AtomicInteger();
        flightMissions.stream().map(flightMission -> (i.incrementAndGet()) + ". " + flightMission.toString()).forEachOrdered(System.out::println);
    }

    @Override
    public FlightMission updateMissionDetails(FlightMission flightMission, FlightMission updatedFlightMission) {



        return null;
    }

    @Override
    public FlightMission createMission(String name, LocalDateTime startDate, LocalDateTime endDate, Long distance) {
        FlightMission flightMission = FlightMissionFactory.getInstance().create(name, startDate, endDate, distance);
        Collection<FlightMission> flightMissions = NassaContext.getInstance().retrieveBaseEntityList(FlightMission.class);
        flightMissions.add(flightMission);
        return flightMission;
    }

    public Double calculateMissionProgress(FlightMission flightMission) {
        double progress = (double) Duration.between(LocalDateTime.now(), flightMission.getStartDate()).toMillis()
                / (double) Duration.between(flightMission.getEndDate(), flightMission.getStartDate()).toMillis();

        return progress*100 > 100 ? 100 : progress < 0 ? 0 : progress*100;
    }

    public void missionStatusUpdate (FlightMission mission) {
        if(mission.getStartDate().isBefore(LocalDateTime.now()) && mission.getMissionResult() == MissionResult.PLANNED) {
            mission.setMissionResult(MissionResult.IN_PROGRESS);
        }
        if (mission.getEndDate().isBefore(LocalDateTime.now()) && mission.getMissionResult() == MissionResult.IN_PROGRESS) {
            finishMission(mission);
        }
    }

    public void finishMission(FlightMission mission) {
        Random rand = new Random();
        MissionResult missionResult = rand.nextBoolean() ? MissionResult.COMPLETED : MissionResult.FAILED;
        mission.setMissionResult( missionResult );
        for (CrewMember crewMember : mission.getAssignedCrew()) {
            crewMember.setReadyForNextMissions(missionResult == MissionResult.COMPLETED); // TODO: 11/9/20 change state using update method
        }
        mission.getAssignedSpaceship().setReadyForNextMissions(missionResult == MissionResult.COMPLETED); // TODO: 11/9/20 change state using update method
    }

}
