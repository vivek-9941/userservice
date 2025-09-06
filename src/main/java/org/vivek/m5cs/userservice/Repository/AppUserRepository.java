package org.vivek.m5cs.userservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vivek.m5cs.userservice.Model.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    public AppUser findByUsername(String username);
    public AppUser findByEmail(String email);

}
