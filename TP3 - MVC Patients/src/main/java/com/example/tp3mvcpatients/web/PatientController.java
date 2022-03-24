package com.example.tp3mvcpatients.web;

import com.example.tp3mvcpatients.entities.Patient;
import com.example.tp3mvcpatients.repositories.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class PatientController {
    private PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping(path = "/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping(path = "/index")
    public String patients(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "5") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Patient> patients = patientRepository.findByNomContains(keyword, PageRequest.of(page, size));
        model.addAttribute("list", patients.getContent());
        model.addAttribute("nbrPages", new int[patients.getTotalPages()]);
        model.addAttribute("pagecourant", page);
        model.addAttribute("keyword", keyword);
        return "patients";
    }

    @GetMapping(path = "/delete")
    public String delete(Long id) {
        patientRepository.deleteById(id);
        return "redirect:/index";
    }

    @GetMapping(path = "/new")
    public String nouveau(Model model) {
        return "ajout";
    }

    @GetMapping(path = "/create")
    public String nouveau(Model model
            , @RequestParam(name = "nom", defaultValue = "") String nom,
                          @RequestParam(name = "ddn", defaultValue = "") String ddn,
                          @RequestParam(name = "malade", defaultValue = "") String malade,
                          @RequestParam(name = "score", defaultValue = "0") int score) {
        Patient p = new Patient();
        p.setNom(nom);
        p.setDateNaissance(ddn);
        if (malade.equals("Non"))
            p.setMalade(false);
        else
            p.setMalade(true);
        p.setScore(score);
        patientRepository.save(p);
        return "redirect:/index";
    }
    @GetMapping(path = "/maj")
    public String maj(Model model,@RequestParam(name = "id",defaultValue = "")Long id
            ,@RequestParam(name = "nom",defaultValue = "")String nom,
                          @RequestParam(name = "ddn",defaultValue = "") String ddn,
                          @RequestParam(name = "malade",defaultValue = "")String malade,
                          @RequestParam(name = "score",defaultValue = "0")int score){
        Patient p = new Patient();
        p.setNom(nom);
        p.setDateNaissance(ddn);
        if(malade.equals("Non"))
            p.setMalade(false);
        else
            p.setMalade(true);
        p.setScore(score);
        patientRepository.updatePatient(id,p.getNom(),p.getDateNaissance(),p.isMalade(),p.getScore());
        return "redirect:/index";
    }

    @GetMapping(path = "/update")
    public String modifier(Model model,Long id) {
        Patient p = patientRepository.findPatientById(id);
        model.addAttribute("id",p.getId());
        model.addAttribute("nom",p.getNom());
        model.addAttribute("date",p.getDateNaissance());
        model.addAttribute("malade",p.isMalade());
        model.addAttribute("score",p.getScore());
        return "modification";
    }
}
