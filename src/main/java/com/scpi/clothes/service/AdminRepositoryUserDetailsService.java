package com.scpi.clothes.service;

import com.scpi.clothes.model.Admin;
import com.scpi.clothes.repository.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminRepositoryUserDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;

    public AdminRepositoryUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(s);
        if (admin == null)
            throw new UsernameNotFoundException("admin '" + s + "' not found!");
        return admin;
    }
}
