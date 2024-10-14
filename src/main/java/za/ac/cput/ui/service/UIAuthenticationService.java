package za.ac.cput.ui.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import za.ac.cput.dto.request.LoginRequest;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.User;

public class UIAuthenticationService {
    private final String BASE_URL = "http://localhost:8080/api/auth";
    private final RestTemplate restTemplate;

    public UIAuthenticationService() {
        this.restTemplate = new RestTemplate();
    }

    public Object login(String email, String password, String role) {
        LoginRequest request = new LoginRequest(email, password, role);
        try {
            if ("CUSTOMER".equalsIgnoreCase(role)) {
                ResponseEntity<Customer> response = restTemplate.postForEntity(BASE_URL + "/login", request, Customer.class);
                return response.getBody();
            } else {
                ResponseEntity<User> response = restTemplate.postForEntity(BASE_URL + "/login", request, User.class);
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ObjectMapper createConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private RestTemplate createConfiguredRestTemplate() {
        ObjectMapper mapper = createConfiguredMapper();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, converter);
        return restTemplate;
    }
}