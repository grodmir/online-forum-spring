package by.grodmir.online_forum.service;

import by.grodmir.online_forum.entities.Role;
import by.grodmir.online_forum.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleUser() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
