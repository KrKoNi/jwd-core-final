package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.MissionService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Override
    public FlightMission updateSpaceshipDetails(FlightMission flightMission) {
        return null;
    }

    @Override
    public FlightMission createMission(String name, LocalDateTime startDate, LocalDateTime endDate, Long distance) {
        FlightMission flightMission = FlightMissionFactory.getInstance().create(name, startDate, endDate, distance);
        Collection<FlightMission> flightMissions = NassaContext.getInstance().retrieveBaseEntityList(FlightMission.class);
        flightMissions.add(flightMission);
        return flightMission;
    }
}
