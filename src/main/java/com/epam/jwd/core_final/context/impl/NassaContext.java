package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.strategy.impl.CrewFile;
import com.epam.jwd.core_final.strategy.impl.SpaceshipFile;
import com.epam.jwd.core_final.util.PropertyReaderUtil;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collection;

public class NassaContext implements ApplicationContext {
    private NassaContext() {}
    private final static NassaContext INSTANCE = new NassaContext();
    public static NassaContext getInstance() {
        return INSTANCE;
    }

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
        String separator = FileSystems.getDefault().getSeparator();
        String crewPath = "src" + separator + "main" + separator + "resources" + separator
                + NassaContext.getInstance().getApplicationProperties().getInputRootDir()
                + separator
                + NassaContext.getInstance().getApplicationProperties().getCrewFileName();
        String spaceshipPath = "src" + separator + "main" + separator + "resources" + separator
                + NassaContext.getInstance().getApplicationProperties().getInputRootDir()
                + separator
                + NassaContext.getInstance().getApplicationProperties().getSpaceshipsFileName();
        try {
            new CrewFile(crewPath).read();
            new SpaceshipFile(spaceshipPath).read();
        } catch (IOException e) {
            System.out.println("File not found");
            throw new InvalidStateException();
        }
    }
}
