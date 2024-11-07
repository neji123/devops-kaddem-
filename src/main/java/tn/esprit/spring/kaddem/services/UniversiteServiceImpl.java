package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UniversiteServiceImpl implements IUniversiteService{
@Autowired
    UniversiteRepository universiteRepository;
@Autowired
    DepartementRepository departementRepository;
    public UniversiteServiceImpl() {
        // TODO Auto-generated constructor stub
    }
  public   List<Universite> retrieveAllUniversites(){
return (List<Universite>) universiteRepository.findAll();
    }

    public Universite addUniversite(Universite universite) {
        // Vérifie si l'université avec ce nom existe déjà
        if (universiteRepository.existsByNomUniv(universite.getNomUniv())) {
            throw new IllegalArgumentException("Une université avec ce nom existe déjà");
        }
        return universiteRepository.save(universite);
    }

    public Universite updateUniversite(Universite universite) {
        return universiteRepository.findById(universite.getIdUniv())
                .map(existingUniv -> {
                    existingUniv.setNomUniv(universite.getNomUniv());
                    return universiteRepository.save(existingUniv);
                })
                .orElseThrow(() -> new RuntimeException("Université non trouvée"));
    }
  public Universite retrieveUniversite (Integer idUniversite){
Universite u = universiteRepository.findById(idUniversite).get();
return  u;
    }
    public  void deleteUniversite(Integer idUniversite){
        universiteRepository.delete(retrieveUniversite(idUniversite));
    }

    public void assignUniversiteToDepartement(Integer idUniversite, Integer idDepartement){
        Universite u= universiteRepository.findById(idUniversite).orElse(null);
        Departement d= departementRepository.findById(idDepartement).orElse(null);
        u.getDepartements().add(d);
        universiteRepository.save(u);
    }

    public Set<Departement> retrieveDepartementsByUniversite(Integer idUniversite){
Universite u=universiteRepository.findById(idUniversite).orElse(null);
return u.getDepartements();
    }

    public Universite retrieveUniversiteByNomUniv(String nomUniv) {
        Optional<Universite> universite = universiteRepository.findByNomUniv(nomUniv);
        if (universite.isPresent()) {
            return universite.get();
        } else {
            throw new RuntimeException("Université non trouvée avec le nom : " + nomUniv);
        }
    }
}


