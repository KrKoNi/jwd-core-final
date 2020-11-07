package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.service.CrewService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
                (crewMember.getName().equals( crewMemberCriteria.getName()) || crewMemberCriteria.getName() == null)
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
                (crewMember.getName().equals( crewMemberCriteria.getName()) || crewMemberCriteria.getName() == null)
                        && (crewMember.getRank() == crewMemberCriteria.getRank() || crewMemberCriteria.getRank() == null)
                        && (crewMember.getReadyForNextMissions() == crewMemberCriteria.getReadyForNextMissions() || crewMemberCriteria.getReadyForNextMissions() == null)
                        && (crewMember.getRole() == crewMemberCriteria.getRole() || crewMemberCriteria.getRole() == null)
        )).findFirst();
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember crewMember) {
        return null;
    }

    @Override
    public void assignCrewMemberOnMission(CrewMember crewMember) throws RuntimeException {

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
