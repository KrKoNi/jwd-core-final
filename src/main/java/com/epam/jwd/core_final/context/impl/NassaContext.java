package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.strategy.impl.CrewFile;
import com.epam.jwd.core_final.strategy.impl.InlineFileStrategy;
import com.epam.jwd.core_final.strategy.impl.MultilineFileStrategy;
import com.epam.jwd.core_final.strategy.impl.SpaceshipFile;
import com.epam.jwd.core_final.util.PropertyReaderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class NassaContext implements ApplicationContext {
    private NassaContext() {}
    private final static NassaContext INSTANCE = new NassaContext();
    public static NassaContext getInstance() {
        return INSTANCE;
    }

    // no getters/setters for them
    private final Collection<CrewMember> crewMembers = new ArrayList<>();
    private final Collection<Spaceship> spaceships = new ArrayList<>();
    private final Collection<Spaceship> missions = new ArrayList<>();

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

    private ApplicationProperties applicationProperties;

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    @Override
    public void init() throws InvalidStateException {
        applicationProperties = PropertyReaderUtil.loadProperties();

        String crewPath = "src/main/resources/"
                + NassaContext.getInstance().getApplicationProperties().getInputRootDir()
                + "/"
                + NassaContext.getInstance().getApplicationProperties().getCrewFileName();
        String spaceshipPath = "src/main/resources/"
                + NassaContext.getInstance().getApplicationProperties().getInputRootDir()
                + "/"
                + NassaContext.getInstance().getApplicationProperties().getSpaceshipsFileName();
        try {
            new CrewFile(crewPath).read();
            new SpaceshipFile(spaceshipPath).read();
        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println(e);
        }

        //throw new InvalidStateException();
    }
}
