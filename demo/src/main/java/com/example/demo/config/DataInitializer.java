package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.service.RoleService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        roleService.initializeRoles();
    }
}
