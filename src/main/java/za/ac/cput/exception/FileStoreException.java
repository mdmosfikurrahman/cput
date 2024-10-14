package za.ac.cput.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileStoreException extends RuntimeException {
    private final String code = "FILE_STORE_500";
    private final String message = "{messages.exception.FileStoreException}";
}
