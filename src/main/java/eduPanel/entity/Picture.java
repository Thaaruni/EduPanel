package eduPanel.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "picture")
public class Picture implements SuperEntity {
    @Id
    @OneToOne
    @JoinColumn(name="lecture_id" , referencedColumnName = "id")
    private Lecturer lecturer;
    @Column(name = "picture_path", nullable = false, length = 400 )
    private String picturePath;

}
