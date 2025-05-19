package eduPanel.validation;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LecturerImageConstraintValidator implements ConstraintValidator<LecturerImage, MultipartFile> {

    private long maximumFileSize;

    @Override
    public void initialize(LecturerImage constraintAnnotation) {
        maximumFileSize = constraintAnnotation.maximumFileSize();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty()) return true;
        if (multipartFile.getContentType() == null || !multipartFile.getContentType().startsWith("image/")) return false;
        return multipartFile.getSize() <= maximumFileSize;
    }
}