package com.epam.jwd.core_final.domain;

import java.util.Map;

/**
 * crew {@link java.util.Map<Role, Short>}
 * flightDistance {@link Long} - total available flight distance
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */

public class Spaceship extends AbstractBaseEntity {
    private final Map<Role, Short> crew;
    private Long flightDistance;
    private Boolean isReadyForNextMissions;

    public Spaceship(String name, Long flightDistance, Map<Role, Short> crew) {
        super(name);
        this.crew = crew;
        this.flightDistance = flightDistance;
        isReadyForNextMissions = true;
    }

    public Long getFlightDistance() {
        return flightDistance;
    }

    public void setFlightDistance(Long flightDistance) {
        this.flightDistance = flightDistance;
    }

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public void setReadyForNextMissions(Boolean readyForNextMissions) {
        isReadyForNextMissions = readyForNextMissions;
    }

    public Map<Role, Short> getCrew() {
        return crew;
    }

    @Override
    public String toString() {
        return "Spaceship{" +
                "name=" + getName() +
                ", id=" + getId() +
                ", flightDistance=" + flightDistance +
                ", isReadyForNextMissions=" + isReadyForNextMissions +
                '}';
    }
}
