package fr.iocean.framework.core.resource.mock;

import fr.iocean.framework.core.resource.model.LongIdPersistentResource;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = {"name"}, callSuper = false)
@ToString(of = {"name"}, callSuper = true)
public class User extends LongIdPersistentResource implements Comparable<User> {
    
    private static final long serialVersionUID = 6434352024112491080L;
    
    @NotBlank
    private String name;

    public User() {
    }
    
    public User withId(final Long id) {
        this.id = id;
        return this;
    }
    
    public User withName(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public int compareTo(User other) {
        return this.getName().compareTo(other.getName());
    }
}
