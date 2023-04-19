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

import com.mafv.academy.models.Modulo;
import com.mafv.academy.services.ModuloService;


@Controller
@RequestMapping("/modulos")
public class ModuloController {
    
    @Autowired
    ModuloService modulosService;
    
    @GetMapping(value = "/list")
    public ModelAndView listPage(Model model) {

        List<Modulo> modulos = modulosService.findAll();

        ModelAndView modelAndView = new ModelAndView("modulos/list");
        modelAndView.addObject("modulos", modulos);

        return modelAndView;
    }

    @GetMapping(value = "/create")
    public ModelAndView create(Modulo modulo) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("modulo", new Modulo());
        modelAndView.setViewName("modulos/create");

        return modelAndView;
    }

    @PostMapping(path = "/save")
    public ModelAndView save(Modulo modulo) throws IOException {

        Modulo mod = modulosService.save(modulo);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + mod.getCodigo());

        return modelAndView;
    }

    @GetMapping(path = { "/edit/{id}" })
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) int id) {

        Modulo modulo = modulosService.findById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("modulo", modulo);
        modelAndView.setViewName("modulos/edit");
        return modelAndView;
    }

    @PostMapping(path = { "/update" })
    public ModelAndView update(Modulo modulo) {

        modulosService.update(modulo);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + modulo.getCodigo());

        return modelAndView;
    }

    @GetMapping(path = { "/delete/{id}" })
    public ModelAndView delete(
            @PathVariable(name = "id", required = true) int id) {

        modulosService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/modulos/list");

        return modelAndView;
    }
}
