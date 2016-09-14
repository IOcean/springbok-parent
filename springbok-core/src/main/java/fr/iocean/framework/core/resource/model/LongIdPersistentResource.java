package fr.iocean.framework.core.resource.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Resource saved in a RDBMS and identified by a numeric id, equality is only based on id.
 */
@Getter
@Setter
@ToString(of = "id")
@MappedSuperclass
public abstract class LongIdPersistentResource implements PersistentResource<Long> {

    private static final long serialVersionUID = -5886577401324234159L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongIdPersistentResource that = (LongIdPersistentResource) o;
        return !(id == null && that.id == null) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return Objects.hash(id);
    }
}
