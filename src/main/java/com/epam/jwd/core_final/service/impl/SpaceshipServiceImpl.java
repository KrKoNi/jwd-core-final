package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.SpaceshipService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
        SpaceshipCriteria criteria1 = (SpaceshipCriteria) criteria;

        return spaceships.stream().filter(spaceship -> (
                (spaceship.getName().equals( criteria1.getName()) || criteria1.getName() == null)
                        && (spaceship.getFlightDistance() >= criteria1.getFlightDistance() || criteria1.getFlightDistance() == null)
                        && (spaceship.getReadyForNextMissions() == criteria1.getReadyForNextMissions() || criteria1.getReadyForNextMissions() == null)
        )).collect(Collectors.toList());
    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        List<Spaceship> spaceships = findAllSpaceships();
        SpaceshipCriteria criteria1 = (SpaceshipCriteria) criteria;

        return spaceships.stream().filter(spaceship -> (
                (spaceship.getName().equals( criteria1.getName()) || criteria1.getName() == null)
                        && (criteria1.getFlightDistance() == null || spaceship.getFlightDistance() >= criteria1.getFlightDistance())
                        && ( criteria1.getReadyForNextMissions() == null || spaceship.getReadyForNextMissions() == criteria1.getReadyForNextMissions())
        )).findFirst();
    }

    @Override
    public Spaceship updateSpaceshipDetails(Spaceship oldSpaceship, Spaceship newSpaceship) {

        oldSpaceship.setFlightDistance(newSpaceship.getFlightDistance());
        oldSpaceship.setReadyForNextMissions(newSpaceship.getReadyForNextMissions());

        return oldSpaceship;
    }

    @Override
    public void assignSpaceshipOnMission(Spaceship spaceship, FlightMission mission) throws RuntimeException {
        Collection<Spaceship> spaceships = findAllSpaceships();
    }

    @Override
    public Spaceship createSpaceship(String name, Long distance, Map<Role, Short> crew) throws RuntimeException, EntityDuplicateException {
        Spaceship spaceship = SpaceshipFactory.getInstance().create(name, distance, crew);
        Optional<Spaceship> spaceshipOptional = findSpaceshipByCriteria(new SpaceshipCriteria.Builder() {{
            name(name);
        }}.build());

        if (spaceshipOptional.isPresent()) {
            throw new EntityDuplicateException("Spaceship with given name already exists");
        }
        Collection<Spaceship> spaceships = findAllSpaceships();
        spaceships.add(spaceship);
        return spaceship;
    }
}
