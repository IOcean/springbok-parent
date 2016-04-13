package fr.iocean.framework.security.model.profile;

import fr.iocean.framework.core.resource.model.LongIdPersistentResource;
import fr.iocean.framework.security.model.account.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"account", "profile"}, callSuper = true)
public class ProfileAccount extends LongIdPersistentResource {

    private static final long serialVersionUID = 956187325117395404L;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    Profile profile;
}
