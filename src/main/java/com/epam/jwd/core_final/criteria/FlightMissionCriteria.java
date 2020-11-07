package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;

import com.epam.jwd.core_final.domain.Spaceship;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long distance;
    private final Spaceship assignedSpaceship;
    private final List<CrewMember> assignedCrew;
    private final MissionResult missionResult;

    public List<CrewMember> getAssignedCrew() {
        return assignedCrew;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Long getDistance() {
        return distance;
    }

    public Spaceship getAssignedSpaceship() {
        return assignedSpaceship;
    }

    public MissionResult getMissionResult() {
        return missionResult;
    }

    public static class Builder extends Criteria.Builder {
        private LocalDate startDate = null;
        private LocalDate endDate = null;
        private Long distance = null;
        private Spaceship assignedSpaceship = null;
        private List<CrewMember> assignedCrew = null;
        private MissionResult missionResult = null;

        public Builder assignedSpaceship(Spaceship assignedSpaceship) {
            this.assignedSpaceship = assignedSpaceship;
            return this;
        }


        public Builder assignedCrew(List<CrewMember> assignedCrew) {
            this.assignedCrew = assignedCrew;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder distance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Builder missionResult(MissionResult missionResult) {
            this.missionResult = missionResult;
            return this;
        }

        public FlightMissionCriteria build() {
            return new FlightMissionCriteria(this);
        }
    }

    private FlightMissionCriteria(Builder builder) {
        super(builder);
        startDate = builder.startDate;
        endDate = builder.endDate;
        distance = builder.distance;
        assignedSpaceship = builder.assignedSpaceship;
        assignedCrew = builder.assignedCrew;
        missionResult = builder.missionResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlightMissionCriteria that = (FlightMissionCriteria) o;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(distance, that.distance) &&
                Objects.equals(assignedSpaceship, that.assignedSpaceship) &&
                Objects.equals(assignedCrew, that.assignedCrew) &&
                missionResult == that.missionResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startDate, endDate, distance, assignedSpaceship, assignedCrew, missionResult);
    }
}
