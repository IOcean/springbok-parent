package fr.iocean.framework.security.model.credential;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.iocean.framework.core.resource.model.LongIdPersistentResource;
import fr.iocean.framework.security.model.profile.ProfileCredential;
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
public class Credential extends LongIdPersistentResource {

    private static final long serialVersionUID = -4768460974607663983L;

    @Column(unique = true)
    @NotBlank
    private String label;

    @NotBlank
    private String application; 
            
    @OneToMany(mappedBy = "credential", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    List<ProfileCredential> profileCredentials;
}