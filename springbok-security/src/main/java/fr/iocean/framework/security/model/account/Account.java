package fr.iocean.framework.security.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.iocean.framework.core.resource.model.LongIdPersistentResource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.util.Date;

@Entity
@Setter
@Getter
@EqualsAndHashCode(of = {"email", "username"}, callSuper = false)
@ToString(of = {"email", "username", "enabled"}, callSuper = true)
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class Account extends LongIdPersistentResource {

    private static final long serialVersionUID = -1898848122717107177L;

    @Email
    @Column(unique = true)
    @NotBlank
    private String email;

    @Column(unique = true)
    @NotBlank
    @Length(min = 3, max = 30)
    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String token;

    @Column(name = "signedup_date")
    private Date signedUpDate;

    private boolean enabled = false;
}
