package com.mafv.academy.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Modulo;
import com.mafv.academy.services.DocenteService;
import com.mafv.academy.services.ModuloService;


@Controller
@RequestMapping("/modulos")
public class ModuloController {
    
    @Autowired
    ModuloService modulosService;

    @Autowired
    DocenteService docentesService;
    
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/list")
    public ModelAndView listPage(Model model) {

        List<Modulo> modulos = modulosService.findAll();

        ModelAndView modelAndView = new ModelAndView("modulos/list");
        modelAndView.addObject("modulos", modulos);

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/create")
    public ModelAndView create(Modulo modulo) {

        List<Docente> docentes = docentesService.findAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docentes", docentes);
        modelAndView.addObject("modulo", new Modulo());
        modelAndView.setViewName("modulos/create");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = "/save")
    public ModelAndView save(Modulo modulo) throws IOException {

        Modulo mod = modulosService.save(modulo);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + mod.getCodigo());

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/edit/{id}" })
    public ModelAndView edit(
            @PathVariable(name = "id", required = true) int id) {

        Modulo modulo = modulosService.findById(id);
        List<Docente> docentes = docentesService.findAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docentes", docentes);
        modelAndView.addObject("modulo", modulo);
        modelAndView.setViewName("modulos/edit");
        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = { "/update" })
    public ModelAndView update(Modulo modulo) {

        modulosService.update(modulo);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:edit/" + modulo.getCodigo());

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/{id}" })
    public ModelAndView delete(
            @PathVariable(name = "id", required = true) int id) {

        modulosService.deleteById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/modulos/list");

        return modelAndView;
    }

    // Borrado de docente de un modulo
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/docente/modulo/{idModulo}" })
    public ModelAndView deleteDocente(
        @PathVariable(name = "idModulo", required = true) int idModulo) {

        Modulo modulo = modulosService.findById(idModulo);
        modulo.setDocente(null);
        modulosService.save(modulo);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/modulos/list");

        return modelAndView;
    }

    // Mostrar docente del modulo seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/docente/{idDocente}/modulo/{idModulo}" })
    public ModelAndView mostrarDocente(
        @PathVariable(name = "idDocente", required = true) int idDocente,
        @PathVariable(name = "idModulo", required = true) int idModulo) {

        Docente docente = docentesService.findById(idDocente);
        Modulo modulo = modulosService.findById(idModulo);
    
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docente", docente);
        modelAndView.addObject("modulo", modulo);
        modelAndView.setViewName("modulos/docente");
    
        return modelAndView;
    }

    // Mostrar docentes para añadir uno al modulo seleccionado 
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/select/docente/{id}"})
    public ModelAndView selectDocente(
        @PathVariable(name = "id", required = true) int id){

        Modulo modulo = modulosService.findById(id);
        List<Docente> docentes = docentesService.findAll();
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.addObject("modulo", modulo);
        modelAndView.addObject("docentes", docentes);
        modelAndView.setViewName("docentes/list");
    
        return modelAndView;
    }
    
    // Añadir un docente al modulo seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')") 
    @GetMapping(path = { "/add/docente/{idDocente}/modulo/{idModulo}"})
    public ModelAndView addDocente(
        @PathVariable(name = "idModulo", required = true) int idModulo,
        @PathVariable(name = "idDocente", required = true) int idDocente){
    
        Modulo modulo = modulosService.findById(idModulo);
        Docente docente = docentesService.findById(idDocente);

        modulo.setDocente(docente);
        modulosService.save(modulo);
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.setViewName("redirect:/modulos/list");
    
        return modelAndView;
    }
}
