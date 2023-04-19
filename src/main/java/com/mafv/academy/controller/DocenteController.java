package com.mafv.academy.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.services.CursoService;
import com.mafv.academy.services.DocenteService;

@Controller
@RequestMapping("/docentes")
public class DocenteController {
    
    @Autowired
    DocenteService docentesService;

    @Autowired
    CursoService cursosService;
    
    @GetMapping(value = "/list")
    public ModelAndView listPage(Model model) {

        List<Docente> docentes = docentesService.findAll();

        ModelAndView modelAndView = new ModelAndView("docentes/list");
        modelAndView.addObject("docentes", docentes);

        return modelAndView;
    }

    @GetMapping(value = "/create")
    public ModelAndView create(Docente docente) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docente", new Docente());
        modelAndView.setViewName("docentes/create");

        return modelAndView;
    }

    @PostMapping(path = "/save")
    public ModelAndView save(Docente docente) throws IOException {

        Docente dc = docentesService.save(docente);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + dc.getCodigo());

        return modelAndView;
    }

    @GetMapping(path = { "/edit/{id}" })
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) int id) {

        Docente docente = docentesService.findById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docente", docente);
        modelAndView.setViewName("docentes/edit");
        return modelAndView;
    }

    @PostMapping(path = { "/update" })
    public ModelAndView update(Docente docente) {

        docentesService.update(docente);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + docente.getCodigo());

        return modelAndView;
    }

    @GetMapping(path = { "/delete/{id}" })
    public ModelAndView delete(
            @PathVariable(name = "id", required = true) int id) {

        // Se comprueba si el docente es tutor de un curso, si lo es, borrará el tutor del curso
        Docente docente = docentesService.findById(id);
        Curso curso = cursosService.findByTutor(docente);

        if(curso != null){
            if(curso.getTutor() != null){
                curso.setTutor(null);
                cursosService.update(curso);
            }
        }

        docentesService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/docentes/list");

        return modelAndView;
    }
}
