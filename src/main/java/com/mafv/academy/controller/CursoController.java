package com.mafv.academy.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Estudiante;
import com.mafv.academy.models.Modulo;
import com.mafv.academy.models.Permiso;
import com.mafv.academy.services.CursoService;
import com.mafv.academy.services.DocenteService;
import com.mafv.academy.services.EstudianteService;
import com.mafv.academy.services.ModuloService;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    CursoService cursosService;

    @Autowired
    DocenteService docentesService;

    @Autowired
    EstudianteService estudianteService;

    @Autowired
    ModuloService modulosService;

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

        Page<Curso> page = cursosService.findAll(pageable);

        List<Curso> cursos = page.getContent();

        ModelAndView modelAndView = new ModelAndView("cursos/list");
        modelAndView.addObject("cursos", cursos);


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

        Page<Curso> page = cursosService.findAll(pageable);

        List<Curso> cursos = page.getContent();

        ModelAndView modelAndView = new ModelAndView("cursos/list");
        modelAndView.addObject("cursos", cursos);


        modelAndView.addObject("numPage", numPage);
        modelAndView.addObject("totalPages", page.getTotalPages());
        modelAndView.addObject("totalElements", page.getTotalElements());

        modelAndView.addObject("fieldSort", fieldSort);
        modelAndView.addObject("directionSort", directionSort.equals("asc") ? "asc" : "desc");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/create")
    public ModelAndView create(Curso curso) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("curso", new Curso());
        modelAndView.setViewName("cursos/create");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = "/save")
    public ModelAndView save(Curso curso) throws IOException {

        Curso cs = cursosService.save(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + cs.getCodigo());

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'TUTOR')")
    @GetMapping(path = { "/edit/{idCurso}" })
    public ModelAndView edit(
            @PathVariable(name = "idCurso", required = true) int idCurso) {

        Curso curso = cursosService.findById(idCurso);
        List<Docente> docentes = comprobarTutores();
        List<Estudiante> estudiantes = cursosService.findEstudiantesByCurso(curso);
        List<Modulo> modulos = modulosService.findByCurso(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docentes", docentes);
        modelAndView.addObject("curso", curso);
        modelAndView.addObject("estudiantes", estudiantes);
        modelAndView.addObject("modulos", modulos);
        modelAndView.setViewName("cursos/edit");
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = { "/update" })
    public ModelAndView update(Curso curso) {

        cursosService.update(curso);

        if (curso.getTutor() != null){
            Docente docente = curso.getTutor();
            List<Permiso> permisos = docente.getPermissions();
            Permiso permiso = new Permiso("TUTOR", "TUTOR");
            permisos.add(permiso);
            docentesService.save(docente);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + curso.getCodigo());

        return modelAndView;
    }

    // Al borrar un curso, si tiene relaciones con otras tablas, no se podrá borrar
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/{idCurso}" })
    public ModelAndView delete(
            @PathVariable(name = "idCurso", required = true) int idCurso) {

        ModelAndView modelAndView = new ModelAndView();

        Curso curso = cursosService.findById(idCurso);
        if (curso.getTutor() != null || curso.getModulos().size() != 0){
            modelAndView.setViewName("redirect:/cursos/list/1/codigo/asc?operacionExitoFalse=" + true);
        } else {
            cursosService.deleteById(idCurso);
            modelAndView.setViewName("redirect:/cursos/list/1/codigo/asc?operacionExitoTrue=" + true);
        }

        return modelAndView;
    }

    // TUTORES

    // Mostrar tutor del curso seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/tutor/{idCurso}" })
    public ModelAndView mostrarTutor(
        @PathVariable(name = "idCurso", required = true) int idCurso) {

        Docente tutor = docentesService.findById(idCurso);
        Curso curso = cursosService.findByTutor(tutor);
    
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("tutor", tutor);
        modelAndView.addObject("curso", curso);
        modelAndView.setViewName("cursos/tutor");
    
        return modelAndView;
    }

    // Muestra la tutoria al Docente que es tutor de un curso
    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping(path = { "/tutor/{idDocente}/{username}"})
    public ModelAndView tutoriaDocente(
            @PathVariable(name = "idDocente", required = true) int idDocente,
            @PathVariable(name = "username", required = true) String username){

        Docente docente = docentesService.findById(idDocente);
        Curso curso = cursosService.findByTutor(docente);
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/cursos/edit/" + curso.getCodigo());

        return modelAndView;
    }

    // Mostrar docentes que no son tutores para añadir uno al curso seleccionado  - EN DESUSO POR AHORA
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/select/tutor/{id}"})
    public ModelAndView selectTutor(
        @PathVariable(name = "id", required = true) int id){

        Curso curso = cursosService.findById(id);
        List<Docente> docentes = darDocentesNoTutores();

        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.addObject("curso", curso);
        modelAndView.addObject("docentes", docentes);
        modelAndView.setViewName("docentes/list/1/codigo/asc");

        return modelAndView;
    }

    // Añadir un docente al curso seleccionado sin tutor
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/add/tutor/{idTutor}/curso/{idCurso}"})
    public ModelAndView addTutor(
        @PathVariable(name = "idCurso", required = true) int idCurso,
        @PathVariable(name = "idTutor", required = true) int idTutor){
    
        Curso curso = cursosService.findById(idCurso);
        Docente tutor = docentesService.findById(idTutor);

        curso.setTutor(tutor);
        cursosService.save(curso);
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.setViewName("redirect:/cursos/list/1/codigo/asc");
    
        return modelAndView;
    }

    // Borrado de tutor de un curso
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/tutor/{idCurso}" })
    public ModelAndView deleteTutor(
            @PathVariable(name = "idCurso", required = true) int idCurso) {

        cursosService.deleteTutor(idCurso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/cursos/edit/" + idCurso);

        return modelAndView;
    }

    // Añadir un módulo al curso seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/add/modulo/{idModulo}/curso/{idCurso}"})
    public ModelAndView addModuloCurso(
        @PathVariable(name = "idCurso", required = true) int idCurso,
        @PathVariable(name = "idModulo", required = true) int idModulo){
    
        Curso curso = cursosService.findById(idCurso);
        Modulo modulo = modulosService.findById(idModulo);
        modulo.setCurso(curso);
        
        cursosService.save(curso);
        modulosService.save(modulo);
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.setViewName("redirect:/cursos/add/modulo/"+curso.getCodigo());
    
        return modelAndView;
    }

    // Borrado de módulo de un curso
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/modulo/{idModulo}/curso/{idCurso}"})
    public ModelAndView deleteModulo(
        @PathVariable(name = "idCurso", required = true) int idCurso,
        @PathVariable(name = "idModulo", required = true) int idModulo){
    
        Curso curso = cursosService.findById(idCurso);
        Modulo modulo = modulosService.findById(idModulo);
        modulosService.deleteModuloFromCurso(modulo.getCodigo(), curso.getCodigo());
        modulosService.deleteById(idModulo);
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.setViewName("redirect:/cursos/edit/"+curso.getCodigo());
    
        return modelAndView;
    }

    // FUNCIONES

    // Función que comprueba todos los docentes y coloca el atributo "tutor" a true si dicho docente es tutor de ese curso y los devuelve
    public List<Docente> comprobarTutores(){
        List<Curso> cursos = cursosService.findAll();
        List<Docente> docentes = docentesService.findAll();

        for (Curso cursot : cursos){
            for (Docente docente : docentes){
                if(cursot.getTutor() != null){
                    if(cursot.getTutor().getCodigo() == docente.getCodigo()){
                        docente.setEsTutor(true);
                        break;
                    }
                }
            }
        }

        return docentes;
    }

    // Función que comprueba los docentes y te devuelve solo aquellos que no sean tutores
    public List<Docente> darDocentesNoTutores(){
        List<Docente> docentes = comprobarTutores();
        List<Docente> noTutores = new ArrayList<Docente>();

        for (Docente docente : docentes){
            if(!docente.isEsTutor()){
                noTutores.add(docente);
            }
        }

        return noTutores;
    }
}
