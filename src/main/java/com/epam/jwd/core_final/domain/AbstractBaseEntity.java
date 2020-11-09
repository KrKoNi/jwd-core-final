package com.epam.jwd.core_final.domain;

/**
 * Expected fields:
 * <p>
 * id {@link Long} - entity id
 * name {@link String} - entity name
 */
public abstract class AbstractBaseEntity implements BaseEntity {
    private final Long id;
    private final String name;
    private static Long idCounter = 0L;

    AbstractBaseEntity(final String name) {
        this.name = name;
        this.id = ++idCounter;
    }



    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

}
