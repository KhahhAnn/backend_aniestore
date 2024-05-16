package com.khahhann.backend.service.impl;

import com.khahhann.backend.model.Roles;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.RolesRepository;
import com.khahhann.backend.repository.UserRepository;
import com.khahhann.backend.service.AccountService;
import com.khahhann.backend.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private PasswordEncoder passwordEncoder;
    private UserRepository usersRepository;
    private EmailService emailService;
    private RolesRepository rolesRepository;

    @Override
    public ResponseEntity<?> register(Users user) {
        if(this.usersRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email exist");
        }
        if(user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Password is not null");
        }
        String encryptPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptPassword);
        user.setActiveCode(createActiveCode());
        user.setActive(false);
        List<Roles> defaultRoles = this.rolesRepository.findByRoleName("ROLE_USER");
        user.setRolesList(defaultRoles);
        Users newUser = this.usersRepository.saveAndFlush(user);
        sendMailActiveCode(user.getEmail(), user.getActiveCode());
        return ResponseEntity.ok("Register success!");
    }

    @Override
    public String createActiveCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void sendMailActiveCode(String email, String activeCode) {
        String subject = "Kích hoạt tài khoản của bạn tại Aniestore";
        String text = "Vui lòng sử dụng đoạn mã sau để kích hoạt tài khoản < " + email + ">:<html><body><br/><h1>"+ activeCode +"</h1></body></html>";
        text+= "<br/> Click vào link để kích hoạt tài khoản: ";
        String url = "http://localhost:8080/auth/active/"+ email + "/" + activeCode;
        text+= ("<br/> <a href="+url+">"+ url +"</a>");
        this.emailService.sendEmail("khanhanbui2003@gmail.com", email, subject, text);
    }

    @Override
    public ResponseEntity<?> activeAccount(String email, String activeCode) {
        Users user = this.usersRepository.findByEmail(email);
        if(user == null) {
            return ResponseEntity.badRequest().body("Account not exist");
        }
        if(user.isActive()) {
            return ResponseEntity.badRequest().body("Account has been activated");
        }
        if(activeCode.equals(user.getActiveCode())) {
            user.setActive(true);
            user.setActiveCode("");
            this.usersRepository.save(user);
            return ResponseEntity.ok("Active success!");
        } else {
            return ResponseEntity.badRequest().body("Wrong activation code!");
        }
    }

    @Override
    public boolean isActiveAccount(String email) {
        Users user = this.usersRepository.findByEmail(email);
        return user != null && user.isActive();
    }

}
