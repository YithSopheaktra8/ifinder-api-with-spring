package co.istad.ifinder.domain;

import co.istad.ifinder.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tokens")
@Setter
@Getter
public class Token extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 2040)
    private String resetToken;

    @OneToOne
    private User user;

    private String verifyCode;

}
