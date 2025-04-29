package eduPanel.to.request;

import eduPanel.util.LecturerType;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerReqTo implements Serializable {

   private String name;
   private String designation;
   private LecturerType type;
   private Integer displayOrder;
   private MultipartFile picture;
   private String linkedin;
}
