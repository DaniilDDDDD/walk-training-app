package web.fiiit.userservice.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "\"medical_card\"")
public class MedicalCard {

    @Id
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner", nullable = false)
    @JsonBackReference
    private User owner;

    public MedicalCard() {
    }
}
