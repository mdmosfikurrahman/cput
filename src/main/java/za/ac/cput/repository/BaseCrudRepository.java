package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseCrudRepository<E, K> extends JpaRepository<E, K> , QuerydslPredicateExecutor<E>  {
}
