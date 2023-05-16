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
import com.mafv.academy.services.CursoService;
import com.mafv.academy.services.DocenteService;
import com.mafv.academy.services.EstudianteService;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    CursoService cursosService;

    @Autowired
    DocenteService docentesService;

    @Autowired
    EstudianteService estudianteService;
    
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

        List<Docente> docentes = comprobarTutores();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("curso", new Curso());
        modelAndView.addObject("docentes", docentes);
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

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/edit/{id}" })
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) int id) {

        Curso curso = cursosService.findById(id);
        List<Docente> docentes = comprobarTutores();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docentes", docentes);
        modelAndView.addObject("curso", curso);
        modelAndView.setViewName("cursos/edit");
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = { "/update" })
    public ModelAndView update(Curso curso) {

        cursosService.update(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + curso.getCodigo());

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/{id}" })
    public ModelAndView delete(
            @PathVariable(name = "id", required = true) int id) {

        cursosService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/cursos/list");

        return modelAndView;
    }

    // TUTORES

    // Mostrar tutor del curso seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/tutor/{id}" })
    public ModelAndView mostrarTutor(
        @PathVariable(name = "id", required = true) int id) {

        Docente tutor = docentesService.findById(id);
        Curso curso = cursosService.findByTutor(tutor);
    
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("tutor", tutor);
        modelAndView.addObject("curso", curso);
        modelAndView.setViewName("cursos/tutor");
    
        return modelAndView;
    }

    // Mostrar docentes que no son tutores para añadir uno al curso seleccionado 
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/select/tutor/{id}"})
    public ModelAndView selectTutor(
        @PathVariable(name = "id", required = true) int id){

        Curso curso = cursosService.findById(id);
        List<Docente> docentes = darDocentesNoTutores();

        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.addObject("curso", curso);
        modelAndView.addObject("docentes", docentes);
        modelAndView.setViewName("docentes/list");

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
        modelAndView.setViewName("redirect:/cursos/list");
    
        return modelAndView;
    }

    // Borrado de tutor de un curso
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/tutor/{id}" })
    public ModelAndView deleteTutor(
            @PathVariable(name = "id", required = true) int id) {

        Docente tutor = docentesService.findById(id);
        Curso curso = cursosService.findByTutor(tutor);
        curso.setTutor(null);
        cursosService.save(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/cursos/list");

        return modelAndView;
    }

    // ESTUDIANTES

    // Te lleva a la lista de estudiantes para añadir estudiantes al curso marcado
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/add/estudiante/{idCurso}"})
    public ModelAndView addTutor(
        @PathVariable(name = "idCurso", required = true) int idCurso){
    
        Curso curso = cursosService.findById(idCurso);

        List<Estudiante> estudiantes = darEstudiantes(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("estudiantes", estudiantes);
        modelAndView.addObject("curso", curso);            
        modelAndView.setViewName("estudiantes/list");
    
        return modelAndView;
    }

    // Añadir el estudiante al curso 
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/add/estudiante/{idEstudiante}/curso/{idCurso}"})
    public ModelAndView addEstudiante(
        @PathVariable(name = "idCurso", required = true) int idCurso,
        @PathVariable(name = "idEstudiante", required = true) int idEstudiante){
    
        Curso curso = cursosService.findById(idCurso);
        Estudiante estudiante = estudianteService.findById(idEstudiante);
        
        List<Estudiante> estudiantes = curso.getEstudiantes();
        estudiantes.add(estudiante);
        estudiante.setCurso(curso);
        
        curso.setEstudiantes(estudiantes);
        cursosService.save(curso);
        estudianteService.save(estudiante);
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.setViewName("redirect:/cursos/add/estudiante/"+curso.getCodigo());
    
        return modelAndView;
    }
    
    // Listar los estudiantes del curso seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/list/estudiantes/{idCurso}"})
    public ModelAndView listEstudiante(
        @PathVariable(name = "idCurso", required = true) int idCurso){
    
        Curso cursoList = cursosService.findById(idCurso);
        List<Estudiante> estudiantes = estudianteService.findByCurso(cursoList);
        
        cursoList.setEstudiantes(estudiantes);
    
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("estudiantes", estudiantes);
        modelAndView.addObject("cursoList", cursoList);             
        modelAndView.setViewName("estudiantes/list");
    
        return modelAndView;
    }

    // Borrar estudiante del curso
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/estudiante/{idEstudiante}/curso/{idCurso}"})
    public ModelAndView deleteEstudiante(
        @PathVariable(name = "idCurso", required = true) int idCurso,
        @PathVariable(name = "idEstudiante", required = true) int idEstudiante){
    
        Curso curso = cursosService.findById(idCurso);
        Estudiante estudiante = estudianteService.findById(idEstudiante);
        estudianteService.deleteEstudianteFromCurso(estudiante.getCodigo(), curso.getCodigo());
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.setViewName("redirect:/cursos/list/estudiantes/"+curso.getCodigo());
    
        return modelAndView;
    }

    // Borrar TODOS los estudiantes del curso
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/all/estudiante/{idCurso}"})
    public ModelAndView deleteAllEstudiante(
        @PathVariable(name = "idCurso", required = true) int idCurso){
    
        Curso curso = cursosService.findById(idCurso);
        estudianteService.deleteAllEstudianteFromCurso(curso.getCodigo());
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.setViewName("redirect:/cursos/list");
    
        return modelAndView;
    }

    // MODULOS

    // Listado de módulos que no tienen curso

    // Añadir un módulo al curso seleccionado

    // Borrado de módulo de un curso

    // Listado de módulos de un curso

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

    // Función que coge todos los estudiantes y te devuelve aquellos que no pertenezcan a un curso
    public List<Estudiante> darEstudiantes(Curso curso){

        List<Estudiante> estudiantes = estudianteService.findAll();
        List<Estudiante> estudiantesFiltrado = new ArrayList<Estudiante>();

        for (Estudiante estudiante : estudiantes){
            if(estudiante.getCurso() == null){
                estudiantesFiltrado.add(estudiante);
            }
        }

        return estudiantesFiltrado;
    }
}
