package ru.vsu.csf.classifiedsweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.csf.classifiedsweb.models.Advertisement;
import ru.vsu.csf.classifiedsweb.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    List<User> findAllByOrderByRatingDesc();
}
