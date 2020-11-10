package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.FreeSpaceshipAbsentException;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.SpaceshipService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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
                (criteria1.getName() == null || spaceship.getName().equals(criteria1.getName()))
                        && (criteria1.getFlightDistance() == null || spaceship.getFlightDistance() >= criteria1.getFlightDistance())
                        && (criteria1.getReadyForNextMissions() == null || spaceship.getReadyForNextMissions() == criteria1.getReadyForNextMissions())
        )).collect(Collectors.toList());
    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        List<Spaceship> spaceships = findAllSpaceships();
        SpaceshipCriteria spaceshipCriteria = (SpaceshipCriteria) criteria;

        return spaceships.stream().filter(spaceship -> (
                (spaceshipCriteria.getName() == null || spaceship.getName().equals(spaceshipCriteria.getName()))
                        && (spaceshipCriteria.getFlightDistance() == null || spaceship.getFlightDistance() >= spaceshipCriteria.getFlightDistance())
                        && (spaceshipCriteria.getReadyForNextMissions() == null || spaceship.getReadyForNextMissions() == spaceshipCriteria.getReadyForNextMissions())
        )).findFirst();
    }

    @Override
    public Spaceship updateSpaceshipDetails(Spaceship oldSpaceship, Spaceship newSpaceship) {

        oldSpaceship.setFlightDistance(newSpaceship.getFlightDistance());
        oldSpaceship.setReadyForNextMissions(newSpaceship.getReadyForNextMissions());

        return oldSpaceship;
    }

    @Override
    public void assignSpaceshipOnMission(FlightMission mission, Spaceship spaceship) {
        spaceship.setReadyForNextMissions(false);
        mission.setAssignedSpaceship(spaceship);
    }

    @Override
    public void printAllSpaceships() {
        List<Spaceship> spaceships = findAllSpaceships();
        AtomicInteger i = new AtomicInteger();
        spaceships.stream()
                .map(spaceship -> (i.incrementAndGet()) + ". " + spaceship.toString())
                .forEachOrdered(System.out::println);
    }

    @Override
    public void printAllAvailableSpaceships() {
        List<Spaceship> availableSpaceships = findAllSpaceshipsByCriteria(
                new SpaceshipCriteria.Builder() {{
                    isReadyForNextMissions(true);
                }}.build()
        );
        AtomicInteger i = new AtomicInteger();
        availableSpaceships.stream()
                .map(spaceship -> (i.incrementAndGet()) + ". " + spaceship.toString())
                .forEachOrdered(System.out::println);
    }

    @Override
    public void assignRandomSpaceshipOnMission(FlightMission mission) throws RuntimeException, FreeSpaceshipAbsentException {
        Optional<Spaceship> spaceship = findSpaceshipByCriteria(new SpaceshipCriteria.Builder() {{
            flightDistance(mission.getDistance());
            isReadyForNextMissions(true);
        }}.build());

        if (spaceship.isEmpty()) {
            throw new FreeSpaceshipAbsentException("There is no spaceship which can complete mission");
        }

        assignSpaceshipOnMission(mission, spaceship.get());

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

    @Override
    public Spaceship createTemporarySpaceship(Long distance) throws RuntimeException {
        return SpaceshipFactory.getInstance().create("", distance, null);
    }
}
