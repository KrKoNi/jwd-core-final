package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.epam.jwd.core_final.strategy.impl.InlineFileStrategy;
import com.epam.jwd.core_final.strategy.impl.MultilineFileStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

// todo
public class NassaContext implements ApplicationContext {
    private NassaContext() {}
    private final static NassaContext INSTANCE = new NassaContext();
    public static NassaContext getInstance() {
        return INSTANCE;
    }

    // no getters/setters for them
    private Collection<CrewMember> crewMembers = new ArrayList<>();
    private Collection<Spaceship> spaceships = new ArrayList<>();

    @Override
    public <T extends BaseEntity> Collection<T> retrieveBaseEntityList(Class<T> tClass) {
        if(tClass.toString().equals(CrewMember.class.toString())) {
            return (Collection<T>) crewMembers;
        }
        if(tClass.toString().equals(Spaceship.class.toString())) {
            return (Collection<T>) spaceships;
        }
        return null;
    }

    /**
     * You have to read input files, populate collections
     * @throws InvalidStateException
     */
    @Override
    public void init() throws InvalidStateException {
        InlineFileStrategy inlineFileStrategy = new InlineFileStrategy();
        inlineFileStrategy.read();

        MultilineFileStrategy multilineFileStrategy = new MultilineFileStrategy();
        multilineFileStrategy.read();

        Optional<CrewMember> crewMember = CrewServiceImpl.getInstance().findCrewMemberByCriteria(
                new CrewMemberCriteria.Builder() {{
                    name("Zoe Day");
                    id(0L);
                    role(Role.resolveRoleById(1L));
                    rank(Rank.resolveRankById(1L));
                    isReadyForNextMissions(true);
                }}.build()
        );

        Optional<Spaceship> spaceship = SpaceshipServiceImpl.getInstance().findSpaceshipByCriteria(
                new SpaceshipCriteria.Builder() {{
                    flightDistance(4870L);
                    isReadyForNextMissions(true);
                }}.build()
        );

        crewMember.ifPresent(member -> System.out.println("Found crewmate:\n" + "Name: " + member.getName()));
        spaceship.ifPresent(ship -> System.out.println("Found spaceship:\n" + "Name: " + ship.getName()));
        //throw new InvalidStateException();
    }
}
