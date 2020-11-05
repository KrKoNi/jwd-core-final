package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.SpaceshipService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpaceshipServiceImpl implements SpaceshipService {

    private SpaceshipServiceImpl() {

    }

    private final static SpaceshipServiceImpl INSTANCE = new SpaceshipServiceImpl();

    public static SpaceshipServiceImpl getInstance() {
        return INSTANCE;
    }


    @Override
    public List<Spaceship> findAllSpaceships() {
        return null;
    }

    @Override
    public List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria) {
        return null;
    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        return Optional.empty();
    }

    @Override
    public Spaceship updateSpaceshipDetails(Spaceship spaceship) {
        return null;
    }

    @Override
    public void assignSpaceshipOnMission(Spaceship crewMember) throws RuntimeException {

    }

    @Override
    public Spaceship createSpaceship(String name, Long distance, Map<Role, Short> crew) throws RuntimeException {
        Spaceship spaceship = SpaceshipFactory.getInstance().create(name, distance, crew);
        Collection<Spaceship> spaceships = NassaContext.getInstance().retrieveBaseEntityList(Spaceship.class);
        spaceships.add(spaceship);
        return spaceship;

    }
}
