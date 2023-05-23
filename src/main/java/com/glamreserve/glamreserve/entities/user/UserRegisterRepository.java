package com.glamreserve.glamreserve.entities.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRegisterRepository extends JpaRepository<UserRegisterRequest, Long> {
}
