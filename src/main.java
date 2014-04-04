import java.io.File;
import java.util.ArrayList;

/**
 * Entretien 3000
 * @author Justin Duplessis
 * @version 0.10
 * 
 * Programme qui classe et renomme les films d'un dossier racine.
 * Fait avec amour.
 */

public class main {
	
	public static final int TAILLE_MIN_FILM = 500000000;
	
	public static int nbDos = 0;
	public static int nbFilms = 0;
	public static int nbOrphelins = 0;
	
	public static int nbMods = 0;
	
	public static int nbErreursFilms = 0;
	public static int nbErreursDos = 0;
	public static int nbErreursOrp = 0;
	public static ArrayList<File> ficCumulatif = new ArrayList<File>();
	public static ArrayList<File> dosCumulatif = new ArrayList<File>();
	
	public static void main(String[] args) {
		System.out.println("######################");
		System.out.println("####Entretien 3000####");
		System.out.println("######################");
		System.out.println("######ver 0.1.0#######");
		System.out.println("######################");
		executionJar(args);
	}
	
	public static void executionJar(String[] args) {
		
		if (args.length == 1) {
			File f = new File(args[0]);
			
			if (f.exists()) {
				// lancer le programme
				debutAlgo(args[0]);
			} else {
				System.out.println("Le chemin spécifié n'existe pas !");
				System.out.println(args[0]);
				System.out.println("(Don't be upset)");
			}
			
		} else {
			System.out.println("Veuillez rentrer un paramètre (chemin entre \"\")");
			System.out.println("Vous avez entré " + args.length + "paramètres:");
			for(int i =0;i<args.length;i++){
				System.out.println(i + ":" +args[i]);
			}
			System.out.println("(Don't be upset)");
		}
		
	}

	/**
	 * Le début de l'algorithme comme tel.
	 * L'ordre de modification est très logique et il serait difficile de 
	 * combiner les étapes sans obtenir des fichiers oubliés.
	 * 
	 * TODO Faire en une étape sans oublier de fichiers et garder l'impact sur la mémoire bas (gl)
	 * 
	 * @param racine La racine à analyser
	 */
	public static void debutAlgo(String racine) {
		int nbModsPre = 0;
		long temps = System.currentTimeMillis();
		
		System.out.println("\n##Analyse de la racine " + racine + "##");
		
		//renommer les fichiers
		System.out.println("\n###Analyse et construction des noms de fichiers... ");
		renommerFilms(racine);
		
		System.out.print("#" + nbFilms + " films, " + nbMods + " modification(s)");
		if(nbErreursFilms != 0){
			System.out.print(", " + nbErreursFilms + " erreur(s) !");
		}
		System.out.println("#");
		nbModsPre = nbMods;
		
		// renommer les dossiers
		System.out.println("\n###Analyse et construction des noms de dossiers...");
		renommerDossiers(racine);
		
		System.out.print("#" + nbDos + " dossiers, " + (nbMods - nbModsPre) + " modification(s)");
		if(nbErreursDos != 0){
			System.out.print(", " + nbErreursDos + " erreur(s) !");
		}
		System.out.println("#");
		nbModsPre = nbMods;
		
		// créer les nouveaux dossiers
		System.out.println("\n###Création des nouveaux dossiers et déplacments...");
		securiserOrphelins(racine);
		System.out.print("#" + nbOrphelins + " fichiers orphelins, " + (nbMods - nbModsPre) + " modification(s)");
		if(nbErreursOrp != 0){
			System.out.print(", " + nbErreursOrp + " erreur(s) !");
		}
		System.out.println("#");
		System.out.println();
		
		int nbErreurs = nbErreursDos + nbErreursFilms + nbErreursOrp;
		if(nbErreurs >0){
			System.out.println("" + nbErreurs + " ERREURS lors de l'exécution, vérifiez les doublons possibles et les droits d'écriture!");
		}
		System.out.println("Exécuté en " + (System.currentTimeMillis() - temps) + "ms");
		
	}
	
	private static void renommerDossiers(String racine) {
		
		trouverDossiers(racine);
		Renomeur r;
		
		for (File f : dosCumulatif) {

			nbDos++;
			r = new Renomeur(f);
			File nouveauDos = new File(f.getParent(), r.getNom());

			if (r.changement) {
				if (nouveauDos.exists()) {
					System.out.println("Erreur, le dossier " + nouveauDos.getAbsolutePath() + " existe déjà !");
					nbErreursDos++;
				} else {
					nbMods++;
					f.renameTo(nouveauDos);
				}
			}
		}
	}

	private static void trouverDossiers(String chemin) {
		
		File racine = new File(chemin);
		File[] listeDosRacine = racine.listFiles();
		
		for (File f : listeDosRacine){
			if(f.isDirectory()){
				dosCumulatif.add(f);
			}else if(f.isDirectory()){
				trouverDossiers(f.getAbsolutePath());
			}
		}
		
	}

	/**
	 * L'appel de cette méthode fait les appels nécessaires pour trouver les 
	 * films et applique le traitement de la classe Renommeur
	 * @param racine Racine du dossier
	 */
	
	private static void renommerFilms(String racine) {
		
		trouverFics(racine);
		Renomeur r;
		
		for (File f : ficCumulatif) {

			nbFilms++;
			r = new Renomeur(f);
			File nouveauFic = new File(f.getParent(), r.getNom());

			if (r.changement) {
				if (nouveauFic.exists()) {
					System.out.println("Erreur, le fichier " + nouveauFic.getAbsolutePath() + " existe déjà !");
					nbErreursFilms++;
				} else {
					nbMods++;
					f.renameTo(nouveauFic);
				}
			}
		}
	}

	/**
	 * Algorithme récursif qui cherche les fichiers
	 * Les fichiers qui respectent les critères seront dans ficCumulatif
	 * @param chemin le chemin a analyser
	 */
	
	private static void trouverFics(String chemin){
		
		File racine = new File(chemin);
		File[] listeFicsRacineCourante = racine.listFiles();
		
		for (File f : listeFicsRacineCourante){
			if(f.isFile() && f.length() >= TAILLE_MIN_FILM){
				ficCumulatif.add(f);
			}else if(f.isDirectory()){
				trouverFics(f.getAbsolutePath());
			}
		}
		
	}
	
	/**
	 * Algorithme qui cherche les films qui sont à la racine sans dossier.
	 * Un dossier avec le nom du film sera créé pour chaque fichier trouvé
	 * @param racine racine
	 */
	
	public static void securiserOrphelins(String racine){
		
		File repRacine = new File(racine);
		File dir;

		for (File fic : repRacine.listFiles()) {
			if (fic.isFile() && fic.getName().contains(".") && fic.length() >= TAILLE_MIN_FILM) {
				nbOrphelins++;
				dir = new File(repRacine, fic.getName().substring(0,fic.getName().lastIndexOf(".")));
				File nouveauFic = new File(dir, fic.getName());
				if(nouveauFic.exists()){
					System.out.println("Erreur, le fichier " + dir.getAbsolutePath() + " existe déjà !");
					nbErreursOrp++;
				}else{
					dir.mkdir();
					fic.renameTo(nouveauFic);
					nbMods++;
				}
			}
		}
		
	}

}
