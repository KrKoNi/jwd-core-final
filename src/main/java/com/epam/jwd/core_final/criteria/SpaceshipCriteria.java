package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;

/**
 * Should be a builder for {@link Spaceship} fields
 */
public class SpaceshipCriteria extends Criteria<Spaceship> {

    private final Role role;
    private final Rank rank;
    private final Boolean isReadyForNextMissions;

    public static class Builder extends Criteria.Builder {
        private Role role;
        private Rank rank;
        private Boolean isReadyForNextMissions;

        public Builder rank(Rank rank) {
            this.rank = rank;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
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
        rank = builder.rank;
        role = builder.role;
        isReadyForNextMissions = builder.isReadyForNextMissions;
    }

}
