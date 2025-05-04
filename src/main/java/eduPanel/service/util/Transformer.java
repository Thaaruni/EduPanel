package eduPanel.service.util;

import eduPanel.entity.Lecturer;
import eduPanel.entity.LinkedIn;
import eduPanel.entity.Picture;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Transformer {

    private final ModelMapper mapper;

    public Transformer(ModelMapper mapper) {
        this.mapper = mapper;
//        mapper.typeMap(Lecturer.class, LecturerTO.class)
//                .addMapping(lecturer -> lecturer.getLinkedIn().getUrl(), LecturerTO::setLinkedin);
//        mapper.typeMap(LecturerTO.class, Lecturer.class)
//                .addMapping(LecturerTO::getLinkedin, (lecturer, o) ->
//                        lecturer.getLinkedIn().setUrl((String) o));

        mapper.typeMap(LinkedIn.class, String.class)
                .setConverter(ctx -> ctx.getSource() != null ?  ctx.getSource().getUrl() : null);
        mapper.typeMap(MultipartFile.class, Picture.class)
                .setConverter(ctx -> null);
        mapper.typeMap(String.class, LinkedIn.class)
                .setConverter(ctx -> ctx.getSource() != null ? new LinkedIn(null, ctx.getSource()) : null);
    }

    public Lecturer fromLecturerReqTO(LecturerReqTo lecturerReqTO){
        Lecturer lecturer = mapper.map(lecturerReqTO, Lecturer.class);
        if (lecturerReqTO.getLinkedin() != null) lecturer.getLinkedIn().setLecturer(lecturer);
        return lecturer;
    }

    public Lecturer fromLecturerTO(LectureTo lecturerTO){
        Lecturer lecturer = mapper.map(lecturerTO, Lecturer.class);
        if (lecturerTO.getLinkedin() != null) lecturer.getLinkedIn().setLecturer(lecturer);
        return lecturer;
    }

    public LectureTo toLecturerTO(Lecturer lecturer){
        return mapper.map(lecturer, LectureTo.class);
    }

    public List<LectureTo> toLectureTOList(List<Lecturer> lecturerList){
        return lecturerList.stream().map(this::toLecturerTO).collect(Collectors.toList());
    }

}