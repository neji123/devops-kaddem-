package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;
import tn.esprit.spring.kaddem.services.UniversiteServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UniversiteServiceImplTest {

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private UniversiteServiceImpl universiteService;

    @BeforeEach
    void setUp() {
        // Clear the repository before each test
        universiteRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Clear the repository after each test
        universiteRepository.deleteAll();
    }

    @Test
    void testRetrieveAllUniversites() {
        Universite univ1 = new Universite();
        univ1.setNomUniv("Université de Tunis");
        universiteRepository.save(univ1);

        Universite univ2 = new Universite();
        univ2.setNomUniv("Université de Sfax");
        universiteRepository.save(univ2);

        List<Universite> result = universiteService.retrieveAllUniversites();

        assertEquals(2, result.size());
    }

    @Test
    void testAddUniversite() {
        Universite universite = new Universite();
        universite.setNomUniv("Université de Carthage");

        Universite result = universiteService.addUniversite(universite);

        assertNotNull(result.getIdUniv());
        assertEquals("Université de Carthage", result.getNomUniv());
    }

    @Test
    void testUpdateUniversite() {
        Universite universite = new Universite();
        universite.setNomUniv("Université de Monastir");
        Universite savedUniversite = universiteRepository.save(universite);

        savedUniversite.setNomUniv("Université de Kairouan");
        Universite updatedResult = universiteService.updateUniversite(savedUniversite);

        assertEquals("Université de Kairouan", updatedResult.getNomUniv());
    }

    @Test
    void testRetrieveUniversite() {
        Universite universite = new Universite();
        universite.setNomUniv("Université de Gabès");
        Universite savedUniversite = universiteRepository.save(universite);

        Universite result = universiteService.retrieveUniversite(savedUniversite.getIdUniv());

        assertNotNull(result);
        assertEquals("Université de Gabès", result.getNomUniv());
    }

    @Test
    void testDeleteUniversite() {
        Universite universite = new Universite();
        universite.setNomUniv("Université de Gafsa");
        Universite savedUniversite = universiteRepository.save(universite);

        universiteService.deleteUniversite(savedUniversite.getIdUniv());

        assertFalse(universiteRepository.findById(savedUniversite.getIdUniv()).isPresent());
    }

    @Test
    void testDeleteNonExistentUniversite() {
        Integer id = 999; // ID qui n'existe pas dans la base

        // Ici, on ne simule pas le comportement du repository, mais on s'assure
        // que l'ID n'existe pas dans la base
        Optional<Universite> universiteOptional = universiteRepository.findById(id);
        assertFalse(universiteOptional.isPresent());

        // On vérifie que l'appel à la méthode de suppression lance une exception
        assertThrows(RuntimeException.class, () -> {
            universiteService.deleteUniversite(id);
        });
    }

    @Test
    void testRetrieveUniversiteByNomUniv() {
        // Créer et sauvegarder une université dans la base
        Universite universite = new Universite();
        universite.setNomUniv("Université de Sousse");
        universiteRepository.save(universite);

        // Appeler le service pour récupérer l'université par son nom
        Universite result = universiteService.retrieveUniversiteByNomUniv("Université de Sousse");

        // Vérifications
        assertNotNull(result);
        assertEquals("Université de Sousse", result.getNomUniv());
    }
    @Test
    void testUpdateNonExistentUniversite() {
        Universite universite = new Universite();
        universite.setIdUniv(999); // ID qui n'existe pas
        universite.setNomUniv("Université Inconnue");

        // On s'attend à ce qu'une exception soit lancée
        assertThrows(RuntimeException.class, () -> {
            universiteService.updateUniversite(universite);
        });

        // Vérifie que l'université n'est pas présente dans le repository
        assertFalse(universiteRepository.findById(universite.getIdUniv()).isPresent());
    }


    @Test
    void testAddUniversiteDuplicateName() {
        Universite universite = new Universite();
        universite.setNomUniv("Université de Sousse");

        // Ajoute d'abord l'université dans le repository
        universiteService.addUniversite(universite);

        // Crée une nouvelle instance d'université avec le même nom
        Universite duplicateUniversite = new Universite();
        duplicateUniversite.setNomUniv("Université de Sousse");

        // Vérifie que l'ajout d'une université avec un nom en double lève une exception
        assertThrows(IllegalArgumentException.class, () -> {
            universiteService.addUniversite(duplicateUniversite);
        });
    }


}

