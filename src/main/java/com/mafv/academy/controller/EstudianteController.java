package com.mafv.academy.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mafv.academy.models.Estudiante;
import com.mafv.academy.models.Permiso;
import com.mafv.academy.services.CursoService;
import com.mafv.academy.services.EstudianteService;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {
    
    @Autowired
    EstudianteService estudiantesService;

    @Autowired
    CursoService cursosService;

    @Autowired
	PasswordEncoder encoder;
    
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/list")
    public ModelAndView listPage(Model model) {

        List<Estudiante> estudiantes = estudiantesService.findAll();

        ModelAndView modelAndView = new ModelAndView("estudiantes/list");
        modelAndView.addObject("estudiantes", estudiantes);

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/create")
    public ModelAndView create(Estudiante estudiante) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("estudiante", new Estudiante());
        modelAndView.setViewName("estudiantes/create");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = "/save")
    public ModelAndView save(Estudiante estudiante,
        @RequestParam("imageForm") MultipartFile multipartFile) throws IOException {

        byte[] foto = multipartFile.getBytes();
        estudiante.setFoto(foto);

        String usuario = estudiante.getNombre().substring(0, 3) + estudiante.getApellidos().substring(0, 3) + estudiante.getDni().substring(5, 8);
        estudiante.setUsername(usuario);
        estudiante.setPassword(encoder.encode(usuario));
        estudiante.setFirstLogin(false);

        Permiso permiso = new Permiso("ESTUDIANTE", "ESTUDIANTE");
        List<Permiso> permisoEstudiante = new ArrayList<Permiso>();
        permisoEstudiante.add(permiso);
        estudiante.setPermissions(permisoEstudiante);

        Estudiante et = estudiantesService.save(estudiante);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + et.getCodigo());

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/edit/{id}" })
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) int id) {

        Estudiante estudiante = estudiantesService.findById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("estudiante", estudiante);
        modelAndView.setViewName("estudiantes/edit");
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = { "/update" })
    public ModelAndView update(Estudiante estudiante,
        @RequestParam("imageForm") MultipartFile multipartFile) throws IOException {

        byte[] foto = multipartFile.getBytes();
        estudiante.setFoto(foto);

        estudiantesService.update(estudiante);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + estudiante.getCodigo());

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/{id}" })
    public ModelAndView delete(
            @PathVariable(name = "id", required = true) int id) {

        estudiantesService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/estudiantes/list");

        return modelAndView;
    }

    // Muestra información del estudiante seleccionado
    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping(path = { "/info/{codigo}/{username}"})
    public ModelAndView infoEstudiante(
            @PathVariable(name = "codigo", required = true) int codigo,
            @PathVariable(name = "username", required = true) String username){

        Estudiante estudiante = estudiantesService.findById(codigo);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("estudiante", estudiante);
        modelAndView.setViewName("/estudiantes/info");

        return modelAndView;
    }

    // A los alumnos se le listará los módulos y se le mostrará que curso está, clickando en mas info, verá el profesor que imparte, el módulo y
    // las notas de probablemente los 3 trimestres y la media final.

}
