package eduPanel.to;

import eduPanel.util.LecturerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureTo implements Serializable {
    private Integer id;
    private String name;
    private String designation;
    private String qualification;
    private LecturerType type;
    private Integer displayOrder;
    private String picture;
    private String linkedin;

}
