package za.ac.cput.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.repository.BaseCrudRepository;

import java.util.List;
public class BaseCrudServiceImpl<E , K> {

    protected final BaseCrudRepository<E, K> repository;

    public BaseCrudServiceImpl(BaseCrudRepository<E, K> repository) {
        this.repository = repository;
    }

    public List<E> getAll() {
        return repository.findAll();
    }

    public E getById(K id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void delete(K id) {
        repository.deleteById(id);
    }

    @Transactional
    public E create(E entity){
        return repository.save(entity);
    }

    @Transactional
    public E update(E entity) {
        return repository.save(entity);
    }
}
