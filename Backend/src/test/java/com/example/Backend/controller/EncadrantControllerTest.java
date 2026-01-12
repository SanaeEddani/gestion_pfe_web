package com.example.Backend.controller;

import com.example.Backend.model.Projet;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.service.EncadrantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EncadrantController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 DÉSACTIVE SPRING SECURITY POUR LES TESTS
class EncadrantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EncadrantService encadrantService;

    @Autowired
    private ObjectMapper objectMapper;

    /* =====================================================
       TEST : récupérer les étudiants disponibles
       ===================================================== */
    @Test
    void shouldReturnEtudiantsDisponibles() throws Exception {

        // -------- Fake étudiant --------
        Utilisateur etudiant = new Utilisateur();
        etudiant.setId(1L);
        etudiant.setNom("EL AMRANI");
        etudiant.setPrenom("Yassine");
        etudiant.setFiliere("GI");

        // -------- Fake projet --------
        Projet projet = new Projet();
        ReflectionTestUtils.setField(projet, "id", 10L); // ID simulé
        projet.setEtudiant(etudiant);
        projet.setSujet("Système de recommandation");
        projet.setEntreprise("Orange Business");
        projet.setDateDebut(LocalDate.of(2025, 3, 1));
        projet.setDateFin(LocalDate.of(2025, 7, 30));

        Mockito.when(encadrantService.getEtudiantsDisponibles())
                .thenReturn(List.of(projet));

        // -------- Appel API --------
        mockMvc.perform(get("/api/encadrant/etudiants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("EL AMRANI"))
                .andExpect(jsonPath("$[0].prenom").value("Yassine"))
                .andExpect(jsonPath("$[0].filiere").value("GI"))
                .andExpect(jsonPath("$[0].sujet").value("Système de recommandation"))
                .andExpect(jsonPath("$[0].entreprise").value("Orange Business"));
    }

    /* =====================================================
       TEST : encadrer un étudiant
       ===================================================== */
    @Test
    void shouldEncadrerEtudiant() throws Exception {

        EncadrantController.EncadrerRequest req =
                new EncadrantController.EncadrerRequest();
        req.projetId = 10L;
        req.encadrantId = 5L;

        mockMvc.perform(post("/api/encadrant/encadrer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        Mockito.verify(encadrantService)
                .encadrer(10L, 5L);
    }
}
