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
import com.mafv.academy.services.CursoService;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    CursoService cursosService;
    
    @GetMapping(value = "/list")
    public ModelAndView listPage(Model model) {

        List<Curso> cursos = cursosService.findAll();

        ModelAndView modelAndView = new ModelAndView("cursos/list");
        modelAndView.addObject("cursos", cursos);

        return modelAndView;
    }

    @GetMapping(value = "/create")
    public ModelAndView create(Curso curso) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("curso", new Curso());
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

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("curso", curso);
        modelAndView.setViewName("cursos/edit");
        return modelAndView;
    }

    @PostMapping(path = { "/update" })
    public ModelAndView update(Curso curso) {

        cursosService.update(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + curso.getCodigo());

        return modelAndView;
    }

    @GetMapping(path = { "/delete/{id}" })
    public ModelAndView delete(
            @PathVariable(name = "id", required = true) int id) {

        Curso curso = cursosService.findById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("curso", curso);
        modelAndView.setViewName("cursos/delete");

        return modelAndView;
    }

    @GetMapping(path = { "/confirmDelete/{id}" })
    public ModelAndView confirmDelete(
            @PathVariable(name = "id", required = true) int id) {

        cursosService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/cursos/list");

        return modelAndView;
    }


}
