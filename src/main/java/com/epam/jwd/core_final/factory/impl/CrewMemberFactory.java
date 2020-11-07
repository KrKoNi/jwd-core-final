package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.factory.EntityFactory;

public class CrewMemberFactory implements EntityFactory<CrewMember> {

    private CrewMemberFactory() {

    }

    private final static CrewMemberFactory INSTANCE = new CrewMemberFactory();

    public static CrewMemberFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public CrewMember create(Object... args) {
        return new CrewMember((Role)args[0], (String) args[1], (Rank)args[2]);
    }

}
