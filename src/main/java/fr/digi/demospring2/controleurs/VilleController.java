package fr.digi.demospring2.controleurs;

import fr.digi.demospring2.entities.Ville;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleController {

    @GetMapping
    public List<Ville> villes() {

        List<Ville> villes = new ArrayList<>();

        villes.add(new Ville("Paris", 2161000));
        villes.add(new Ville("Marseille", 861635));
        villes.add(new Ville("Lyon", 515695));
        villes.add(new Ville("Toulouse", 471941));
        villes.add(new Ville("Nice", 342637));
        villes.add(new Ville("Nantes", 309346));
        villes.add(new Ville("Montpellier", 285121));
        villes.add(new Ville("Strasbourg", 280966));
        villes.add(new Ville("Bordeaux", 254436));
        villes.add(new Ville("Lille", 232741));
        return villes;

    }

}
