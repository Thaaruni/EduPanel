package eduPanel.to.request;

import eduPanel.util.LecturerType;
import eduPanel.validation.LecturerImage;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;


import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerReqTo implements Serializable {
   private Integer id;
   @NotBlank(message = "Name camt be empty")
   @Pattern(regexp = "^[A-Za-z]{2,}$" , message ="Invalid name")
   private String name;
   @NotBlank(message = "Designation cant be empty")
   @Length(min = 3 , message ="Invalid designation")
   private String designation;
   @NotBlank(message = "Qualification cant be empty")
   @Length(min = 3 , message ="Invalid qualification")
   private String qualification;
   @NotNull(message = "Type should be full time or visiting")
   private LecturerType type;

   @Null(groups = Create.class , message = "Display order cant be empty")
   @NotNull(groups = Update.class , message = "Display order cant be empty")
   @PositiveOrZero(groups = Update.class , message = "Display order cant be empty")
   private Integer displayOrder;
   @LecturerImage
   private MultipartFile picture;
   @Pattern(regexp = "^https?://.+$", message = "Invalid linkedIn url")
   private String linkedin;

   public interface Create extends Default {}
   public interface Update extends Default {}

   public LecturerReqTo(String name, String designation, String qualification, LecturerType type, Integer displayOrder, MultipartFile picture, String linkedin) {
      this.name = name;
      this.designation = designation;
      this.qualification = qualification;
      this.type = type;
      this.displayOrder = displayOrder;
      this.picture = picture;
      this.linkedin = linkedin;
   }
}
