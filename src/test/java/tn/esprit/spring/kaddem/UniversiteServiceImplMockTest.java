package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UniversiteServiceImplMockTest {

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    @Mock
    private UniversiteRepository universiteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllUniversites() {
        Universite univ1 = new Universite();
        univ1.setIdUniv(1);
        univ1.setNomUniv("Université de Tunis");

        Universite univ2 = new Universite();
        univ2.setIdUniv(2);
        univ2.setNomUniv("Université de Sfax");

        when(universiteRepository.findAll()).thenReturn(Arrays.asList(univ1, univ2));

        List<Universite> result = universiteService.retrieveAllUniversites();

        assertEquals(2, result.size());
        assertEquals("Université de Tunis", result.get(0).getNomUniv());
        assertEquals("Université de Sfax", result.get(1).getNomUniv());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testAddUniversite() {
        Universite universite = new Universite();
        universite.setNomUniv("Université de Carthage");

        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite result = universiteService.addUniversite(universite);

        assertNotNull(result);
        assertEquals("Université de Carthage", result.getNomUniv());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testUpdateUniversite() {
        Universite universite = new Universite();
        universite.setIdUniv(1);
        universite.setNomUniv("Université de Monastir");

        // Simulation de l'université existante dans le repository
        when(universiteRepository.findById(universite.getIdUniv()))
                .thenReturn(Optional.of(universite));

        // Lorsque la méthode save est appelée, elle retourne l'université mise à jour
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite result = universiteService.updateUniversite(universite);

        assertNotNull(result);
        assertEquals("Université de Monastir", result.getNomUniv());
        verify(universiteRepository, times(1)).findById(universite.getIdUniv());
        verify(universiteRepository, times(1)).save(universite); // Vérifier que save a été appelé
    }


    @Test
    void testRetrieveUniversite() {
        Integer id = 1;
        Universite universite = new Universite();
        universite.setIdUniv(id);
        universite.setNomUniv("Université de Gabès");

        when(universiteRepository.findById(id)).thenReturn(Optional.of(universite));

        Universite result = universiteService.retrieveUniversite(id);

        assertNotNull(result);
        assertEquals("Université de Gabès", result.getNomUniv());
        verify(universiteRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteUniversite() {
        Integer id = 1;
        Universite universite = new Universite();
        universite.setIdUniv(id);
        universite.setNomUniv("Université de Gafsa");

        when(universiteRepository.findById(id)).thenReturn(Optional.of(universite));

        universiteService.deleteUniversite(id);

        verify(universiteRepository, times(1)).delete(universite);
    }


    @Test
    void testAddInvalidUniversite() {
        Universite universite = new Universite(); // Données invalides, pas de nom par exemple

        // Suppose que le repository ne sauvegarde pas une université sans nom
        when(universiteRepository.save(any(Universite.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            universiteService.addUniversite(universite);
        });

        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testDeleteNonExistentUniversite() {
        Integer id = 999; // Id qui n'existe pas

        when(universiteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            universiteService.deleteUniversite(id);
        });

        verify(universiteRepository, times(1)).findById(id);
        verify(universiteRepository, never()).delete(any(Universite.class)); // Ne doit jamais appeler delete
    }

    @Test
    void testRetrieveUniversiteByNomUniv() {
        String nomUniv = "Université de Sousse";
        Universite universite = new Universite();
        universite.setIdUniv(1);
        universite.setNomUniv(nomUniv);

        when(universiteRepository.findByNomUniv(nomUniv)).thenReturn(Optional.of(universite));

        Universite result = universiteService.retrieveUniversiteByNomUniv(nomUniv);

        assertNotNull(result);
        assertEquals(nomUniv, result.getNomUniv());
        verify(universiteRepository, times(1)).findByNomUniv(nomUniv);
    }
    @Test
    void testUpdateNonExistentUniversite() {
        Universite universite = new Universite();
        universite.setIdUniv(999); // Université qui n'existe pas
        universite.setNomUniv("Université Inconnue");

        when(universiteRepository.findById(universite.getIdUniv())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            universiteService.updateUniversite(universite);
        });

        verify(universiteRepository, times(1)).findById(universite.getIdUniv());
        verify(universiteRepository, never()).save(universite); // Ne doit pas enregistrer
    }

    @Test
    void testAddUniversiteDuplicateName() {
        Universite universite = new Universite();
        universite.setNomUniv("Université de Sousse");

        when(universiteRepository.existsByNomUniv(universite.getNomUniv())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            universiteService.addUniversite(universite);
        });

        verify(universiteRepository, times(1)).existsByNomUniv(universite.getNomUniv());
        verify(universiteRepository, never()).save(universite); // Ne doit pas enregistrer
    }
    @Test
    void testDeleteUniversiteWithDepartments() {
        Universite universite = new Universite();
        universite.setIdUniv(1);
        universite.setNomUniv("Université de Sfax");

        Departement departement1 = new Departement();
        departement1.setNomDepart("Informatique");

        Departement departement2 = new Departement();
        departement2.setNomDepart("Mathématiques");

        universite.setDepartements(new HashSet<>(Arrays.asList(departement1, departement2)));

        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));

        universiteService.deleteUniversite(1);

        verify(universiteRepository, times(1)).delete(universite);
    }
















}

