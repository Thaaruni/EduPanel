package eduPanel.service.custom;


import eduPanel.service.SuperService;
import eduPanel.to.LectureTo;
import eduPanel.to.request.LecturerReqTo;
import eduPanel.util.LecturerType;

import java.util.List;


public interface LecturerService extends SuperService {

  LectureTo saveLecturer(LecturerReqTo lecturerReqTO);

    void updateLecturerDetails(LecturerReqTo lecturerReqTO);
//
    void updateLecturerDetails(LectureTo lecturerTO);
//
   void deleteLecturer(Integer lecturerId);
//
    LectureTo getLecturerDetails(Integer lecturerId);
//
    List<LectureTo> getLecturers(LecturerType type);
}
