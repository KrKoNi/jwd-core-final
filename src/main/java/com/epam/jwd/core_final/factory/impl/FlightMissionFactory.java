package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.factory.EntityFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FlightMissionFactory implements EntityFactory<FlightMission> {
    private FlightMissionFactory() {

    }

    private final static FlightMissionFactory INSTANCE = new FlightMissionFactory();

    public static FlightMissionFactory getInstance() {
        return INSTANCE;
    }
    @Override
    public FlightMission create(Object... args) {
        return new FlightMission((String) args[0], (LocalDateTime) args[1], (LocalDateTime) args[2], (Long) args[3]);
    }
}
