package fr.iocean.framework.security.model.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.iocean.framework.core.resource.model.LongIdPersistentResource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"label"}, callSuper = true)
@ToString(of = {"label"}, callSuper = true)
@NoArgsConstructor
public class Profile extends LongIdPersistentResource {

    private static final long serialVersionUID = -4768460974607663983L;

    @Column(unique = true)
    @NotBlank
    private String label;
    
    private boolean active = true;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    @JsonIgnore
    private List<ProfileAccount> profileAccounts;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProfileCredential> profileCredentials;
}