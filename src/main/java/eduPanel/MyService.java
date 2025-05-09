package eduPanel;

import eduPanel.repository.custom.LecturerRepository;

import eduPanel.entity.Lecturer;
import eduPanel.repository.RepositoryFactory;
import eduPanel.repository.custom.LecturerRepository;
import eduPanel.repository.custom.impl.LecturerRepositoryImpl;
public class MyService {

    void saveLecturer(){
        LecturerRepository repository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.LECTURER);
    }
}