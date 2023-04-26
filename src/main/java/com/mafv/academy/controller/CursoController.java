package com.mafv.academy.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.services.CursoService;
import com.mafv.academy.services.DocenteService;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    CursoService cursosService;

    @Autowired
    DocenteService docentesService;
    
    @Value("${pagination.size}")
    int sizePage;

    @GetMapping(value = "/list")
    public ModelAndView list(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:list/1/codigo/asc");
        return modelAndView;
    }

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

    @GetMapping(value = "/create")
    public ModelAndView create(Curso curso) {

        List<Docente> docentes = comprobarTutores();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("curso", new Curso());
        modelAndView.addObject("docentes", docentes);
        modelAndView.setViewName("cursos/create");

        return modelAndView;
    }

    @PostMapping(path = "/save")
    public ModelAndView save(Curso curso) throws IOException {

        Curso cs = cursosService.save(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + cs.getCodigo());

        return modelAndView;
    }

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

    @PostMapping(path = { "/update" })
    public ModelAndView update(Curso curso, @RequestParam("tutor") Optional<Integer> valor) {

        cursosService.update(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + curso.getCodigo());

        return modelAndView;
    }

    @GetMapping(path = { "/delete/{id}" })
    public ModelAndView delete(
            @PathVariable(name = "id", required = true) int id) {

        cursosService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/cursos/list");

        return modelAndView;
    }

    @GetMapping(path = { "/delete/tutor/{id}" })
    public ModelAndView deleteTutor(
            @PathVariable(name = "id", required = true) int id) {

        Docente docente = docentesService.findById(id);
        Curso curso = cursosService.findByTutor(docente);
        curso.setTutor(null);
        cursosService.save(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/cursos/list");

        return modelAndView;
    }

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

    // Añadir un docente al curso seleccionado sin tutor
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
