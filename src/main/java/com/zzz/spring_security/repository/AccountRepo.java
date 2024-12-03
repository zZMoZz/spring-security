package com.zzz.spring_security.repository;

import com.zzz.spring_security.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* We use "CrudRepository" because we don't need
  all functionalities that "JpaRepository" provides. */
@Repository
public interface AccountRepo extends CrudRepository<Account, Long> {
    // As you follow declaration, this method implemented by JPA
    Optional<Account> findByEmail(String email);
}
