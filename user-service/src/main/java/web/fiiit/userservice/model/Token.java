package web.fiiit.userservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "token")
@Data
@Builder
@AllArgsConstructor
public class Token {

    @Id
    private Long id;

    @Column(name = "value", unique = true)
    private String value;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    public Token() {
    }
}
