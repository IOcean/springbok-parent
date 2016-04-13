package fr.iocean.framework.security.model.profile;

import fr.iocean.framework.core.resource.model.LongIdPersistentResource;
import fr.iocean.framework.security.model.credential.Credential;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString(of = {"profile", "credential"}, callSuper = true)
@NoArgsConstructor
@Table(name = "profile_credential",
        uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "credential_id"}))
public class ProfileCredential
        extends LongIdPersistentResource {

    private static final long serialVersionUID = 956187325117395404L;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    Profile profile;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    Credential credential;
}
