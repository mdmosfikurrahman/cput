package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Attachment {

    @JsonIgnore
    @NotBlank
    @Size(max = 400)
    private String path;
    @Size(max = 400)
    private String url;
    @NotNull
    private Long bytes;
    @NotBlank
    private String contentType;

    public @NotBlank @Size(max = 400) String getPath() {
        return path;
    }

    public void setPath(@NotBlank @Size(max = 400) String path) {
        this.path = path;
    }

    public @Size(max = 400) String getUrl() {
        return url;
    }

    public void setUrl(@Size(max = 400) String url) {
        this.url = url;
    }

    public @NotNull Long getBytes() {
        return bytes;
    }

    public void setBytes(@NotNull Long bytes) {
        this.bytes = bytes;
    }

    public @NotBlank String getContentType() {
        return contentType;
    }

    public void setContentType(@NotBlank String contentType) {
        this.contentType = contentType;
    }

    public Attachment() {
    }

    public Attachment(String path, String url, Long bytes, String contentType) {
        this.path = path;
        this.url = url;
        this.bytes = bytes;
        this.contentType = contentType;
    }
}
