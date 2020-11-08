package com.epam.jwd.core_final.domain;

/**
 * Expected fields:
 * <p>
 * role {@link Role} - member role
 * rank {@link Rank} - member rank
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class CrewMember extends AbstractBaseEntity {
    private Role role;
    private Rank rank;
    private Boolean isReadyForNextMissions;


    public CrewMember(Role role, String name, Rank rank) {
        super(name);
        this.role = role;
        this.rank = rank;
        isReadyForNextMissions = true;
    }

    public Role getRole() {
        return role;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setReadyForNextMissions(Boolean readyForNextMissions) {
        isReadyForNextMissions = readyForNextMissions;
    }

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    @Override
    public String toString() {
        return "CrewMember{" +
                "name=" + getName() +
                ", id=" + getId() +
                ", role=" + role +
                ", rank=" + rank +
                ", isReadyForNextMissions=" + isReadyForNextMissions +
                '}';
    }
}
