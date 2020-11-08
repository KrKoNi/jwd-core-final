package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;

import java.util.Objects;

/**
 * Should be a builder for {@link BaseEntity} fields
 */
public abstract class Criteria<T extends BaseEntity> {
    private final String name;
    private final Long id;

    Criteria(Criteria.Builder builder) {
        name = builder.name;
        id = builder.id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Criteria<?> criteria = (Criteria<?>) o;
        return Objects.equals(name, criteria.name) &&
                Objects.equals(id, criteria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    public static class Builder<T extends Builder<T>> {
        private String name = null;
        private Long id = null;

        public Builder() {

        }

        protected T name(String name) {
            this.name = name;
            return (T) this;
        }

        protected T id(Long id) {
            this.id = id;
            return (T) this;
        }
    }
}
