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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public CrewMember updateCrewMemberDetails(CrewMember oldCrewMember, CrewMember updatedCrewMember) {

        //  oldCrewMember.setReadyForNextMissions(updatedCrewMember.getReadyForNextMissions());
        oldCrewMember.setRank(updatedCrewMember.getRank());
        oldCrewMember.setRole(updatedCrewMember.getRole());

        return oldCrewMember;
    }

    @Override
    public void printAllCrewMembers() {
        List<CrewMember> crewMembers = findAllCrewMembers();
        AtomicInteger i = new AtomicInteger();
        crewMembers.stream()
                .map(crewMember -> (i.incrementAndGet()) + ". " + crewMember.toString())
                .forEachOrdered(System.out::println);
    }

    @Override
    public void assignRandomCrewMembersOnMission(FlightMission mission) throws RuntimeException {
        Spaceship spaceship = mission.getAssignedSpaceship();

        Map<Role, Short> crew = spaceship.getCrew();
        Arrays.stream(Role.values())
                .forEachOrdered(value ->
                        IntStream.range(0, crew.get(value))
                                .mapToObj(i1 -> findCrewMemberByCriteria(
                                        new CrewMemberCriteria.Builder() {{
                                            role(value);
                                            isReadyForNextMissions(true);
                                        }}.build()))
                                .forEachOrdered(crewMemberByCriteria -> crewMemberByCriteria.ifPresent(member -> {
                                    member.setReadyForNextMissions(false);
                                    mission.addCrew(member);
                                }))
                );

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

    @Override
    public Boolean isAvailable(LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public CrewMember createTemporaryCrewMember(Role role, Rank rank) throws RuntimeException {
        return CrewMemberFactory.getInstance().create(role, "", rank);
    }

}
