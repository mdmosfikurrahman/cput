package za.ac.cput.service.impl;

import com.querydsl.core.BooleanBuilder;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.dto.request.ScooterRequest;
import za.ac.cput.domain.Attachment;
import za.ac.cput.domain.QScooter;
import za.ac.cput.domain.Scooter;
import za.ac.cput.enums.Model;
import za.ac.cput.enums.ScooterStatus;
import za.ac.cput.repository.BaseCrudRepository;
import za.ac.cput.repository.ScooterRepository;
import za.ac.cput.service.FileStoreService;
import za.ac.cput.service.ScooterService;

import java.util.List;
import java.util.UUID;

@Service
public class ScooterServiceImpl extends BaseCrudServiceImpl<Scooter, Long> implements ScooterService {
    private final ScooterRepository scooterRepository;
    private final FileStoreService fileStoreService;

    public ScooterServiceImpl(BaseCrudRepository<Scooter, Long> repository, ScooterRepository scooterRepository, FileStoreService fileStoreService) {
        super(repository);
        this.scooterRepository = scooterRepository;
        this.fileStoreService = fileStoreService;
    }

    private Scooter setupScooter(ScooterRequest form, MultipartFile file, Scooter scooter) {
        BeanUtils.copyProperties(form, scooter);

        var ext = FilenameUtils.getExtension(file.getOriginalFilename());
        var relativePath = "scooter-%s.%s".formatted(UUID.randomUUID().toString(), ext);
        Attachment attachment = fileStoreService.storeFile(relativePath, file);
        scooter.setImage(attachment);

        return scooter;
    }

    @Override
    public Scooter create(ScooterRequest form, MultipartFile file) {
        Scooter scooter = new Scooter();
        scooter.setStatus(ScooterStatus.AVAILABLE);
        return scooterRepository.save(setupScooter(form, file, scooter));
    }

    @Override
    public Scooter update(Long id, ScooterRequest form, MultipartFile file) {
        Scooter scooter = getById(id);

        String oldPath = (scooter.getImage() != null) ? scooter.getImage().getPath() : null;
        setupScooter(form, file, scooter);
        if (oldPath != null) {
            fileStoreService.deleteFile(oldPath);
        }

        return scooterRepository.save(scooter);
    }

    @Override
    public Scooter update(Long id, ScooterRequest form) {
        Scooter scooter = getById(id);
        BeanUtils.copyProperties(form, scooter);

        return scooterRepository.save(scooter);
    }

    @Override
    public List<Scooter> search(String q) {
        if (q != null && !q.isBlank()) {
            BooleanBuilder predicate = new BooleanBuilder();

            for (ScooterStatus status : ScooterStatus.values()) {
                if (status.name().toLowerCase().contains(q.toLowerCase())) {
                    predicate.or(QScooter.scooter.status.eq(status));
                }
            }

            for (Model model : Model.values()) {
                if (model.name().toLowerCase().contains(q.toLowerCase())) {
                    predicate.or(QScooter.scooter.model.eq(model));
                }
            }
            predicate.or(QScooter.scooter.model.stringValue().toLowerCase().containsIgnoreCase(q.toLowerCase()));

            if (q.matches("\\d+")) {
                predicate.or(QScooter.scooter.id.eq(Long.valueOf(q))
                        .or(QScooter.scooter.rentalPerDay.eq(Integer.valueOf(q))));
            }

            return (List<Scooter>) scooterRepository.findAll(predicate);
        }

        return scooterRepository.findAll();
    }

    @PostConstruct
    private void initializeScooter() {
        if (scooterRepository.count() == 0) {
            createInitialScooter(Model.HONDA, 2020, 50);
        }
        if (scooterRepository.count() < 2) {
            createInitialScooter(Model.HONDA, 2022, 40);
        }
        if (scooterRepository.count() < 3) {
            createInitialScooter(Model.YAMAHA, 2019, 30);
        }
    }

    private void createInitialScooter(Model model, int year, int rentalPerDay) {
        Scooter scooter = new Scooter();
        scooter.setModel(model);
        scooter.setYear(year);
        scooter.setRentalPerDay(rentalPerDay);
        scooter.setStatus(ScooterStatus.AVAILABLE);

        Attachment attachment = new Attachment();
        attachment.setPath("/src/main/resources/static");
        attachment.setUrl("/Scooter-1.jpg");
        attachment.setBytes(1024L);
        attachment.setContentType("image/jpeg");

        scooter.setImage(attachment);

        scooterRepository.save(scooter);
        System.out.printf("Initial scooter with model %s and year %d has been created.%n", model, year);
    }

    @Override
    public List<Scooter> searchByModelOrYear(String model, Integer year) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (model != null && !model.isBlank()) {
            predicate.or(QScooter.scooter.model.stringValue().equalsIgnoreCase(model));
        }

        if (year != null) {
            predicate.or(QScooter.scooter.year.eq(year));
        }

        return (List<Scooter>) scooterRepository.findAll(predicate);
    }

    @Override
    public List<Scooter> getAvailableScooters() {
        return scooterRepository.findAll();
    }

}
