package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.service.CrewService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CrewServiceImpl implements CrewService {

    private CrewServiceImpl() {

    }

    private final static CrewServiceImpl INSTANCE = new CrewServiceImpl();

    public static CrewServiceImpl getInstance() {
        return INSTANCE;
    }


    @Override
    public List<CrewMember> findAllCrewMembers() {
        return (List<CrewMember>) NassaContext.getInstance().retrieveBaseEntityList(CrewMember.class);
    }

    @Override
    public List<CrewMember> findAllCrewMembersByCriteria(Criteria<? extends CrewMember> criteria) {
        List<CrewMember> crewMembers = findAllCrewMembers();
        CrewMemberCriteria crewMemberCriteria = (CrewMemberCriteria) criteria;
        return crewMembers.stream().filter(crewMember -> (
                (crewMember.getName().equals(crewMemberCriteria.getName()) || crewMemberCriteria.getName() == null)
                        && (crewMember.getRank() == crewMemberCriteria.getRank() || crewMemberCriteria.getRank() == null)
                        && (crewMember.getReadyForNextMissions() == crewMemberCriteria.getReadyForNextMissions() || crewMemberCriteria.getReadyForNextMissions() == null)
                        && (crewMember.getRole() == crewMemberCriteria.getRole() || crewMemberCriteria.getRole() == null)
        )).collect(Collectors.toList());
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(Criteria<? extends CrewMember> criteria) {
        List<CrewMember> crewMembers = findAllCrewMembers();
        CrewMemberCriteria crewMemberCriteria = (CrewMemberCriteria) criteria;
        return crewMembers.stream().filter(crewMember -> (
                (crewMemberCriteria.getName() == null || crewMember.getName().equals(crewMemberCriteria.getName()))
                        && (crewMemberCriteria.getRank() == null || crewMember.getRank() == crewMemberCriteria.getRank())
                        && (crewMemberCriteria.getReadyForNextMissions() == null || crewMember.getReadyForNextMissions() == crewMemberCriteria.getReadyForNextMissions())
                        && (crewMemberCriteria.getRole() == null || crewMember.getRole() == crewMemberCriteria.getRole())
        )).findFirst();
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember crewMember) {
        return null;
    }

    @Override
    public void assignCrewMemberOnMission(FlightMission mission) throws RuntimeException {
        Spaceship spaceship = mission.getAssignedSpaceship();

        Map<Role, Short> crew = spaceship.getCrew();

        for (int i = 0; i < crew.get(Role.MISSION_SPECIALIST); i++) {
            Optional<CrewMember> crewMember = findCrewMemberByCriteria(new CrewMemberCriteria.Builder() {{
                role(Role.MISSION_SPECIALIST);
                isReadyForNextMissions(true);
            }}.build());
            ;

            if (crewMember.isPresent()) {
                CrewMember crewMember1 = crewMember.get();
                crewMember1.setReadyForNextMissions(false);
                mission.addCrew(crewMember1);
            }

        }
        for (int i = 0; i < crew.get(Role.FLIGHT_ENGINEER); i++) {
            Optional<CrewMember> crewMember = findCrewMemberByCriteria(new CrewMemberCriteria.Builder() {{
                role(Role.FLIGHT_ENGINEER);
                isReadyForNextMissions(true);
            }}.build());

            crewMember.ifPresent(member -> {
                member.setReadyForNextMissions(false);
                mission.addCrew(member);
            });
        }
        for (int i = 0; i < crew.get(Role.PILOT); i++) {
            Optional<CrewMember> crewMember = findCrewMemberByCriteria(new CrewMemberCriteria.Builder() {{
                role(Role.PILOT);
                isReadyForNextMissions(true);
            }}.build());

            crewMember.ifPresent(member -> {
                member.setReadyForNextMissions(false);
                mission.addCrew(member);
            });
        }
        for (int i = 0; i < crew.get(Role.COMMANDER); i++) {
            Optional<CrewMember> crewMember = findCrewMemberByCriteria(new CrewMemberCriteria.Builder() {{
                role(Role.COMMANDER);
                isReadyForNextMissions(true);
            }}.build());

            crewMember.ifPresent(member -> {
                member.setReadyForNextMissions(false);
                mission.addCrew(member);
            });
        }

    }

    @Override
    public CrewMember createCrewMember(Role role, String name, Rank rank) throws RuntimeException, EntityDuplicateException {
        CrewMember crewMember = CrewMemberFactory.getInstance().create(role, name, rank);
        Optional<CrewMember> crewMemberOptional = findCrewMemberByCriteria(new CrewMemberCriteria.Builder() {{
            name(name);
        }}.build());
        if (crewMemberOptional.isPresent()) {
            throw new EntityDuplicateException("Crew member with given name already exists");
        }
        Collection<CrewMember> crewMembers = NassaContext.getInstance().retrieveBaseEntityList(CrewMember.class);
        crewMembers.add(crewMember);
        return crewMember;
    }
}
