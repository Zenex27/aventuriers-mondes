package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Joueur {
    public enum CouleurJouer {
        JAUNE, ROUGE, BLEU, VERT, ROSE;
    }

    /**
     * Jeu auquel le joueur est rattaché
     */
    private final Jeu jeu;
    /**
     * Nom du joueur
     */
    private final String nom;
    /**
     * CouleurJouer du joueur (pour représentation sur le plateau)
     */
    private final CouleurJouer couleur;
    /**
     * Liste des villes sur lesquelles le joueur a construit un port
     */
    private final List<Ville> ports;
    /**
     * Liste des routes capturées par le joueur
     */
    private final List<Route> routes;
    /**
     * Nombre de pions wagons que le joueur peut encore poser sur le plateau
     */
    private int nbPionsWagon;
    /**
     * Nombre de pions wagons que le joueur a dans sa réserve (dans la boîte)
     */
    private int nbPionsWagonEnReserve;

    /**
     * Nombre de pions bateaux que le joueur peut encore poser sur le plateau
     */
    private int nbPionsBateau;
    /**
     * Nombre de pions bateaux que le joueur a dans sa réserve (dans la boîte)
     */
    private int nbPionsBateauEnReserve;
    /**
     * Liste des destinations à réaliser pendant la partie
     */
    private final List<Destination> destinations;
    /**
     * Liste des cartes que le joueur a en main
     */
    private final List<CarteTransport> cartesTransport;
    /**
     * Liste temporaire de cartes transport que le joueur est en train de jouer pour
     * payer la capture d'une route ou la construction d'un port
     */
    private final List<CarteTransport> cartesTransportPosees;
    /**
     * Score courant du joueur (somme des valeurs des routes capturées et points
     * perdus lors des échanges de pions)
     */
    private int score;

    public Joueur(String nom, Jeu jeu, CouleurJouer couleur) {
        this.nom = nom;
        this.jeu = jeu;
        this.couleur = couleur;
        this.ports = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.nbPionsWagon = 0;
        this.nbPionsWagonEnReserve = 25;
        this.nbPionsBateau = 0;
        this.nbPionsBateauEnReserve = 50;
        this.cartesTransport = new ArrayList<>();
        this.cartesTransportPosees = new ArrayList<>();
        this.destinations = new ArrayList<>();
        this.score = 0;
    }

    public void removeDestinations(Destination d) {
        destinations.remove(d);
    }

    public void setDestinations(Destination d) {
        destinations.add(d);
    }

    public List<CarteTransport> getCartesTransport() {
        return cartesTransport;
    }

    public void setNbPionsWagon(int nbPionsWagon) {
        this.nbPionsWagon = nbPionsWagon;
    }

    public void setNbPionsWagonEnReserve(int nbPionsWagonEnReserve) {
        this.nbPionsWagonEnReserve = nbPionsWagonEnReserve;
    }

    public void setNbPionsBateau(int nbPionsBateau) {
        this.nbPionsBateau = nbPionsBateau;
    }

    public void setNbPionsPorts(int nbPionsPorts) {
        this.nbPionsPorts = nbPionsPorts;
    }

    public void setNbPionsBateauEnReserve(int nbPionsBateauEnReserve) {
        this.nbPionsBateauEnReserve = nbPionsBateauEnReserve;
    }

    public String getNom() {
        return nom;
    }

    public CarteTransport piocherCarteTransportDansPile(String choix) {

        if (choix.equals("BATEAU")) {
            return jeu.piocherCarteBateau();
        } else {
            return jeu.piocherCarteWagon();
        }
    }

    public void piocherDansPileWagon(ArrayList<String> tab, String choix) {
        if (choix.split("")[0].equals("W")) {
            cartesTransport.add(jeu.piocherCarteWagon());
        }
        if (choix.split("")[0].equals("B")) {
            cartesTransport.add(jeu.piocherCarteBateau());
        }
    }

    public void piocherDansPileBateau(ArrayList<String> tab, String choix) {
        if (choix.split("")[0].equals("W")) {
            cartesTransport.add(jeu.piocherCarteWagon());
        }
        if (choix.split("")[0].equals("B")) {
            cartesTransport.add(jeu.piocherCarteBateau());
        }
    }

    public void TroisJokerDansCarteVisible() {
        int n = 0;
        for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
            if (carte.getType() == TypeCarteTransport.JOKER) {
                n++;
            }
        }
        while (n >= 3) {
            n = 0;
            for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                jeu.DefausserDansBateau(carte);
                jeu.DefausserDansWagon(carte);
            }

            for (int i = 0; i < 3; i++) {
                jeu.AddCarteTransport(jeu.piocherCarteBateau());
                jeu.AddCarteTransport(jeu.piocherCarteWagon());
            }

            for (int i = 0; i < 6; i++) {
                if (jeu.getCartesTransportVisibles().get(i).getType() == TypeCarteTransport.JOKER) {
                    n++;
                }
            }
        }
    }

    public void PiocherCarteQuandJokerExistePas(String choix2, CarteTransport cart1, ArrayList<String> tab) {
        if (choix2.split("")[0].equals("C") && (cart1 == null || cart1.getType() != TypeCarteTransport.JOKER)) {
            String choix1 = choisir(
                    "Par quel pioche voulez remplacer la carte prise ?",
                    tab,
                    null,
                    false);
            for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                if (Objects.equals(carte.getNom(), choix2)) {
                    cartesTransport.add(carte);
                    jeu.ModifierCarteTransport(carte);
                    jeu.AddCarteTransport(piocherCarteTransportDansPile(choix1));
                }
            }
            TroisJokerDansCarteVisible();
        } else {
            String choix1 = choisir(
                    "Par quel pioche voulez remplacer la carte prise ?",
                    tab,
                    null,
                    true);
            if (choix1.split("")[0].equals("W")) {
                cartesTransport.add(jeu.piocherCarteWagon());
            }
            if (choix1.split("")[0].equals("B")) {
                cartesTransport.add(jeu.piocherCarteBateau());
            }
        }
    }

    public void PiocherCarteTransport(ArrayList<String> tab, String choix) {
        if (choix.split("")[0].equals("C")) {
            if (!jeu.piocheBateauEstVide() || !jeu.piocheWagonEstVide()) {
                String choix1 = choisir(
                        "Quelle action souhaitez-vous faire ?",
                        tab,
                        null,
                        false);
                for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                    if (Objects.equals(carte.getNom(), choix)) {
                        cartesTransport.add(carte);
                        jeu.ModifierCarteTransport(carte);
                        jeu.AddCarteTransport(piocherCarteTransportDansPile(choix1));
                    }
                }
                CarteTransport cart = cartesTransport.get(cartesTransport.size() - 1);

                if (cart == null || cart.getType() != TypeCarteTransport.JOKER) {
                    String choix2 = choisir(
                            "Ou voulez voulez piocher ?",
                            tab,
                            null,
                            true);

                    CarteTransport cart1 = null;
                    for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                        if (choix2.equals(carte.getNom())) {
                            if (carte.getType() == TypeCarteTransport.JOKER) {
                                cart1 = carte;
                            }
                        }
                    }
                    PiocherCarteQuandJokerExistePas(choix2, cart1, tab);
                }
            } else {
                String choix1 = choisir(
                        "Quelle action souhaitez-vous faire ?",
                        tab,
                        null,
                        false);
                for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                    if (Objects.equals(carte.getNom(), choix)) {
                        cartesTransport.add(carte);
                        jeu.ModifierCarteTransport(carte);
                    }
                    if (Objects.equals(carte.getNom(), choix1)) {
                        cartesTransport.add(carte);
                        jeu.ModifierCarteTransport(carte);
                    }
                }
            }
        } else {
            if (choix.split("")[0].equals("W")) {
                piocherDansPileWagon(tab, choix);
                String choixP = choisir(
                        "Quelle action souhaitez-vous faire ?",
                        tab,
                        null,
                        true);
                CarteTransport cart = null;

                for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                    if (choix.equals(carte.getNom())) {
                        if (carte.getType() == TypeCarteTransport.JOKER) {
                            cart = carte;
                        }
                    }
                }
                if (choixP.split("")[0].equals("C")) {
                    PiocherCarteQuandJokerExistePas(choixP, cart, tab);
                }
                if (choixP.split("")[0].equals("B")) {
                    piocherDansPileBateau(tab, choixP);
                }
                if (choixP.split("")[0].equals("W")) {
                    piocherDansPileWagon(tab, choixP);
                }
            }
            if (choix.split("")[0].equals("B")) {
                piocherDansPileBateau(tab, choix);
                String choixP = choisir(
                        "Quelle action souhaitez-vous faire ?",
                        tab,
                        null,
                        true);
                CarteTransport cart = null;

                for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                    if (choix.equals(carte.getNom())) {
                        if (carte.getType() == TypeCarteTransport.JOKER) {
                            cart = carte;
                        }
                    }
                }
                if (choixP.split("")[0].equals("C")) {
                    PiocherCarteQuandJokerExistePas(choixP, cart, tab);
                }
                if (choixP.split("")[0].equals("B")) {
                    piocherDansPileBateau(tab, choixP);
                }
                if (choixP.split("")[0].equals("W")) {
                    piocherDansPileWagon(tab, choixP);
                }
            }
        }
    }


    public void EchangerPionsWagons() {
        List<Bouton> boutons = new ArrayList<>();
        int n = Math.min(nbPionsWagonEnReserve, nbPionsBateau);
        for (int i = 1; i <= n; i++) {
            boutons.add(new Bouton(String.valueOf(i)));
        }

        String choix = choisir(
                "Quel pions voulez vous échanger",
                null,
                boutons,
                false);

        nbPionsWagon += Integer.parseInt(choix);
        nbPionsBateauEnReserve += Integer.parseInt(choix);
        nbPionsWagonEnReserve -= Integer.parseInt(choix);
        nbPionsBateau -= Integer.parseInt(choix);
        score -= Integer.parseInt(choix);
    }

    public void EchangerPionsBateaux() {
        List<Bouton> boutons = new ArrayList<>();
        int n = Math.min(nbPionsBateauEnReserve, nbPionsWagon);
        for (int i = 1; i <= n; i++) {
            boutons.add(new Bouton(String.valueOf(i)));
        }

        String choix = choisir(
                "Combien de pions voulez vous échanger",
                null,
                boutons,
                false);

        nbPionsWagon -= Integer.parseInt(choix);
        nbPionsBateauEnReserve -= Integer.parseInt(choix);
        nbPionsWagonEnReserve += Integer.parseInt(choix);
        nbPionsBateau += Integer.parseInt(choix);
        score -= Integer.parseInt(choix);
    }

    public void EchangerPions(String pion) {
        if (Objects.equals(pion, "PIONS WAGON")) {
            EchangerPionsWagons();
        } else {
            EchangerPionsBateaux();
        }
    }


    public void PrendreNvDestinations() {
        List<Destination> dest = new ArrayList<>();
        if (jeu.getPileDestinations().isEmpty()) {
            log(String.format("%s désolé, la pioche destination est vide", toLog()));
        }
        if (jeu.getPileDestinations().size() < 4) {
            for (int i = 0; i < jeu.getPileDestinations().size(); i++) {
                dest.add(jeu.getPileDestinations().remove(0));
                destinations.add(dest.get(i));
            }
            while (dest.size() > 1) {
                List<Bouton> boutons = new ArrayList<>();
                for (Destination d : dest) {
                    boutons.add(new Bouton(d.toString(), d.getNom()));
                }
                String choix = choisir(
                        "Vous pouvez sois supprimer, sois en garder au moins 1",
                        null,
                        boutons,
                        true);

                if (choix.equals("")) {
                    log(String.format("%s a choisi de passer", toLog()));
                    break;
                }
                for (Destination d : dest) {
                    if (d.getNom().equals(choix)) {
                        log(String.format("%s a choisi de prendre " + d, toLog()));
                        destinations.remove(d);
                        dest.remove(d);
                        jeu.getPileDestinations().add(jeu.getPileDestinations().size(), d);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {
                dest.add(jeu.getPileDestinations().remove(0));
                destinations.add(dest.get(i));
            }
            while (dest.size() > 1) {
                List<Bouton> boutons = new ArrayList<>();
                for (Destination d : dest) {
                    boutons.add(new Bouton(d.toString(), d.getNom()));
                }
                String choix = choisir(
                        "Vous pouvez supprimer jusqu'a 3 destination maximum.",
                        null,
                        boutons,
                        true);

                if (choix.equals("")) {
                    log(String.format("%s a choisi de passer", toLog()));
                    break;
                }
                for (Destination d : dest) {
                    if (d.getNom().equals(choix)) {
                        log(String.format("%s a choisi de supprimer " + d, toLog()));
                        destinations.remove(d);
                        dest.remove(d);
                        jeu.getPileDestinations().add(jeu.getPileDestinations().size(), d);
                        break;
                    }
                }
            }
        }
    }


    public boolean CapturerRouteW(String choix, ArrayList<String> tab, Route route) {
        int n = 0;
        for (CarteTransport carte : cartesTransport) {
            if ((route.getCouleur() == carte.getCouleur() && carte.getType() == TypeCarteTransport.WAGON) || carte.getType() == TypeCarteTransport.JOKER) {
                n++;
            }
        }
        ArrayList<String> tabC = new ArrayList<>();
        for (CarteTransport carte : cartesTransport) {
            tabC.add(carte.getNom());
        }
        CarteTransport cart = null;
        int NbRoutePosee = 0;
        if (n >= route.getLongueur()) {
            while (NbRoutePosee < route.getLongueur() && nbPionsWagon >= route.getLongueur()) {
                String choixPr = possibilités(tabC);
                for (CarteTransport carte : cartesTransport) {
                    if ((choixPr.equals(carte.getNom())) && ((carte.getCouleur() == route.getCouleur() && carte.getType() == TypeCarteTransport.WAGON) || carte.getType() == TypeCarteTransport.JOKER)) {
                        cartesTransportPosees.add(carte);
                        jeu.getPilesDeCartesWagon().defausser(carte);
                        cart = carte;
                        NbRoutePosee++;
                    }
                }
                if (cart != null) {
                    cartesTransport.remove(cart);
                    tabC.remove(cart.getNom());
                }
            }
            routes.add(route);
            score += route.getScore();
            nbPionsWagon -= route.getLongueur();
            return true;
        } else {
            return false;
        }
    }

    public boolean CapturerRouteB(String choix, ArrayList<String> tab, Route route) {
        int n = 0;
        for (CarteTransport carte : cartesTransport) {
            if ((route.getCouleur() == carte.getCouleur() && carte.getType() == TypeCarteTransport.BATEAU) || carte.getType() == TypeCarteTransport.JOKER) {
                n += carte.estDouble() ? 2 : 1;
            }
        }
        ArrayList<String> tabC = new ArrayList<>();
        for (CarteTransport carte : cartesTransport) {
            tabC.add(carte.getNom());
        }
        CarteTransport cart = null;

        int NbBateauPosee = 0;
        if (n >= route.getLongueur() && nbPionsBateau >= route.getLongueur()) {
            while (NbBateauPosee < route.getLongueur()) {
                String choixPr = possibilités(tabC);
                for (CarteTransport carte : cartesTransport) {
                    if ((choixPr.equals(carte.getNom())) && ((carte.getCouleur() == route.getCouleur() && carte.getType() == TypeCarteTransport.BATEAU) || carte.getType() == TypeCarteTransport.JOKER)) {
                        cartesTransportPosees.add(carte);
                        if (carte.getType() == TypeCarteTransport.JOKER) {
                            jeu.getPilesDeCartesWagon().defausser(carte);
                        } else {
                            jeu.getPilesDeCartesBateau().defausser(carte);
                        }
                        cart = carte;
                        NbBateauPosee += carte.estDouble() ? 2 : 1;
                    }
                }
                if (cart != null) {
                    cartesTransport.remove(cart);
                    tabC.remove(cart.getNom());
                }
            }
            routes.add(route);
            score += route.getScore();
            nbPionsBateau -= route.getLongueur();
            return true;
        } else {
            return false;
        }
    }

    public boolean CapturerRouteWagonGrise(String choix, ArrayList<String> tab, Route route) {
        int n = 0;
        int nbCarteCouleur = 0;
        EnumSet<Couleur> enumSet = EnumSet.allOf(Couleur.class);
        ArrayList<String> tabC = new ArrayList<>();
        for (Couleur couleur : enumSet) {
            for (CarteTransport carte : cartesTransport) {
                if ((couleur == carte.getCouleur() && carte.getType() == TypeCarteTransport.WAGON) || carte.getType() == TypeCarteTransport.JOKER) {
                    n += carte.estDouble() ? 2 : 1;
                }
            }
            if (n > nbCarteCouleur) {
                nbCarteCouleur = n;
            }
            n = 0;
        }
        for (CarteTransport carte : cartesTransport) {
            tabC.add(carte.getNom());
        }
        CarteTransport cart = null;
        Couleur couleurCarte = null;
        int NbWagonPosee = 0;
        if (nbCarteCouleur >= route.getLongueur() && nbPionsWagon >= route.getLongueur()) {
            while (NbWagonPosee < route.getLongueur()) {
                String choixPr = possibilités(tabC);
                for (CarteTransport carte : cartesTransport) {
                    if ((choixPr.equals(carte.getNom()) && ((couleurCarte == null || carte.getCouleur() == couleurCarte) && carte.getType() == TypeCarteTransport.WAGON)) || carte.getType() == TypeCarteTransport.JOKER) {
                        cartesTransportPosees.add(carte);
                        if (carte.getType() == TypeCarteTransport.JOKER) {
                            jeu.getPilesDeCartesWagon().defausser(carte);
                        } else {
                            jeu.getPilesDeCartesBateau().defausser(carte);
                            couleurCarte = carte.getCouleur();
                        }
                        cart = carte;
                        NbWagonPosee += carte.estDouble() ? 2 : 1;
                    }
                }
                if (cart != null) {
                    cartesTransport.remove(cart);
                    tabC.remove(cart.getNom());
                }
            }
            routes.add(route);
            score += route.getScore();
            nbPionsWagon -= route.getLongueur();
            return true;
        } else {
            return false;
        }
    }

    public boolean CapturerRouteBateauGrise(String choix, ArrayList<String> tab, Route route) {
        int n = 0;
        int nbCarteCouleur = 0;
        EnumSet<Couleur> enumSet = EnumSet.allOf(Couleur.class);
        ArrayList<String> tabC = new ArrayList<>();
        for (Couleur couleur : enumSet) {
            for (CarteTransport carte : cartesTransport) {
                if ((couleur == carte.getCouleur() && carte.getType() == TypeCarteTransport.BATEAU) || carte.getType() == TypeCarteTransport.JOKER) {
                    n += carte.estDouble() ? 2 : 1;
                }
            }
            if (n > nbCarteCouleur) {
                nbCarteCouleur = n;
            }
            n = 0;
        }
        for (CarteTransport carte : cartesTransport) {
            tabC.add(carte.getNom());
        }
        CarteTransport cart = null;
        Couleur couleurCarte = null;
        int NbBateauPosee = 0;
        if (nbCarteCouleur >= route.getLongueur() && nbPionsBateau >= route.getLongueur()) {
            while (NbBateauPosee < route.getLongueur()) {
                String choixPr = possibilités(tabC);
                for (CarteTransport carte : cartesTransport) {
                    if ((choixPr.equals(carte.getNom()) && ((couleurCarte == null || carte.getCouleur() == couleurCarte) && carte.getType() == TypeCarteTransport.BATEAU)) || carte.getType() == TypeCarteTransport.JOKER) {
                        cartesTransportPosees.add(carte);
                        if (carte.getType() == TypeCarteTransport.JOKER) {
                            jeu.getPilesDeCartesWagon().defausser(carte);
                        } else {
                            jeu.getPilesDeCartesBateau().defausser(carte);
                            couleurCarte = carte.getCouleur();
                        }
                        cart = carte;
                        NbBateauPosee += carte.estDouble() ? 2 : 1;
                    }
                }
                if (cart != null) {
                    cartesTransport.remove(cart);
                    tabC.remove(cart.getNom());
                }
            }
            routes.add(route);
            score += route.getScore();
            nbPionsBateau -= route.getLongueur();
            return true;
        } else {
            return false;
        }
    }

    public boolean CapturerRouteP(String choix, ArrayList<String> tab, Route route) {
        int n;
        int nbCarte2 = 0;
        EnumSet<Couleur> enumSet = EnumSet.allOf(Couleur.class);
        for (Couleur couleur : enumSet) {
            n = 0;
            for (CarteTransport carte : cartesTransport) {
                if (carte.getType() == TypeCarteTransport.WAGON && carte.getCouleur() == couleur) {
                    n++;
                }
                if (carte.getType() == TypeCarteTransport.JOKER && carte.getCouleur() == couleur) {
                    nbCarte2++;
                }
            }
            if (n >= 2) {
                nbCarte2++;
            }
        }

        ArrayList<String> tabC = new ArrayList<>();
        for (CarteTransport carte : cartesTransport) {
            tabC.add(carte.getNom());
        }

        ArrayList<Couleur> couleurs = new ArrayList<>();
        int NbWagonPosee = 0;

        CarteTransport cart = null;
        if (nbCarte2 >= route.getLongueur() && nbPionsWagon >= route.getLongueur()) {
            while (NbWagonPosee < route.getLongueur() * 2) {
                String choixPr = possibilités(tabC);
                for (CarteTransport carte : cartesTransport) {
                    if (choixPr.equals(carte.getNom()) && (((couleurs.size() < 2 || couleurs.contains(carte.getCouleur())) && carte.getType() == TypeCarteTransport.WAGON)) || carte.getType() == TypeCarteTransport.JOKER) {
                        cartesTransportPosees.add(carte);
                        if (carte.getType() == TypeCarteTransport.JOKER) {
                            jeu.getPilesDeCartesWagon().defausser(carte);
                        } else {
                            jeu.getPilesDeCartesWagon().defausser(carte);
                            couleurs.add(carte.getCouleur());
                        }
                        cart = carte;
                        NbWagonPosee++;
                    }
                }
                if (cart != null) {
                    cartesTransport.remove(cart);
                    tabC.remove(cart.getNom());
                }
                cart = null;
            }
            routes.add(route);
            score += route.getScore();
            nbPionsBateau -= route.getLongueur();
            return true;
        } else {
            return false;
        }
    }

    public void CapturerRoute(String choix, ArrayList<String> tab) {
        boolean capture = false;
        do {
            Route routeP = null;
            for (Route route : jeu.getRoutesLibres()) {
                if (Objects.equals(route.getNom(), choix)) {
                    routeP = route;
                }
            }
            if (routeP instanceof RouteTerrestre) {
                capture = CapturerRouteW(choix, tab, routeP);
                if (routeP.getCouleur() == Couleur.GRIS) {
                    capture = CapturerRouteWagonGrise(choix, tab, routeP);
                }
                if (routeP instanceof RoutePaire) {
                    capture = CapturerRouteP(choix, tab, routeP);
                }
            }
            if (routeP instanceof RouteMaritime) {
                capture = CapturerRouteB(choix, tab, routeP);
                if (routeP.getCouleur() == Couleur.GRIS) {
                    capture = CapturerRouteBateauGrise(choix, tab, routeP);
                }
            }

            if (!capture) {
                choix = possibilités(tab);
            }
        } while (!capture);
    }

    public String possibilités(ArrayList<String> tab) {
        String choix = choisir(
                "Quel carte voulez vous jouer",
                tab,
                null,
                true);
        return choix;
    }

    private Ville trouverPorts(String port){
        for(Ville ville : jeu.getPortsLibres()){
            if(ville.getNom().equals(port)){
                return ville;
            }
        }
        return null;
    }

    private Boolean possibleConstruirePorts(String port) {
        CarteTransport carte1 = null;
        CarteTransport carte2 = null;
        CarteTransport carte3 = null;
        CarteTransport carte4 = null;
        Ville portChoix = trouverPorts(port);

        if (ports.size() == 3) {
            return false;
        }

        for (Couleur couleur1 : Couleur.values()) {
            for (CarteTransport carte : cartesTransport) {
                if (carte.getAncre() && Objects.equals(carte.getCouleur(), couleur1)) {
                    carte1 = carte;
                    break;
                }
            }
            if (carte1 != null) {
                for (CarteTransport carte : cartesTransport) {
                    if (carte.getAncre() && (Objects.equals(carte.getType(), carte1.getType()) || Objects.equals(carte.getType(), TypeCarteTransport.JOKER)) && (Objects.equals(carte.getCouleur(), carte1.getCouleur()) || Objects.equals(carte.getCouleur(), Couleur.GRIS) && !Objects.equals(carte1, carte))) {
                        carte2 = carte;
                        break;
                    }

                }
                if (carte2 != null) {
                    for (CarteTransport carte : cartesTransport) {
                        if (carte.getAncre() && !Objects.equals(carte.getType(), carte1.getType()) && (Objects.equals(carte.getCouleur(), carte1.getCouleur()) || Objects.equals(carte.getCouleur(), Couleur.GRIS))) {
                            carte3 = carte;
                            break;
                        }
                    }
                    if (carte3 != null) {
                        for (CarteTransport carte : cartesTransport) {
                            if (carte.getAncre() && !Objects.equals(carte.getType(), carte1.getType()) && (Objects.equals(carte.getCouleur(), carte1.getCouleur()) || Objects.equals(carte.getCouleur(), Couleur.GRIS))) {
                                carte4 = carte;
                                break;
                            }
                        }

                    }

                }
            }
            if(carte4 != null){
                break;
            }
        }
        for(Route route : routes ){
        if(jeu.getPortsLibres().contains(portChoix) && (||)){


        }
    }
    public void ConstruirePort(String choix, Ville villes) {
//
//        List<CarteTransport> cartes = new ArrayList<>();
//        Map<Couleur, Integer> nbBateau = new HashMap<>();
//        Map<Couleur, Integer> nbWagon = new HashMap<>();
//
//
//        if (ports.size()==3) {
//            log(String.format("%s Vous n'avez pas assez de pions ports.", toLog()));
//        }
//
//        for (CarteTransport carte : cartes) {
//
//            if (carte.getType() == TypeCarteTransport.WAGON && carte.getType() != TypeCarteTransport.JOKER) {
//                nbWagon.put(carte.getCouleur(), nbWagon.getOrDefault(carte.getCouleur(), 0) + 1);
//            } else if (carte.getType() == TypeCarteTransport.BATEAU && carte.getType() != TypeCarteTransport.JOKER) {
//                nbBateau.put(carte.getCouleur(), nbBateau.getOrDefault(carte.getCouleur(), 0) + 1);
//                // récupérer les cartes triées par les HashMap
//                List<CarteTransport> cartesTriees = trierCartes(nbWagon, nbBateau, cartes);
//                cartesTransportPosees.addAll(cartesTriees);
//                nbBateau.remove(carte);
//
//
//            }
//
//
//        }
//        for (CarteTransport carte : cartes) {
//            if (nbBateau.getOrDefault(couleur, 0) >= 2 && nbWagon.getOrDefault(couleur, 0) >= 2 && carte.getType() != TypeCarteTransport.JOKER ) {
//
//
//
//            } else {
//                log(String.format("%s Vous n'avez pas assez de cartes pour construire un port.", toLog()));
//            }
//        }
//    }
//
//    private List<CarteTransport> trierCartes(Map<Couleur, Integer> nbWagon, Map<Couleur, Integer> nbBateau, List<CarteTransport> cartes) {
//        return cartes;
    }




    /**
     * Cette méthode est appelée à tour de rôle pour chacun des joueurs de la partie.
     * Elle doit réaliser un tour de jeu, pendant lequel le joueur a le choix entre 5 actions possibles :
     *  - piocher des cartes transport (visibles ou dans la pioche)
     *  - échanger des pions wagons ou bateau
     *  - prendre de nouvelles destinations
     *  - capturer une route
     *  - construire un port.
     */
    public void jouerTour() {
        ArrayList<String> pions = new ArrayList<>();
        int n = Math.min(nbPionsBateauEnReserve, nbPionsWagon);
        if(n > 0) {
            pions.add("PIONS BATEAU");
        }
        n = Math.min(nbPionsWagonEnReserve, nbPionsBateau);
        if(n > 0) {
            pions.add("PIONS WAGON");
        }

        ArrayList<String> destination = new ArrayList<>();
        for(int i = 0; i < jeu.getPileDestinations().size(); i++) {
            destination.add("DESTINATION");
        }

        ArrayList<String> piocherCarte = new ArrayList<>();
        piocherCarte.add("WAGON");
        piocherCarte.add("BATEAU");
        for(CarteTransport carte : jeu.getCartesTransportVisibles()) {
            piocherCarte.add(carte.getNom());
        }

        ArrayList<String> CapturerRoute = new ArrayList<>();
        for(Route route : jeu.getRoutesLibres()) {
            CapturerRoute.add(route.getNom());
        }

        ArrayList<String> All = new ArrayList<>();
        All.addAll(pions);
        All.addAll(destination);
        All.addAll(piocherCarte);
        All.addAll(CapturerRoute);

        String choix = choisir(
                "Quelle action souhaitez-vous faire ?",
                All,
                null,
                true);

        if(pions.contains(choix)) {
            EchangerPions(choix);
        } if(destination.contains(choix)) {
            PrendreNvDestinations();
        } if(piocherCarte.contains(choix)) {
            PiocherCarteTransport(piocherCarte,choix);
        } if(CapturerRoute.contains(choix)) {
            CapturerRoute(choix,CapturerRoute);
        }if(ConstruirePort.contains(choix)) {
            ConstruirePort(choix,ConstruirePort);
        }
    }

    /**
     * Attend une entrée de la part du joueur (au clavier ou sur la websocket) et
     * renvoie le choix du joueur.
     *
     * Cette méthode lit les entrées du jeu (`Jeu.lireligne()`) jusqu'à ce
     * qu'un choix valide (un élément de `choix` ou de `boutons` ou
     * éventuellement la chaîne vide si l'utilisateur est autorisé à passer) soit
     * reçu.
     * Lorsqu'un choix valide est obtenu, il est renvoyé par la fonction.
     *
     * Exemple d'utilisation pour demander à un joueur de répondre à une question
     * par "oui" ou "non" :
     *
     * ```
     * List<String> choix = Arrays.asList("Oui", "Non");
     * String input = choisir("Voulez-vous faire ceci ?", choix, null, false);
     * ```
     *
     * Si par contre on voulait proposer les réponses à l'aide de boutons, on
     * pourrait utiliser :
     *
     * ```
     * List<Bouton> boutons = Arrays.asList(new Bouton("Un", "1"), new Bouton("Deux", "2"), new Bouton("Trois", "3"));
     * String input = choisir("Choisissez un nombre.", null, boutons, false);
     * ```
     *
     * @param instruction message à afficher à l'écran pour indiquer au joueur la
     *                    nature du choix qui est attendu
     * @param choix       une collection de chaînes de caractères correspondant aux
     *                    choix valides attendus du joueur
     * @param boutons     une collection de `Bouton` représentés par deux String (label,
     *                    valeur) correspondant aux choix valides attendus du joueur
     *                    qui doivent être représentés par des boutons sur
     *                    l'interface graphique (le label est affiché sur le bouton,
     *                    la valeur est ce qui est envoyé au jeu quand le bouton est
     *                    cliqué)
     * @param peutPasser  booléen indiquant si le joueur a le droit de passer sans
     *                    faire de choix. S'il est autorisé à passer, c'est la
     *                    chaîne de caractères vide ("") qui signifie qu'il désire
     *                    passer.
     * @return le choix de l'utilisateur (un élement de `choix`, ou la valeur
     * d'un élément de `boutons` ou la chaîne vide)
     */
    public String choisir(
            String instruction,
            Collection<String> choix,
            Collection<Bouton> boutons,
            boolean peutPasser) {
        if (choix == null)
            choix = new ArrayList<>();
        if (boutons == null)
            boutons = new ArrayList<>();

        HashSet<String> choixDistincts = new HashSet<>(choix);
        choixDistincts.addAll(boutons.stream().map(Bouton::valeur).toList());
        if (peutPasser || choixDistincts.isEmpty()) {
            choixDistincts.add("");
        }

        String entree;
        // Lit l'entrée de l'utilisateur jusqu'à obtenir un choix valide
        while (true) {
            jeu.prompt(instruction, boutons, peutPasser);
            entree = jeu.lireLigne();
            // si une réponse valide est obtenue, elle est renvoyée
            if (choixDistincts.contains(entree)) {
                return entree;
            }
        }
    }

    /**
     * Affiche un message dans le log du jeu (visible sur l'interface graphique)
     *
     * @param message le message à afficher (peut contenir des balises html pour la
     *                mise en forme)
     */
    public void log(String message) {
        jeu.log(message);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("=== %s (%d pts) ===", nom, score));
        joiner.add(String.format("  Wagons: %d  Bateaux: %d", nbPionsWagon, nbPionsBateau));
        return joiner.toString();
    }

    /**
     * @return une chaîne de caractères contenant le nom du joueur, avec des balises
     * HTML pour être mis en forme dans le log
     */
    public String toLog() {
        return String.format("<span class=\"joueur\">%s</span>", nom);
    }

    boolean destinationEstComplete(Destination d) {
        // Cette méthode pour l'instant renvoie false pour que le jeu puisse s'exécuter.
        // À vous de modifier le corps de cette fonction pour qu'elle retourne la valeur attendue.
        return false;
    }

    public int calculerScoreFinal() {
        throw new RuntimeException("Méthode pas encore implémentée !");
    }

    /**
     * Renvoie une représentation du joueur sous la forme d'un dictionnaire de
     * valeurs sérialisables
     * (qui sera converti en JSON pour l'envoyer à l'interface graphique)
     */
    Map<String, Object> dataMap() {
        return Map.ofEntries(
                Map.entry("nom", nom),
                Map.entry("couleur", couleur),
                Map.entry("score", score),
                Map.entry("pionsWagon", nbPionsWagon),
                Map.entry("pionsWagonReserve", nbPionsWagonEnReserve),
                Map.entry("pionsBateau", nbPionsBateau),
                Map.entry("pionsBateauReserve", nbPionsBateauEnReserve),
                Map.entry("destinationsIncompletes",
                        destinations.stream().filter(d -> !destinationEstComplete(d)).toList()),
                Map.entry("destinationsCompletes", destinations.stream().filter(this::destinationEstComplete).toList()),
                Map.entry("main", cartesTransport.stream().sorted().toList()),
                Map.entry("inPlay", cartesTransportPosees.stream().sorted().toList()),
                Map.entry("ports", ports.stream().map(Ville::nom).toList()),
                Map.entry("routes", routes.stream().map(Route::getNom).toList()));
    }
}
