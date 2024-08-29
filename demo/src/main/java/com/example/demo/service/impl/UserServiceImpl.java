package com.example.demo.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Administrator;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final AdministratorRepository administratorRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by email: {}", email);
        Administrator administrator = administratorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        logger.debug("Found administrator: {}", administrator);
        logger.debug("Administrator password: {}", administrator.getPassword());

        UserDetails userDetails = new User(administrator.getEmail(), administrator.getPassword(), getAuthorities(administrator));
        logger.debug("UserDetails created: {}", userDetails);

        return userDetails;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Administrator administrator) {
        return administrator.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
