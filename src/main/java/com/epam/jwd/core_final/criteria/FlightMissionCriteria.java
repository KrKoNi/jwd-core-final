package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Spaceship;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Long distance;
    private final Spaceship assignedSpaceship;
    private final List<CrewMember> assignedCrew;
    private final MissionResult missionResult;

    public List<CrewMember> getAssignedCrew() {
        return assignedCrew;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public LocalDateTime getStartDate() {
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
        private LocalDateTime startDate = null;
        private LocalDateTime endDate = null;
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

        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDateTime endDate) {
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

}
