package com.project.jobportal.Repository;

import com.project.jobportal.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Usersrepository extends JpaRepository<Users,Integer> {

    Optional<Users> findByEmail(String email);
}
