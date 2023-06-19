package com.mafv.academy.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.servlet.ModelAndView;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Estudiante;
import com.mafv.academy.models.EstudianteModulo;
import com.mafv.academy.models.Modulo;
import com.mafv.academy.services.CursoService;
import com.mafv.academy.services.DocenteService;
import com.mafv.academy.services.EstudianteService;
import com.mafv.academy.services.ModuloService;


@Controller
@RequestMapping("/modulos")
public class ModuloController {
    
    @Autowired
    ModuloService modulosService;

    @Autowired
    DocenteService docentesService;

    @Autowired
    CursoService cursosService;

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

        Page<Modulo> page = modulosService.findAll(pageable);

        List<Modulo> modulos = page.getContent();

        ModelAndView modelAndView = new ModelAndView("modulos/list");
        modelAndView.addObject("modulos", modulos);


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

        Page<Modulo> page = modulosService.findAll(pageable);

        List<Modulo> modulos = page.getContent();

        ModelAndView modelAndView = new ModelAndView("modulos/list");
        modelAndView.addObject("modulos", modulos);


        modelAndView.addObject("numPage", numPage);
        modelAndView.addObject("totalPages", page.getTotalPages());
        modelAndView.addObject("totalElements", page.getTotalElements());

        modelAndView.addObject("fieldSort", fieldSort);
        modelAndView.addObject("directionSort", directionSort.equals("asc") ? "asc" : "desc");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(value = "/create/{idCurso}")
    public ModelAndView create(Modulo modulo,
        @PathVariable(name = "idCurso", required = true) int idCurso) {

        Curso curso = cursosService.findById(idCurso);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("modulo", new Modulo());
        modelAndView.addObject("curso", curso);
        modelAndView.setViewName("modulos/create");

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(path = "/save/{idCurso}")
    public ModelAndView save(Modulo modulo,
        @PathVariable(name = "idCurso", required = true) int idCurso) throws IOException {

        Curso curso = cursosService.findById(idCurso);
        modulo.setCurso(curso);
        Modulo mod = modulosService.save(modulo);
        List<Modulo> modulos = modulosService.findByCurso(curso);
        modulos.add(mod);
        curso.setModulos(modulos);
        cursosService.save(curso);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/cursos/edit/" + idCurso);

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/edit/{idModulo}" })
    public ModelAndView edit(
            @PathVariable(name = "idModulo", required = true) int idModulo) {

        Modulo modulo = modulosService.findById(idModulo);

        ModelAndView modelAndView = new ModelAndView();
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
    @GetMapping(path = { "/delete/{idModulo}" })
    public ModelAndView delete(
            @PathVariable(name = "idModulo", required = true) int idModulo) {

        ModelAndView modelAndView = new ModelAndView();
        Modulo modulo = modulosService.findById(idModulo);

        if (modulosService.findModuloEstudiante(idModulo) && modulo.getDocente() == null){
            modulosService.deleteById(idModulo);
            modelAndView.setViewName("redirect:/modulos/list/1/codigo/asc?operacionExitoTrue=" + true);
        } else {
            modelAndView.setViewName("redirect:/modulos/list/1/codigo/asc?operacionExitoFalse=" + true);
        }

        return modelAndView;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/{idModulo}/{idCurso}" })
    public ModelAndView deleteModuloDesdeCurso(
            @PathVariable(name = "idModulo", required = true) int idModulo,
            @PathVariable(name = "idCurso", required = true) int idCurso) {

        ModelAndView modelAndView = new ModelAndView();
        Modulo modulo = modulosService.findById(idModulo);

        if (modulosService.findModuloEstudiante(idModulo) && modulo.getDocente() == null){
            modulosService.deleteById(idModulo);
            modelAndView.setViewName("redirect:/cursos/edit/" + idCurso + "?operacionExitooTrue=" + true);
        } else {
            modelAndView.setViewName("redirect:/cursos/edit/" + idCurso + "?operacionExitoFalse=" + true);
        }

        return modelAndView;
    }
    
    // Borrado de docente de un modulo desde curso
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/docente/modulo/{idModulo}" })
    public ModelAndView deleteDocenteDesdeCuso(
        @PathVariable(name = "idModulo", required = true) int idModulo) {

        Modulo modulo = modulosService.findById(idModulo);
        modulo.setDocente(null);
        modulosService.save(modulo);

        Curso curso = modulo.getCurso();


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/cursos/edit/" + curso.getCodigo() + "?operacionExitoTrue=" + true);

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

    // Mostrar docente del modulo seleccionado desde la vista de curso
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCENTE')")
    @GetMapping(path = { "/docente/{idDocente}/modulo/{idModulo}/curso/{idCurso}" })
    public ModelAndView verDocenteDeModuloCurso(
        @PathVariable(name = "idDocente", required = true) int idDocente,
        @PathVariable(name = "idModulo", required = true) int idModulo,
        @PathVariable(name = "idCurso", required = true) int idCurso) {

        Docente docente = docentesService.findById(idDocente);
        Modulo modulo = modulosService.findById(idModulo);
    
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("docente", docente);
        modelAndView.addObject("modulo", modulo);
        modelAndView.addObject("idCurso", idCurso);
        modelAndView.setViewName("modulos/docente");
    
        return modelAndView;
    }

    // Mostrar docentes para añadir uno al modulo seleccionado 
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/select/docente/{idDocente}/{idCurso}"})
    public ModelAndView selectDocente(
        @PathVariable(name = "idDocente", required = true) int idDocente,
        @PathVariable(name = "idCurso", required = true) int idCurso){

        Modulo modulo = modulosService.findById(idDocente);
        List<Docente> docentes = docentesService.findAll();
        Curso curso = cursosService.findById(idCurso);
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.addObject("modulo", modulo);
        modelAndView.addObject("docentes", docentes);
        modelAndView.addObject("curso", curso);
        modelAndView.setViewName("docentes/list");
    
        return modelAndView;
    }
    
    // Añadir un docente al modulo seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')") 
    @GetMapping(path = { "/add/docente/{idDocente}/modulo/{idModulo}/{idCurso}"})
    public ModelAndView addDocente(
        @PathVariable(name = "idModulo", required = true) int idModulo,
        @PathVariable(name = "idDocente", required = true) int idDocente,
        @PathVariable(name = "idCurso", required = true) int idCurso){
    
        Modulo modulo = modulosService.findById(idModulo);
        Docente docente = docentesService.findById(idDocente);

        modulo.setDocente(docente);
        modulosService.save(modulo);
    
        ModelAndView modelAndView = new ModelAndView();            
        modelAndView.setViewName("redirect:/cursos/edit/" + idCurso);
    
        return modelAndView;
    }

    // Mostrar la información del módulo (esta información se mostrará tanto desde el curso al listar los módulos [ahí se verá quien es su docente...]
    // y luego se verá información general del módulo, en este caso los alumnos, de los que se verá su nota, (si es admin borrarlos del módulo), 
    // y el docente podrá ponerle una nota al alumno.)
    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping(path = { "/modulo/{codigo}/{username}"})
    public ModelAndView infoModulo(
            @PathVariable(name = "codigo", required = true) int codigo,
            @PathVariable(name = "username", required = true) String username){

        Modulo modulo = modulosService.findById(codigo);
        List<Estudiante> estudiantes = estudiantesService.findByModulo(codigo);
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("modulo", modulo);
        modelAndView.addObject("estudiantes", estudiantes);
        modelAndView.setViewName("/modulos/info");

        return modelAndView;
    }

    // Borrar estudiante del módulo seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/estudiante/{idEstudiante}/modulo/{idModulo}"})
    public ModelAndView deleteEstudianteFromModulo(
        @PathVariable(name = "idEstudiante", required = true) int idEstudiante,
        @PathVariable(name = "idModulo", required = true) int idModulo){

            modulosService.deleteEstudianteFromModulo(idModulo, idEstudiante);

            ModelAndView modelAndView = new ModelAndView();            
            modelAndView.setViewName("redirect:/modulos/list/estudiantes/" + idModulo + "?operacionExitoTrue=" + true);

            return modelAndView;
        }

    // Borrar todos los estudiantes del módulo seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/delete/all/estudiante/modulo/{idModulo}"})
    public ModelAndView deleteAllEstudianteFromModulo(
        @PathVariable(name = "idModulo", required = true) int idModulo){

            modulosService.deleteAllEstudianteFromModulo(idModulo);

            ModelAndView modelAndView = new ModelAndView();            
            modelAndView.setViewName("redirect:/modulos/list/estudiantes/" + idModulo + "?operacionExitoTrue=" + true);

            return modelAndView;
        }
    
    // Listar estudiantes que no pertenezcan al módulo para añadirlos
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/list/add/estudiante/{idModulo}"})
    public ModelAndView listEstudianteToModulo(
        @PathVariable(name = "idModulo", required = true) int idModulo){

            Modulo modulo = modulosService.findById(idModulo);
            List<Estudiante> estudiantes = estudiantesService.findEstudiantesNotInModulo(modulo);

            ModelAndView modelAndView = new ModelAndView();    
            modelAndView.addObject("estudiantes", estudiantes);
            modelAndView.addObject("modulo", modulo);       
            modelAndView.setViewName("estudiantes/list");

            return modelAndView;
        }

    // Listar estudiantes del modulo seleccionado en el curso
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TUTOR')")
    @GetMapping(path = { "/list/estudiantes/{idModulo}"})
    public ModelAndView listEstudiantesFromModulo(
        @PathVariable(name = "idModulo", required = true) int idModulo){

            Modulo modulo = modulosService.findById(idModulo);
            Curso curso = modulo.getCurso();
            List<Docente> docentes = comprobarTutores();
            List<Estudiante> estudiantes = estudiantesService.findByModulo(idModulo);
            List<Modulo> modulos = modulosService.findByCurso(curso);

            ModelAndView modelAndView = new ModelAndView();    
            modelAndView.addObject("docentes", docentes);
            modelAndView.addObject("curso", curso);
            modelAndView.addObject("estudiantes", estudiantes);
            modelAndView.addObject("modulos", modulos);
            modelAndView.addObject("modulo", modulo);
            modelAndView.setViewName("cursos/edit");

            return modelAndView;
    }   
    
    // Añadir estudiante al modulo seleccionado
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping(path = { "/add/estudiante/{idEstudiante}/{idModulo}"})
    public ModelAndView addEstudianteModulo(
        @PathVariable(name = "idEstudiante", required = true) int idEstudiante,
        @PathVariable(name = "idModulo", required = true) int idModulo){

            Modulo modulo = modulosService.findById(idModulo);
            Estudiante estudiante = estudiantesService.findById(idEstudiante);

            EstudianteModulo estudianteModulo = new EstudianteModulo();
            estudianteModulo.setEstudiante(estudiante);
            estudianteModulo.setModulo(modulo);

            estudiante.setEstudianteModulo(estudianteModulo);
            estudiantesService.save(estudiante);

            ModelAndView modelAndView = new ModelAndView();           
            modelAndView.setViewName("redirect:/modulos/list/add/estudiante/" + idModulo);

            return modelAndView;
        }

    // Listar estudiantes del modulo seleccionado del docente
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCENTE')")
    @GetMapping(path = { "/list/docente/estudiantes/{idModulo}/{idDocente}"})
    public ModelAndView listEstudiantesFromModuloDocente(
        @PathVariable(name = "idModulo", required = true) int idModulo,
        @PathVariable(name = "idDocente", required = true) int idDocente){

            Docente docente = docentesService.findById(idDocente);
            Modulo modulo = modulosService.findById(idModulo);

            /* -1- Misma QUERY */
            List<EstudianteModulo> estudiantesModulo = modulosService.findEstudiantesByModulo(idModulo);

            ModelAndView modelAndView = new ModelAndView();    
            modelAndView.addObject("docente", docente);
            modelAndView.addObject("estudiantesModulo", estudiantesModulo);
            modelAndView.addObject("modulo", modulo);
            modelAndView.setViewName("/modulos/estudiantes");

            return modelAndView;
    }  
    
    // Actualizar las notas de los estudiantes del módulo
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCENTE')")
    @PostMapping(path = { "/update/notas/estudiantes"})
    public ModelAndView updateNotasEstudiantes(@RequestParam("codigo") Integer codigoModulo, @RequestParam("primEv") Float[] primeraEv, 
        @RequestParam("segunEv") Float[] segundaEv,
        @RequestParam("tercEv") Float[] terceraEv){

        /* -1- Misma QUERY */
        List<EstudianteModulo> estudiantes = modulosService.findEstudiantesByModulo(codigoModulo);
        int i = 0;

        for(EstudianteModulo estudiante : estudiantes){
            estudiante.setPrimEv(primeraEv[i]);
            estudiante.setSegunEv(segundaEv[i]);
            estudiante.setTercEv(terceraEv[i]);
            estudiantesService.saveEstModulo(estudiante);

            i++;
        }

        Modulo modulo = modulosService.findById(codigoModulo);
        Docente docente = modulo.getDocente();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/modulos/list/docente/estudiantes/" + codigoModulo + "/" + docente.getCodigo());

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
}
