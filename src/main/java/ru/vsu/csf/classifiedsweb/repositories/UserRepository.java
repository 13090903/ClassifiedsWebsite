package ru.vsu.csf.classifiedsweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.csf.classifiedsweb.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}