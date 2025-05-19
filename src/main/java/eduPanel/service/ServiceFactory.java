package eduPanel.service;



import eduPanel.service.custom.impl.LecturerServiceImpl;


public class ServiceFactory {

    private static ServiceFactory INSTANCE;

    public enum ServiceType{
        LECTURER, USER
    }

    private ServiceFactory(){}

    public static ServiceFactory getInstance() {
        return (INSTANCE == null)? (INSTANCE = new ServiceFactory()): INSTANCE;
    }

    public <T extends SuperService> T getService(ServiceType type){
        switch (type){
            case LECTURER:
                return (T) new LecturerServiceImpl();
            case USER:
                throw new RuntimeException("Not implemented yet");
            default:
                throw new IllegalArgumentException();
        }
    }
}