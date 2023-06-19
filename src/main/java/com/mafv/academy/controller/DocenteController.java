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

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Modulo;
import com.mafv.academy.models.Permiso;
import com.mafv.academy.services.CursoService;
import com.mafv.academy.services.DocenteService;
import com.mafv.academy.services.EstudianteService;
import com.mafv.academy.services.ModuloService;

@Controller
@RequestMapping("/docentes")
public class DocenteController {

    @Autowired
    DocenteService docentesService;

    @Autowired
	PasswordEncoder encoder;

    @Autowired
    CursoService cursosService;

    @Autowired
    ModuloService modulosService;

    @Autowired
    EstudianteService estudiantesService;

    @Value("${pagination.size}")
    int sizePage;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/list")
    public ModelAndView list(Model model) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:list/1/codigo/asc");
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/list/1/codigo/asc")
    public ModelAndView listPage(Model model) {

        Integer numPage = 1;
        String fieldSort = "codigo";
        String directionSort = "asc";
        
        Pageable pageable = PageRequest.of(numPage - 1, sizePage,
            directionSort.equals("asc") ? Sort.by(fieldSort).ascending() : Sort.by(fieldSort).descending());

        Page<Docente> page = docentesService.findAll(pageable);

        List<Docente> docentes = page.getContent();

        ModelAndView modelAndView = new ModelAndView("docentes/list");
        modelAndView.addObject("docentes", docentes);


        modelAndView.addObject("numPage", numPage);
        modelAndView.addObject("totalPages", page.getTotalPages());
        modelAndView.addObject("totalElements", page.getTotalElements());

        modelAndView.addObject("fieldSort", fieldSort);
        modelAndView.addObject("directionSort", directionSort.equals("asc") ? "asc" : "desc");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/list/{numPage}/{fieldSort}/{directionSort}")
    public ModelAndView listPage(Model model,
            @PathVariable("numPage") Integer numPage,
            @PathVariable("fieldSort") String fieldSort,
            @PathVariable("directionSort") String directionSort) {


        Pageable pageable = PageRequest.of(numPage - 1, sizePage,
            directionSort.equals("asc") ? Sort.by(fieldSort).ascending() : Sort.by(fieldSort).descending());

        Page<Docente> page = docentesService.findAll(pageable);

        List<Docente> docentes = page.getContent();

        ModelAndView modelAndView = new ModelAndView("docentes/list");
        modelAndView.addObject("docentes", docentes);


        modelAndView.addObject("numPage", numPage);
        modelAndView.addObject("totalPages", page.getTotalPages());
        modelAndView.addObject("totalElements", page.getTotalElements());

        modelAndView.addObject("fieldSort", fieldSort);
        modelAndView.addObject("directionSort", directionSort.equals("asc") ? "asc" : "desc");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/create")
    public ModelAndView create(Docente docente) throws IOException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docente", new Docente());
        modelAndView.setViewName("docentes/create");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = "/save")
    public ModelAndView save(Docente docente,
        @RequestParam("imageForm") MultipartFile multipartFile) throws IOException {

        byte foto[] = multipartFile.getBytes();
        docente.setFoto(foto);

        String usuario = docente.getNombre().substring(0, 3) + docente.getApellidos().substring(0, 3) + docente.getDni().substring(5, 8);
        docente.setUsername(usuario);
        docente.setPassword(encoder.encode(usuario));

        Permiso permiso = new Permiso("DOCENTE", "DOCENTE");
        List<Permiso> permisoDocente = new ArrayList<Permiso>();
        permisoDocente.add(permiso);
        docente.setPermissions(permisoDocente);

        Docente dc = docentesService.save(docente);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + dc.getCodigo());

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/edit/{idDocente}" })
    public ModelAndView edit(
            @PathVariable(name = "idDocente", required = true) int idDocente) {

        Docente docente = docentesService.findById(idDocente);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docente", docente);
        modelAndView.setViewName("docentes/edit");
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = { "/update" })
    public ModelAndView update(Docente docente,
        @RequestParam("imageForm") MultipartFile multipartFile) throws IOException {

        byte[] foto = multipartFile.getBytes();
        docente.setFoto(foto);
        
        docentesService.update(docente);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + docente.getCodigo());

        return modelAndView;
    }

    // Muestra información del docente seleccionado
    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping(path = { "/info/{idDocente}/{username}"})
    public ModelAndView infoDocente(
            @PathVariable(name = "idDocente", required = true) int idDocente,
            @PathVariable(name = "username", required = true) String username){

        Docente docente = docentesService.findById(idDocente);
        List<Modulo> modulos = docente.getModulos();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docente", docente);
        modelAndView.addObject("modulos", modulos);
        modelAndView.setViewName("/docentes/info");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/{idDocente}" })
    public ModelAndView delete(
            @PathVariable(name = "idDocente", required = true) int idDocente) {

        ModelAndView modelAndView = new ModelAndView();
        Docente docente = docentesService.findById(idDocente);
        Curso curso = cursosService.findByTutor(docente);

        // Se comprueba si el docenete imparte en algún módulo y si es tutor de algun curso, si alguna de estas dos se cumple, no se podrá eliminar
        if(docente.getModulos().size() != 0){
            if(curso != null){
                modelAndView.setViewName("redirect:/docentes/list/1/codigo/asc?operacionExitoFalse=" + true);
            } else {
                modelAndView.setViewName("redirect:/docentes/list/1/codigo/asc?operacionExitoTrue=" + true);
                docentesService.deleteById(idDocente);
            }
        } else {
            modelAndView.setViewName("redirect:/docentes/list/1/codigo/asc?operacionExitoFalse=" + true);
        }

        return modelAndView;
    }
    
    // Muestra los módulos que imparte el docente
    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping(path = { "/modulos/{idDocente}/{username}"})
    public ModelAndView infoModulosDocente(
            @PathVariable(name = "idDocente", required = true) int idDocente,
            @PathVariable(name = "username", required = true) String username){

        Docente docente = docentesService.findById(idDocente);
        List<Modulo> modulos = modulosService.findByDocente(docente);
        
        for (Modulo modulo : modulos){
            int numEstudiantes = modulosService.countEstudiantesByModulo(modulo.getCodigo());
            modulo.setNumEstudiantes(numEstudiantes);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docente", docente);
        modelAndView.addObject("modulos", modulos);
        modelAndView.setViewName("/docentes/modulos");

        return modelAndView;
    }

}
