package za.ac.cput.service;

public interface AuthenticationService {
    Object authenticate(String email, String password, String role);
}
