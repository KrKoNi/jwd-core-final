package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;

import com.epam.jwd.core_final.domain.Spaceship;

import java.time.LocalDate;
import java.util.List;

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

    public static class Builder extends Criteria.Builder {
        private LocalDate startDate = LocalDate.now();
        private LocalDate endDate = LocalDate.MAX;
        private Long distance = 0L;
        private final Spaceship assignedSpaceship;
        private final List<CrewMember> assignedCrew;
        private MissionResult missionResult = MissionResult.PLANNED;

        public Builder(Spaceship assignedSpaceship, List<CrewMember> assignedCrew) {
            this.assignedSpaceship = assignedSpaceship;
            this.assignedCrew = assignedCrew;
        }

        public Builder setStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setDistance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Builder setMissionResult(MissionResult missionResult) {
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
