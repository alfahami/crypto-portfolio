package com.codelogium.portfolioservice.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelogium.portfolioservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
