package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;

import java.util.Map;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpaceshipCriteria that = (SpaceshipCriteria) o;
        return Objects.equals(flightDistance, that.flightDistance) &&
                Objects.equals(isReadyForNextMissions, that.isReadyForNextMissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flightDistance, isReadyForNextMissions);
    }
}