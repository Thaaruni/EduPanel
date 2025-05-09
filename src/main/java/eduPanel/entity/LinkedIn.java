package eduPanel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "linkedin")
public class LinkedIn implements SuperEntity {
    @Id
    @OneToOne
    @JoinColumn(name="lecture_id" , referencedColumnName = "id")
    private Lecturer lecturer;
    @Column(nullable = false, length = 2000 )
    private String url;

}
