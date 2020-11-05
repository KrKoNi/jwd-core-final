package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
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
        crewMembers.forEach(crew -> {
            System.out.println("Rank: " + crew.getRank());
            System.out.println("Name: " + crew.getName());
            System.out.println("Role: " + crew.getRole());
            System.out.println();
        });
//        MultilineFileStrategy multilineFileStrategy = new MultilineFileStrategy();
//        multilineFileStrategy.read();
//        spaceships.forEach(spaceship -> {
//            System.out.println("Name: " + spaceship.getName());
//            System.out.println("Distance: " + spaceship.getFlightDistance());
//            //System.out.println("Map: " + spaceship.);
//            System.out.println();
//        });
        Optional<CrewMember> crewMember = CrewServiceImpl.getInstance().findCrewMemberByCriteria(
                new CrewMemberCriteria.Builder() {{
                    name("Zoe Day");
                    id(0L);
                    role(Role.resolveRoleById(1L));
                    rank(Rank.resolveRankById(1L));
                    isReadyForNextMissions(true);
                }}.build()
        );

        Collection<CrewMember> crewMemberCollection = CrewServiceImpl.getInstance().findAllCrewMembersByCriteria(
                new CrewMemberCriteria.Builder() {{
                    //name("Zoe Day");
                    //id(0L);
                    role(Role.resolveRoleById(1L));
                   // rank(Rank.resolveRankById(1L));
                    //isReadyForNextMissions(true);
                }}.build()
        );

        crewMemberCollection.stream().map(AbstractBaseEntity::getName).forEach(System.out::println);

        crewMember.ifPresent(member -> System.out.println("Found:\n" + "Name: " + member.getName()));
        //throw new InvalidStateException();
    }
}
