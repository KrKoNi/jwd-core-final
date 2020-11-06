package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.SpaceshipService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpaceshipServiceImpl implements SpaceshipService {

    private SpaceshipServiceImpl() {

    }

    private final static SpaceshipServiceImpl INSTANCE = new SpaceshipServiceImpl();

    public static SpaceshipServiceImpl getInstance() {
        return INSTANCE;
    }


    @Override
    public List<Spaceship> findAllSpaceships() {
        return (List<Spaceship>) NassaContext.getInstance().retrieveBaseEntityList(Spaceship.class);
    }

    @Override
    public List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria) {
        List<Spaceship> spaceships = findAllSpaceships();
        return spaceships.stream()
                .filter(spaceship -> Objects
                        .equals(criteria,
                                new SpaceshipCriteria.Builder() {{
                                    name(spaceship.getName());
                                    id(spaceship.getId());
                                    flightDistance(spaceship.getFlightDistance());
                                    crew(spaceship.getCrew());
                                    isReadyForNextMissions(spaceship.getReadyForNextMissions());
                                }}.build()
                        )
                )
                .collect(Collectors.toList());

    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        List<Spaceship> spaceships = findAllSpaceships();
        return spaceships.stream()
                .filter(spaceship -> Objects
                        .equals(criteria,
                                new SpaceshipCriteria.Builder() {{
                                    name(spaceship.getName());
                                    id(spaceship.getId());
                                    flightDistance(spaceship.getFlightDistance());
                                    crew(spaceship.getCrew());
                                    isReadyForNextMissions(spaceship.getReadyForNextMissions());
                                }}.build()
                        )
                )
                .findFirst();
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
