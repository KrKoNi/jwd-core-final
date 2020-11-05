package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
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
        return crewMembers.stream()
                .filter(crewMember -> Objects
                        .equals(criteria,
                            new CrewMemberCriteria.Builder() {{
                                    name(crewMember.getName()); // из User.Builder
                                    id(crewMember.getId()); // из RussianUser.Builder
                                    rank(crewMember.getRank()); // из User.Builder
                                    role(crewMember.getRole());
                                    isReadyForNextMissions(crewMember.getReadyForNextMissions());
                                }}.build()
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(Criteria<? extends CrewMember> criteria) {
        List<CrewMember> crewMembers = findAllCrewMembers();

        return crewMembers.stream()
                .filter(crewMember -> Objects
                        .equals(criteria,
                                new CrewMemberCriteria.Builder() {{
                                    name(crewMember.getName());
                                    id(crewMember.getId());
                                    rank(crewMember.getRank());
                                    role(crewMember.getRole());
                                    isReadyForNextMissions(crewMember.getReadyForNextMissions());
                                }}.build()
                        )
                )
                .findFirst();
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember crewMember) {
        return null;
    }

    @Override
    public void assignCrewMemberOnMission(CrewMember crewMember) throws RuntimeException {

    }

    @Override
    public CrewMember createCrewMember(Role role, String name, Rank rank) throws RuntimeException {
        CrewMember crewMember = CrewMemberFactory.getInstance().create(role, name, rank);
        Collection<CrewMember> crewMembers = NassaContext.getInstance().retrieveBaseEntityList(CrewMember.class);
        crewMembers.add(crewMember);
        return crewMember;
    }
}
