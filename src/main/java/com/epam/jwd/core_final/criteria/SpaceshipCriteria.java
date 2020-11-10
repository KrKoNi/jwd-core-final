package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.Spaceship;

import java.util.Objects;

/**
 * Should be a builder for {@link Spaceship} fields
 */
public class SpaceshipCriteria extends Criteria<Spaceship> {

    private final Long flightDistance;
    private final Boolean isReadyForNextMissions;

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public Long getFlightDistance() {
        return flightDistance;
    }

    public static class Builder extends Criteria.Builder {
        private Long flightDistance = null;
        private Boolean isReadyForNextMissions = true;

        public Builder flightDistance(Long flightDistance) {
            this.flightDistance = flightDistance;
            return this;
        }

        public Builder isReadyForNextMissions(Boolean isReadyForNextMissions) {
            this.isReadyForNextMissions = isReadyForNextMissions;
            return this;
        }


        public SpaceshipCriteria build() {
            return new SpaceshipCriteria(this);
        }
    }

    private SpaceshipCriteria(Builder builder) {
        super(builder);
        flightDistance = builder.flightDistance;
        isReadyForNextMissions = builder.isReadyForNextMissions;
    }
    
}