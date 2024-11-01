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
    void addAdmin_NewPhoneNumber_ReturnSavedAdmin(){
        
        Admin admin = new Admin();
        admin.setPhoneNumber("12345678");
        when(admins.save(any(Admin.class))).thenReturn(admin);
        Admin savedAdmin = adminService.addAdmin(admin);
        
        assertNotNull(savedAdmin);
        assertEquals(admin.getPhoneNumber(),savedAdmin.getPhoneNumber());
        verify(admins).save(admin);
    }
    
    @Test
    void addAdmin_SamePhoneNumber_ReturnNull(){
        Admin admin = new Admin();
        admin.setPhoneNumber("10293848");
        Admin sameAdminNumber = new Admin();
        sameAdminNumber.setPhoneNumber("10293848");

        Optional<Admin> samePhoneNumber = Optional.of(sameAdminNumber);
        when(admins.findByPhoneNumber(admin.getPhoneNumber())).thenReturn(samePhoneNumber);
        Admin savedAdmin = adminService.addAdmin(admin);
        assertNull(savedAdmin);
        
        verify(admins).findByPhoneNumber(admin.getPhoneNumber());
        verify(admins, never()).save(any(Admin.class));
    }
    
    @Test
    public void updateAdmin_ValidId_ReturnUpdatedAdmin() {
        Admin existingAdmin = new Admin();
        existingAdmin.setId(1L);
        existingAdmin.setName("testadmin");

        Admin newAdminInfo = new Admin();
        newAdminInfo.setName("newadmin");

        when(admins.findById(1L)).thenReturn(Optional.of(existingAdmin));
        when(admins.save(any(Admin.class))).thenReturn(existingAdmin);

        Admin updatedAdmin = adminService.updateAdmin(1L, newAdminInfo);

        assertNotNull(updatedAdmin);
        assertEquals("newadmin", updatedAdmin.getName());

        verify(admins).save(existingAdmin);
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