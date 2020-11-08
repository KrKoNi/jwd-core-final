package com.epam.jwd.core_final.service;

import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.exception.FreeSpaceshipAbsentException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * All its implementations should be a singleton
 * You have to use streamAPI for filtering, mapping, collecting, iterating
 */
public interface SpaceshipService {

    List<Spaceship> findAllSpaceships();

    List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria);

    Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria);

    Spaceship updateSpaceshipDetails(Spaceship oldSpaceship, Spaceship newSpaceship);

    // todo create custom exception for case, when spaceship is not able to be assigned
    void assignSpaceshipOnMission(FlightMission mission, Spaceship spaceship) throws RuntimeException, FreeSpaceshipAbsentException;

    // todo create custom exception for case, when crewMember is not able to be created (for example - duplicate.

    Spaceship createSpaceship(String name, Long distance, Map<Role, Short> crew) throws RuntimeException, EntityDuplicateException;
}
