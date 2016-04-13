package fr.iocean.framework.core.resource.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Resource saved in a RDBMS and identified by a numeric id, equality is only based on id.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
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
}
