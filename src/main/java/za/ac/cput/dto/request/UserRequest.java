package za.ac.cput.dto.request;

public class UserRequest {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String nic;

    public UserRequest() {
    }

    public UserRequest(String name, String email, String password, String phone, String nic) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }
}
