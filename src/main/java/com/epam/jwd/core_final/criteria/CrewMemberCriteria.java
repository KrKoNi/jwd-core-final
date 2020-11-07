package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

import java.util.Objects;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.CrewMember} fields
 */
public class CrewMemberCriteria extends Criteria<CrewMember> {
    private final Role role;
    private final Rank rank;
    private final Boolean isReadyForNextMissions;

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public Rank getRank() {
        return rank;
    }

    public Role getRole() {
        return role;
    }

    public static class Builder extends Criteria.Builder {
        private Role role = null;
        private Rank rank = null;
        private Boolean isReadyForNextMissions = true;

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


        public CrewMemberCriteria build() {
            return new CrewMemberCriteria(this);
        }
    }

    private CrewMemberCriteria(Builder builder) {
        super(builder);
        rank = builder.rank;
        role = builder.role;
        isReadyForNextMissions = builder.isReadyForNextMissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CrewMemberCriteria that = (CrewMemberCriteria) o;
        return role == that.role &&
                rank == that.rank &&
                Objects.equals(isReadyForNextMissions, that.isReadyForNextMissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), role, rank, isReadyForNextMissions);
    }
}
