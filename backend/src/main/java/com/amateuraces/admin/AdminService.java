package com.amateuraces.admin;

import java.util.List;

public interface AdminService {
    /**
     * Retrieve all admins.
     * @return List of admins.
     */
    List<Admin> listAdmins();

    /**
     * Retrieve an admin by ID.
     * @param id the admin's ID
     * @return the admin
     */
    Admin getAdmin(Long id);

    /**
     * Add a new admin.
     * @param admin the admin to add
     * @return the added admin
     */
    Admin addAdmin(Admin admin);

    /**
     * Update an existing admin.
     * @param id the admin ID
     * @param admin the updated admin details
     * @return the updated admin
     */
    Admin updateAdmin(Long id, Admin admin);

    /**
     * Delete an admin.
     * @param id the admin ID
     * @return status of the delete operation (1 for success, 0 for failure)
     */
    void deleteAdmin(Long id);
}