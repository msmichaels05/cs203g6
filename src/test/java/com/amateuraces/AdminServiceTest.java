package com.amateuraces;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amateuraces.admin.Admin;
import com.amateuraces.admin.AdminRepository;
import com.amateuraces.admin.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    
    @Mock
    private AdminRepository admins;

    @InjectMocks
    private AdminServiceImpl adminService;
    
    @Test
    void addAdmin_NewName_ReturnSavedAdmin(){
        Admin admin = new Admin();
        admin.setName("Updated Name of Admin");
        when(admins.save(any(Admin.class))).thenReturn(admin);
        Admin savedAdmin = adminService.addAdmin(admin);
        
        assertNotNull(savedAdmin);
        assertEquals(admin.getName(),savedAdmin.getName());

        verify(admins).save(admin);
    }

    @Test
    void updateAdmin_NotFound_ReturnNull(){
        Admin admin = new Admin();
        admin.setName("Updated Name of Admin");
        Long adminID = 10L;
        when(admins.findById(adminID)).thenReturn(Optional.empty());
        
        Admin updatedAdmin = adminService.updateAdmin(adminID, admin);
        
        assertNull(updatedAdmin);
        verify(admins).findById(adminID);
        verify(admins, never()).save(any(Admin.class));
    }    
}