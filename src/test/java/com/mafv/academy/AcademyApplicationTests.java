package com.mafv.academy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mafv.academy.models.Permiso;
import com.mafv.academy.models.Usuario;
import com.mafv.academy.repository.PermisoRepository;
import com.mafv.academy.repository.UsuarioRepository;

@SpringBootTest
class AcademyApplicationTests {

	@Autowired
	UsuarioRepository userRepository;

	@Autowired
	PermisoRepository permissionRepository;

	@Autowired
	PasswordEncoder encoder;

	@Test
	void crearUsuarioTest() {

		Usuario u1 = new Usuario();
		u1.setCodigo(1);
		u1.setUsername("admin");
		u1.setPassword(encoder.encode("admin"));

		Permiso permisoAdmin = new Permiso();
		permisoAdmin.setCodigo("admin");
		permisoAdmin.setNombre("admin");

		List<Permiso> permisosTodos = new ArrayList<Permiso>();
		permisosTodos.add(permisoAdmin);

		u1.setPermissions(permisosTodos);

		permissionRepository.save(permisoAdmin);

		userRepository.save(u1);
	}

}
