import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Object renomeur
 * 
 * @author justin
 * 
 */

public class Renomeur {
	File f;
	String origine;
	boolean estFic;
	String titre;
	String date;
	String qualite;
	String ext;
	String nomFinal;
	Pattern p;
	Matcher m;
	private boolean dateTrouve;
	private boolean qualiteTrouve;
	int indexDate;
	int indexQualite;
	boolean changement;
	Pattern pQualiteC = Pattern.compile("\\[[0-9]{3,4}p\\]");
	Pattern pQualiteD = Pattern.compile("\\.?[0-9]{3,4}p");
	Pattern pDateC = Pattern.compile("\\[[0-9]{4}\\]");
	Pattern pDateD = Pattern.compile("\\.[0-9]{4}\\.");

	public Renomeur(File fic) {
		this.f = fic;
		this.origine = fic.getName();
		this.nomFinal = this.origine;
		this.estFic = fic.isFile();
		trouverParams();

	}

	private void trouverParams() {
		if (estFic) {
			trouverExt();
		}
		nettoyerEspaces();
		trouverQualite();
		trouverDate();
		trouverTitre();
		formatter();
	}

	private void nettoyerEspaces() {
		this.origine = this.origine.replace(" ", ".");
		this.origine = this.origine.replace("(", "[");
		this.origine = this.origine.replace(")", "]");
	}

	private void trouverQualite() {
		p = pQualiteC;
		// p = Pattern.compile("\\[[0-9]{3,4}p\\]");
		m = p.matcher(this.origine);
		if (m.find()) {
			this.qualiteTrouve = true;
			this.qualite = this.origine.substring(m.start(), m.end());
			this.indexQualite = m.start();
		} else {
			p = pQualiteD;
			// p = Pattern.compile("\\.?[0-9]{3,4}p");
			m = p.matcher(this.origine);
			if (m.find()) {
				this.qualiteTrouve = true;
				this.qualite = ("["
						+ this.origine.substring(m.start(), m.end()) + "]")
						.replace(".", "");
				this.indexQualite = m.start();
			}
		}
	}

	private void trouverDate() {
		p = pDateC;
		// p = Pattern.compile("\\[[0-9]{4}\\]"); // [2001]
		m = p.matcher(this.origine);
		if (m.find()) {
			this.dateTrouve = true;
			this.date = this.origine.substring(m.start(), m.end());
			this.indexDate = m.start();
		} else {
			p = pDateD;
			// p = Pattern.compile("\\.[0-9]{4}\\."); // .2001.
			m = p.matcher(this.origine);
			if (m.find()) {
				this.dateTrouve = true;
				this.date = "["
						+ this.origine
								.substring((m.start() + 1), (m.end() - 1))
						+ "]";
				this.indexDate = m.start();
			}
		}
	}

	private void trouverTitre() {
		if (dateTrouve) {
			this.titre = origine.substring(0, indexDate);
		} else if (qualiteTrouve) {
			this.titre = origine.substring(0, indexQualite);
		} else if (estFic) {
			this.titre = origine.substring(0, origine.length() - ext.length());
		} else {
			this.titre = origine;
		}
		if (this.titre.endsWith(".")) {
			this.titre = titre.substring(0, titre.length() - 1);
		}
	}

	private void trouverExt() {
		this.ext = this.origine.substring(this.origine.lastIndexOf('.'));

	}

	private void formatter() {
		nomFinal = this.titre;
		if (dateTrouve)
			nomFinal += this.date;
		if (qualiteTrouve)
			nomFinal += this.qualite;
		if (estFic)
			nomFinal += this.ext;
		if (!nomFinal.equals(origine))
			changement = true;
	}

	public boolean getChangement() {
		return changement;
	}

	public String getNom() {
		return nomFinal;
	}
}