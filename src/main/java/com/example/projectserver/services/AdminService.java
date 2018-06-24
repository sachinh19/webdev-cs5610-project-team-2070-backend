package com.example.projectserver.services;

import com.example.projectserver.models.Admin;
import com.example.projectserver.models.Role;
import com.example.projectserver.repositories.AdminRepository;
import com.example.projectserver.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class AdminService {

    private AdminRepository adminRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository,RoleRepository roleRepository) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("api/admin")
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    @GetMapping("api/admin/{adminId}")
    public Admin findAdminById(@PathVariable("adminId") int adminId) {

        return adminRepository.findById(adminId).orElse(null);
    }

    @GetMapping("api/admin/username/{username}")
    public Admin findAdminByUsername(@PathVariable("username") String username) {
        return adminRepository.findAdminByUsername(username).orElse(null);
    }



    @PostMapping("api/register/admin")
    public Admin createAdmin(@RequestBody Admin admin, HttpServletResponse response) {

        admin.setRoleType("Admin");
        Role role = roleRepository.findRoleByName("Admin").orElse(null);
        if(role != null){

            admin.setRole(role);
        }
        return adminRepository.save(admin);
    }

    @PutMapping("api/admin")
    public Admin updateAdmin(@RequestBody Admin newAdmin,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Admin admin = (Admin) session.getAttribute("currentUser");
        int adminId = admin.getId();
        Admin existingAdmin = adminRepository.findById(adminId).orElse(null);

        if (existingAdmin != null) {
            String username = newAdmin.getUsername();
            String password = newAdmin.getPassword();
            String firstName = newAdmin.getFirstName();
            String lastName = newAdmin.getLastName();
            String email = newAdmin.getEmail();
            String expDescription = newAdmin.getExpDescription();
            String aboutMe = newAdmin.getAboutMe();

            if (username != null) {
                existingAdmin.setUsername(username);
            }

            if (password != null) {
                existingAdmin.setPassword(password);
            }
            if (firstName != null) {
                existingAdmin.setFirstName(firstName);
            }
            if (lastName != null) {
                existingAdmin.setLastName(lastName);
            }
            if (email != null) {
                existingAdmin.setEmail(email);
            }
            if(expDescription != null){
                existingAdmin.setExpDescription(expDescription);
            }
            if(aboutMe != null){
                existingAdmin.setAboutMe(aboutMe);
            }
            existingAdmin.setUpdated(new Date());
            return adminRepository.save(existingAdmin);
        }

        response.setStatus(204);
        return existingAdmin;
    }
}
