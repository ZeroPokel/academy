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

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Modulo;
import com.mafv.academy.models.Permiso;
import com.mafv.academy.services.CursoService;
import com.mafv.academy.services.DocenteService;
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
    
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/list")
    public ModelAndView listPage(Model model) {

        List<Docente> docentes = docentesService.findAll();

        ModelAndView modelAndView = new ModelAndView("docentes/list");
        modelAndView.addObject("docentes", docentes);

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
    @GetMapping(path = { "/edit/{id}" })
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) int id) {

        Docente docente = docentesService.findById(id);

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
    @GetMapping(path = { "/info/{codigo}/{username}"})
    public ModelAndView infoDocente(
            @PathVariable(name = "codigo", required = true) int codigo,
            @PathVariable(name = "username", required = true) String username){

        Docente docente = docentesService.findById(codigo);
        List<Modulo> modulos = docente.getModulos();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docente", docente);
        modelAndView.addObject("modulos", modulos);
        modelAndView.setViewName("/docentes/info");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/{id}" })
    public ModelAndView delete(
            @PathVariable(name = "id", required = true) int id) {

        // Se comprueba si el docente es tutor de un curso, si lo es, borrará el tutor del curso
        Docente docente = docentesService.findById(id);
        Curso curso = cursosService.findByTutor(docente);
        
        if(curso != null){
            cursosService.deleteTutor(curso.getCodigo());
        }

        // Además se comprueba si el docenete imparte en algún módulo, si lo hace, será eliminado como docente del módulo
        if(docente.getModulos().size() != 0){
            docentesService.deleteDocenteFromAllModulo(id);
        }

        docentesService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/docentes/list");

        return modelAndView;
    }


    // Si el docente es tutor, tendrá una vista de tutoría, donde le saldrá información de la clase que es tutor, los módulos, que profesor imparte dicho módulo,
    // y de ahí podrá ir a la info de los módulos y de los alumnos
}
