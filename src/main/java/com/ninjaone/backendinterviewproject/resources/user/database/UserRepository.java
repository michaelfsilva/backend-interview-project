package com.ninjaone.backendinterviewproject.resources.user.database;

import com.ninjaone.backendinterviewproject.resources.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
}
