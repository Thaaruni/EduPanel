package eduPanel.service.util;



import eduPanel.entity.Lecturer;
import eduPanel.entity.LinkedIn;
import eduPanel.entity.Picture;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo;
import org.modelmapper.ModelMapper;
import com.google.rpc.Help;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public class Transformer {

    private final ModelMapper mapper = new ModelMapper();

    public Transformer() {
        mapper.typeMap(LinkedIn.class, String.class)
                .setConverter(ctx -> ctx.getSource() != null ?  ctx.getSource().getUrl() : null);
        mapper.typeMap(MultipartFile.class, Picture.class)
                .setConverter(ctx -> null);
        mapper.typeMap(String.class, LinkedIn.class)
                .setConverter(ctx -> ctx.getSource() != null ? new LinkedIn(null, ctx.getSource()) : null);
    }

    public Lecturer fromLecturerReqTo(LecturerReqTo lecturerReqTo){
        Lecturer lecturer = mapper.map(lecturerReqTo, Lecturer.class);
        if (lecturerReqTo.getLinkedin() != null) lecturer.getLinkedIn().setLecturer(lecturer);

        return lecturer;
    }

    public Lecturer fromLecturerTo(LectureTo lectureTo){

        Lecturer lecturer = mapper.map(lectureTo, Lecturer.class);
        if (lectureTo.getLinkedin() != null) lecturer.getLinkedIn().setLecturer(lecturer);
        return lecturer;
    }

    public LectureTo toLecturerTo(Lecturer lecturer){
        return mapper.map(lecturer, LectureTo.class);
    }

    public List<LectureTo> toLecturerTOList(List<Lecturer> lecturerList){
        return lecturerList.stream().map(this::toLecturerTo).collect(Collectors.toList());
    }

}