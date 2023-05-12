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
		permisoAdmin.setCodigo("ADMIN");
		permisoAdmin.setNombre("ADMIN");

		Permiso permisoDocente = new Permiso();
		permisoDocente.setCodigo("DOCENTE");
		permisoDocente.setNombre("DOCENTE");

		Permiso permisoEstudiante = new Permiso();
		permisoEstudiante.setCodigo("ESTUDIANTE");
		permisoEstudiante.setNombre("ESTUDIANTE");

		List<Permiso> permisosTodos = new ArrayList<Permiso>();
		permisosTodos.add(permisoAdmin);
		//permisosTodos.add(permisoDocente);
		//permisosTodos.add(permisoEstudiante);

		u1.setPermissions(permisosTodos);

		permissionRepository.save(permisoAdmin);
		permissionRepository.save(permisoDocente);
		permissionRepository.save(permisoEstudiante);

		userRepository.save(u1);
	}

}
