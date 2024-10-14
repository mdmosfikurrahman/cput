package za.ac.cput.service;

import java.util.List;

public interface BaseCrudService<E, K> {

    List<E> getAll();

    E getById(K id);

    void delete(K id);

    E create(E entity);

    E update(E entity);
}
