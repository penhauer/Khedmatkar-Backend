package com.khedmatkar.demo.account.controller;

import com.khedmatkar.demo.account.dto.AdminDTO;
import com.khedmatkar.demo.account.dto.AdminPermissionDTO;
import com.khedmatkar.demo.account.entity.*;
import com.khedmatkar.demo.account.service.AccountService;
import com.khedmatkar.demo.account.dto.UserDTO;
import com.khedmatkar.demo.account.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RolesAllowed(UserType.Role.ADMIN)
@RequestMapping("/admin")
public class AdminController {
    private final AdminRepository adminRepository;
    private final AccountService accountService;
    private final RegistrationController registrationController;

    public AdminController(AdminRepository adminRepository,
                           AccountService accountService,
                           RegistrationController registrationController) {
        this.adminRepository = adminRepository;
        this.accountService = accountService;
        this.registrationController = registrationController;
    }

    @PostMapping("/register")
    @RolesAllowed(AdminPermission.Role.USER_PROFILE_RW)
    @Transactional
    public AdminDTO registerAdmin(@RequestBody @Valid AdminDTO dto) {
        var password = "123"; // todo generate random strong password
        UserDTO userDTO = UserDTO.builder()
                .email(dto.email)
                .firstName(dto.firstName)
                .lastName(dto.lastName)
                .password(password)
                .type(UserType.ADMIN.name())
                .build();
        registrationController.registerUser(userDTO);
        Admin admin = null;
        var admin_tuple = adminRepository.findByEmail(dto.email);
        if (admin_tuple.isPresent())
            admin = admin_tuple.get();
        assert admin != null;
        admin.setPermissionsFromString(dto.permissions);
        adminRepository.save(admin);
        return AdminDTO.builder()
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .email(admin.getEmail())
                .password(password)
                .permissions(admin.getPermissionsFromString())
                .build();
    }

    @PostMapping("/permission")
    @RolesAllowed(AdminPermission.Role.USER_PROFILE_RW)
    @Transactional
    public void updatePermissions(@RequestBody @Valid AdminPermissionDTO dto) {
        Admin admin;
        var admin_tuple = adminRepository.findByEmail(dto.email);
        if (admin_tuple.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "admin does not exists"
            );
        admin = admin_tuple.get();
        admin.setPermissionsFromString(dto.permissions);
        adminRepository.save(admin);
    }
}
