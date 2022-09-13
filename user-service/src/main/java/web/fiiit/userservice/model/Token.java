package web.fiiit.userservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "token")
@Data
@Builder
@AllArgsConstructor
public class Token {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value", unique = true)
    private String value;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @Column(name = "expiration_time")
    private Date expirationTime;

    public Token() {
    }
}
