package com.umb.cppbt.rekammedik.rekammedik.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umb.cppbt.rekammedik.rekammedik.domain.UserAdmin;

public interface UserAdminDbRepository extends JpaRepository<UserAdmin, Long>{

	public UserAdmin findUserAdminByEmail(String email);
	
	////////////
	
}
