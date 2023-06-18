package com.mafv.academy.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mafv.academy.models.Estudiante;
import com.mafv.academy.models.EstudianteModulo;
import com.mafv.academy.models.Permiso;
import com.mafv.academy.services.CursoService;
import com.mafv.academy.services.EstudianteService;
import com.mafv.academy.services.ModuloService;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {
    
    @Autowired
    EstudianteService estudiantesService;

    @Autowired
    CursoService cursosService;

    @Autowired
    ModuloService modulosService;

    @Autowired
	PasswordEncoder encoder;
    
    @Value("${pagination.size}")
    int sizePage;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/list")
    public ModelAndView listPage(Model model) {

        Integer numPage = 1;
        String fieldSort = "codigo";
        String directionSort = "asc";
        
        Pageable pageable = PageRequest.of(numPage - 1, sizePage,
            directionSort.equals("asc") ? Sort.by(fieldSort).ascending() : Sort.by(fieldSort).descending());

        Page<Estudiante> page = estudiantesService.findAll(pageable);

        List<Estudiante> estudiantes = page.getContent();

        ModelAndView modelAndView = new ModelAndView("estudiantes/list");
        modelAndView.addObject("estudiantes", estudiantes);


        modelAndView.addObject("numPage", numPage);
        modelAndView.addObject("totalPages", page.getTotalPages());
        modelAndView.addObject("totalElements", page.getTotalElements());

        modelAndView.addObject("fieldSort", fieldSort);
        modelAndView.addObject("directionSort", directionSort.equals("asc") ? "asc" : "desc");

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
    @GetMapping(path = { "/edit/{idEstudiante}" })
    public ModelAndView edit(
            @PathVariable(name = "idEstudiante", required = true) int idEstudiante) {

        Estudiante estudiante = estudiantesService.findById(idEstudiante);

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
    @GetMapping(path = { "/delete/{idEstudiante}" })
    public ModelAndView delete(
            @PathVariable(name = "idEstudiante", required = true) int idEstudiante) {

        estudiantesService.deleteById(idEstudiante);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/estudiantes/list?operacionExitoTrue=" + true);

        return modelAndView;
    }

    // Muestra información del estudiante seleccionado
    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping(path = { "/info/{idEstudiante}/{username}"})
    public ModelAndView infoEstudiante(
            @PathVariable(name = "idEstudiante", required = true) int idEstudiante,
            @PathVariable(name = "username", required = true) String username){

        Estudiante estudiante = estudiantesService.findById(idEstudiante);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("estudiante", estudiante);
        modelAndView.setViewName("/estudiantes/info");

        return modelAndView;
    }

    // Muestra los modulos a los que el estudiante está matriculado
    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping(path = { "/modulos/{idEstudiante}/{username}"})
    public ModelAndView infoModulosEstudiante(
            @PathVariable(name = "idEstudiante", required = true) int idEstudiante,
            @PathVariable(name = "username", required = true) String username){

        Estudiante estudiante = estudiantesService.findById(idEstudiante);
        List<EstudianteModulo> estudianteModulos = modulosService.findByEstudiante(idEstudiante);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("estudiante", estudiante);
        modelAndView.addObject("estModulos", estudianteModulos);
        modelAndView.setViewName("/estudiantes/modulos");

        return modelAndView;
    }


}
