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

    public void setNbPionsBateauEnReserve(int nbPionsBateauEnReserve) {
        this.nbPionsBateauEnReserve = nbPionsBateauEnReserve;
    }

    public String getNom() {
        return nom;
    }

    public CarteTransport piocherCarteTransportDansPile() {
        List<Bouton> boutons = Arrays.asList(
                new Bouton("Piocher dans la pile Bateaux"),
                new Bouton("Piocher dans la pile Wagons"));

        String choix = choisir(
                "Dans quel pioche voulez vous tirer la carte ?",
                null,
                boutons,
                false);

        if(choix.equals(boutons.get(0))) {
            return jeu.piocherCarteBateau();
        } else {
            return jeu.piocherCarteWagon();
        }
    }

    public CarteTransport piocherCarteTransportDansCarteVisible() {
        List<Bouton> boutons = new ArrayList<>();
        for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
            boutons.add(new Bouton(carte.toString()));
        }

        String choix = choisir(
                "Quel carte voulez vous piocher",
                null,
                boutons,
                false);

        for(CarteTransport carte : jeu.getCartesTransportVisibles()) {
            if(choix.equals(carte)) {
                cartesTransport.add(carte);
                piocherCarteTransportDansPile();
                return carte;
            }
        }
    }


    public void PiocherCarteTransport() {
        List<Bouton> boutons = Arrays.asList(
                new Bouton("piocher dans les cartes visibles"),
                new Bouton("Piocher dans la pile Wagons"),
                new Bouton("Piocher dans la pile Bateaux"));

        String choix = choisir(
                "Ou voulez-vous piocher",
                null,
                boutons,
                false);

        if(choix.equals(boutons.get(0))) {
            List<Bouton> bouton = new ArrayList<>();
            for (CarteTransport carte : jeu.getCartesTransportVisibles()) {
                bouton.add(new Bouton(carte.toString()));
            }
            choix = choisir(
                    "Quel carte voulez vous piocher",
                    null,
                    boutons,
                    false);

        }
        if(choix.equals(boutons.get(1))) {

        } else {

        }
    }

    public void EchangerPionsWagonsBateau() {
        /*List<Bouton> boutons = new ArrayList<>();
        for(int i = 0; i < 2; i ++) {
            boutons.add(new Bouton("Prendre des pions Wagons contre des Bateaux",String.valueOf(i)));
        }

        String choix = choisir(
                "Quel pions voulez vous échanger",
                null,
                boutons,
                false);

        //log(String.format("%s n'aime aucune ville " + choix, toLog()));
        if(choix.equals(0)) {
            ArrayList<String> nbPionsWagonsEchanger = new ArrayList<>();
            if (nbPionsWagonEnReserve < nbPionsBateau) {
                for (int i = 1; i <= nbPionsWagonEnReserve; i++) {
                    nbPionsWagonsEchanger.add(String.valueOf(i));
                }
            } else {
                for (int i = 1; i <= nbPionsBateau; i++) {
                    nbPionsWagonsEchanger.add(String.valueOf(i));
                }
            }
            List<Bouton> bouton = new ArrayList<>();
            for (String p : nbPionsWagonsEchanger) {
                boutons.add(new Bouton(p));
            }

            String choixPions = choisir(
                    "Combien de pions voulez vous échanger ?",
                    null,
                    bouton,
                    false);

            for (String p : nbPionsWagonsEchanger) {
                if (p.equals(choixPions)) {
                    int n = Integer.parseInt(choixPions);
                    nbPionsWagon += n;
                    nbPionsBateau -= n;
                    nbPionsWagonEnReserve -= n;
                    nbPionsBateauEnReserve += n;
                }
            }
        } else {
            ArrayList<String> nbPionsBateauxEchanger = new ArrayList<>();
            if (nbPionsBateauEnReserve < nbPionsWagon) {
                for (int i = 1; i <= nbPionsWagonEnReserve; i++) {
                    nbPionsBateauxEchanger.add(String.valueOf(i));
                }
            } else {
                for (int i = 1; i <= nbPionsWagon; i++) {
                    nbPionsBateauxEchanger.add(String.valueOf(i));
                }
            }
            List<Bouton> bouton = new ArrayList<>();
            for (String p : nbPionsBateauxEchanger) {
                boutons.add(new Bouton(p));
            }

            String choixPionsBateau = choisir(
                    "Combien de pions voulez vous échanger ?",
                    null,
                    bouton,
                    false);

            for (String p : nbPionsBateauxEchanger) {
                if (p.equals(choixPionsBateau)) {
                    int n = Integer.parseInt(choixPionsBateau);
                    nbPionsWagon -= n;
                    nbPionsBateau += n;
                    nbPionsWagonEnReserve += n;
                    nbPionsBateauEnReserve -= n;
                }
            }
        }*/
    }

    public void PrendreNvDestinations() {
        List<Destination> dest = new ArrayList<>();
        if (jeu.getPileDestinations().isEmpty()) {
            log(String.format("%s désolé, la pioche destination est vide", toLog()));
        }
        if (jeu.getPileDestinations().size() < 4) {
            for (int i = 0; i < jeu.getPileDestinations().size(); i++) {
                dest.add(jeu.getPileDestinations().remove(0));
            }
            boolean peutpasser = false;
            while (dest.size() > 0) {
                if (dest.size() != 4) {
                    peutpasser = true;
                }
                List<Bouton> boutons = new ArrayList<>();
                for (Destination d : dest) {
                    boutons.add(new Bouton(d.toString(), d.getNom()));
                }
                String choix = choisir(
                        "Prenez au moins 1 destinations",
                        null,
                        boutons,
                        peutpasser);

                if (choix.equals("")) {
                    log(String.format("%s a choisi de passer", toLog()));
                    break;
                }
                for (Destination d : dest) {
                    if (d.getNom().equals(choix)) {
                        log(String.format("%s a choisi de prendre " + d, toLog()));
                        destinations.add(d);
                        dest.remove(d);
                        jeu.getPileDestinations().add(jeu.getPileDestinations().size() - 1, d);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {
                dest.add(jeu.getPileDestinations().remove(0));
            }
            boolean peutpasser = false;
            while (dest.size() > 0) {
                if (dest.size() != 4) {
                    peutpasser = true;
                }
                List<Bouton> boutons = new ArrayList<>();
                for (Destination d : dest) {
                    boutons.add(new Bouton(d.toString(), d.getNom()));
                }
                String choix = choisir(
                        "Prenez au moins 1 destinations",
                        null,
                        boutons,
                        peutpasser);

                if (choix.equals("")) {
                    log(String.format("%s a choisi de passer", toLog()));
                    break;
                }
                for (Destination d : dest) {
                    if (d.getNom().equals(choix)) {
                        log(String.format("%s a choisi de prendre " + d, toLog()));
                        destinations.add(d);
                        dest.remove(d);
                        jeu.getPileDestinations().add(jeu.getPileDestinations().size() - 1, d);
                        break;
                    }
                }
            }
        }
    }

    public void CapturerRoute() {
    }

    public void ConstruirePort() {
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
    void jouerTour() {
        List<Bouton> boutons = Arrays.asList(
                new Bouton("piocher des cartes transport"),
                new Bouton("échanger des pions wagons ou bateau"),
                new Bouton("DESTINATION"),
                new Bouton("capturer une route"),
                new Bouton("construire un port"));


        String choix = choisir(
                "Quelle action souhaitez-vous faire ?",
                null,
                boutons,
                true);

        if (choix.equals("")) {
            log(String.format("%s n'aime aucune ville", toLog()));
        } else {
            log(String.format("%s a choisi %s", toLog(), choix));
        }
        if(choix.equals("DESTINATION")) {
            PrendreNvDestinations();
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