package tn.esprit.spring.kaddem.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.kaddem.entities.Universite;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversiteRepository extends CrudRepository<Universite,Integer> {



        // Cette méthode trouvera une université par son nom
        Optional<Universite> findByNomUniv(String nomUniv);
    boolean existsByNomUniv(String nomUniv);

}
