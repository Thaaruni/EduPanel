package eduPanel.to;

import eduPanel.to.request.LecturerReqTo;
import eduPanel.util.LecturerType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureTo implements Serializable {
    @Null(message = "Id should be empty ")
    private Integer id;
    @NotBlank(message = "Name camt be empty")
    @Pattern(regexp = "^[A-Za-z]{2,}$" , message ="Invalid name")
    private String name;
    @NotBlank(message = "Designation cant be empty")
    @Length(min = 3 , message ="Invalid designation")
    private String designation;
    @NotBlank(message = "Qualification cant be empty")
    @Length(min = 2 , message ="Invalid qualification")
    private String qualification;
    @NotNull(message = "Type should be full time or visiting")
    private LecturerType type;
    @NotNull( message = "Display order cant be empty")
    @PositiveOrZero( message = "Display order cant be empty")
    private Integer displayOrder;
    @Null(message = "Picture should be empty")
    private String picture;
    @Pattern(regexp = "^http(s)://.+$", message ="Invalid linkedin URL")
    private String linkedin;

}
