package compteur_argent;
import compteur_argent.vue.*;
import compteur_argent.metier.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main
{
	public static final double SMIC_HEURE = 11.07;
	public static final double SMIC_SECONDE = 0.003075;
	public static final String NIVEAU_DOSSIER = ""; /* "../"; // pour tester sur vscode c'est pas le même niveau*/

	private Fenetre vue;
	private Argent metier;

	private PanelInfos panelInfos;

	private double pourcentageSMIC = 0;
	private double nbHeuresSemaine = 0;
	private double cumulSemaine = 0, cumulJour = 0;

	private ArrayList<Cours> listeCours = null;

	private Main()
	{
		this.metier = new Argent(this);

		this.initialiserCalendrier();

		this.vue = new Fenetre(this);
	}

	public void initialiserCalendrier()
	{
		if(this.listeCours == null)
		{
			ImporteurCalendrier ic = new ImporteurCalendrier(this);
			this.listeCours = ic.getListeCours();
		}
	}

	public void demarrerCompteur()
	{
		this.metier.demarrerCompteur();
	}

	public void majEmploiDuTemps()
	{
		this.vue.majEmploiDuTemps();
	}

	public void setPanelInfos(PanelInfos panelInfos)
	{
		this.panelInfos = panelInfos;
	}

	public void popUpErreur(String message)
	{
		message = (message == null ? "erreur" : message);

		if(this.vue != null)
		{
			this.vue.popUpErreur(message);
		}
		else
		{
			System.out.println(message);
		}
	}

	public void popUpErreuriCal()
	{
		JOptionPane.showMessageDialog(new JFrame(), "Impossible de télécharger le fichier iCal", "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	public void sauvegarderDonnees()
	{
		this.metier.sauvegarderDonnees();
	}

	/*************************************/
	/***************GETTERS***************/
	/*************************************/
	
	public double getPourcentageSMIC() 		{ return this.pourcentageSMIC; }
	public double getNbHeuresSemaine() 		{ return this.nbHeuresSemaine; }
	public double getCumulSemaine() 		{ return this.cumulSemaine; }
	public double getCumulJour() 			{ return this.cumulJour; }
	public ArrayList<Cours> getListeCours() { return this.listeCours; }

	/*************************************/
	/***************SETTERS***************/
	/*************************************/
	
	public void setPourcentageSMIC(double n) 				{ this.pourcentageSMIC = n; }
	public void setNbHeuresSemaine(double n) 				{ this.nbHeuresSemaine = n; }
	public synchronized void setCumulSemaine   (double n) 				{ this.cumulSemaine = n; this.panelInfos.setCumulSemaine(n);}
	public synchronized void setCumulJour      (double n) 				{ this.cumulJour = n; this.panelInfos.setCumulJour(n);}
	public synchronized void setListeCours     (ArrayList<Cours> list) 	{ this.listeCours = list; }

    public static void main(String[] args)
	{
        new Main();
    }
}