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

    private final Map<Role, Short> crew;
    private final Long flightDistance;
    private final Boolean isReadyForNextMissions;

    public static class Builder extends Criteria.Builder {
        private Map<Role, Short> crew = null;
        private Long flightDistance = null;
        private Boolean isReadyForNextMissions = true;

        public Builder flightDistance(Long flightDistance) {
            this.flightDistance = flightDistance;
            return this;
        }

        public Builder crew(Map<Role, Short> crew) {
            this.crew = crew;
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
        crew = builder.crew;
        flightDistance = builder.flightDistance;
        isReadyForNextMissions = builder.isReadyForNextMissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpaceshipCriteria that = (SpaceshipCriteria) o;
        return (Objects.equals(crew, that.crew) || crew == null || that.crew == null) &&
                (Objects.equals(flightDistance, that.flightDistance) || flightDistance == null || that.flightDistance == null) &&
                (Objects.equals(isReadyForNextMissions, that.isReadyForNextMissions) || isReadyForNextMissions == null || that.isReadyForNextMissions == null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), crew, flightDistance, isReadyForNextMissions);
    }
}