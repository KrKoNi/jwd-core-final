package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.InvalidStateException;
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
    private Collection<Spaceship> missions = new ArrayList<>();

    @Override
    public <T extends BaseEntity> Collection<T> retrieveBaseEntityList(Class<T> tClass) {
        if(tClass.toString().equals(CrewMember.class.toString())) {
            return (Collection<T>) crewMembers;
        }
        if(tClass.toString().equals(Spaceship.class.toString())) {
            return (Collection<T>) spaceships;
        }
        if(tClass.toString().equals(FlightMission.class.toString())) {
            return (Collection<T>) missions;
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

        //throw new InvalidStateException();
    }
}
