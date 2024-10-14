package za.ac.cput.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.dto.request.ScooterRequest;
import za.ac.cput.domain.Scooter;
import za.ac.cput.service.ScooterService;

import java.util.List;

@RestController
@RequestMapping("/scooters")
public class ScooterController {

    @Autowired
    private ScooterService scooterService;

    @GetMapping("")
    public List<Scooter> list() {
        return scooterService.getAll();
    }

    @GetMapping("/{id}")
    public Scooter getById(@PathVariable Long id) {
        return scooterService.getById(id);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Scooter create(@RequestPart("form") String formJson, @RequestPart("file") MultipartFile file) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ScooterRequest form = mapper.readValue(formJson, ScooterRequest.class);
        return scooterService.create(form, file);
    }

    @PutMapping("/{id}")
    public Scooter update(@PathVariable Long id, @RequestBody ScooterRequest form, @RequestPart(name = "file") MultipartFile file) {
        return scooterService.update(id, form, file);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        scooterService.delete(id);
    }

    @GetMapping("/search")
    public List<Scooter> search(@RequestParam(required = false) String q) {
        return scooterService.search(q);
    }
}
