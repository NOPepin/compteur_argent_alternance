package compteur_argent.metier;
import compteur_argent.Main;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Argent
{
	private Main ctrl;
	private Thread thread = null;
	private Cours coursActuel = null;

	public Argent(Main ctrl)
	{
		this.ctrl = ctrl;

		lireSauvegarde();
	}

	private void lireSauvegarde()
	{
		try
		{
			double pourcentageSMIC = 0;
			double nbHeuresSemaine = 0;

			Scanner s = new Scanner(new File(Main.NIVEAU_DOSSIER+"sauvegarde.txt"));
			String ligne = s.nextLine();

			pourcentageSMIC = Double.parseDouble(ligne.split(";")[0].split(":")[1]);
			nbHeuresSemaine = Double.parseDouble(ligne.split(";")[1].split(":")[1]);

			ctrl.setPourcentageSMIC(pourcentageSMIC);
			ctrl.setNbHeuresSemaine(nbHeuresSemaine);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void sauvegarderDonnees()
	{
		try 
		{
			FileWriter fw = new FileWriter(Main.NIVEAU_DOSSIER+"sauvegarde.txt");

			fw.write("pourcentageSmic:"+ctrl.getPourcentageSMIC()+";nbHeuresSemaine:"+ctrl.getNbHeuresSemaine()+"\n");
			fw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void demarrerCompteur()
	{
		double[] tabRatios = {0, 0, 0, 0, 0, 0};
		double[] tabNbHeuresJour = {0, 0, 0, 0, 0, 0};

		for(Cours c : ctrl.getListeCours())
		{
			if(!c.estFerier())
			{
				tabNbHeuresJour[c.getJourSemaine()-1] += c.getHeureFin()-c.getHeureDebut() + ((double)(c.getMinuteFin()-c.getMinuteDebut())/60);
			}
		}

		int nbJoursCours = 0;

		for (int i = 0; i < tabNbHeuresJour.length; i++)
		{
			nbJoursCours += (tabNbHeuresJour[i] != 0 ? 1 : 0);
		}

		double nbHeuresPayeesJour = ctrl.getNbHeuresSemaine() / (double)(nbJoursCours);

		for (int i = 0; i < tabNbHeuresJour.length; i++)
		{
			if(tabNbHeuresJour[i] != 0)
			{
				tabRatios[i] = nbHeuresPayeesJour / tabNbHeuresJour[i];
			}
		}

		double[] argentParHeure = {0, 0, 0, 0, 0, 0, 0};
		double[] argentParSeconde = {0, 0, 0, 0, 0, 0, 0};

		for (int i = 0; i < tabRatios.length; i++)
		{
			argentParHeure[i] = Main.SMIC_HEURE * (ctrl.getPourcentageSMIC()/100) * tabRatios[i];
			argentParSeconde[i] = Main.SMIC_SECONDE * (ctrl.getPourcentageSMIC()/100) * tabRatios[i];
		}

		// les ratios sont finis, on enregistre la valeur de chaque cours
		ArrayList<Cours> listeCours = ctrl.getListeCours();
		for (Cours c : listeCours)
		{
			double valeur;
			valeur = c.getHeureFin()-c.getHeureDebut() + ((double)(c.getMinuteFin()-c.getMinuteDebut())/60);
			valeur *= argentParHeure[c.getJourSemaine()-1];
			c.setValeur(valeur);
		}

		Date instant = Date.from(ZonedDateTime.now(ZoneId.systemDefault()).toInstant());

		double[] nbHeuresFiniesJour = {0, 0, 0, 0, 0, 0, 0};

		// on parcours les cours, si ils sont déjà finis ou si ils sont en cours on ajoute leur durée au temps de cours fini total du jour correspondant
		for(Cours c : listeCours)
		{
			if(c.getDateFin().after(instant))
			{
				if(c.getDateDebut().before(instant))
				{
					double dureeCours = Double.parseDouble(new SimpleDateFormat("HH").format(instant))-c.getHeureDebut();
					dureeCours += (Double.parseDouble(new SimpleDateFormat("mm").format(instant))-c.getMinuteDebut())/60;
					dureeCours += (Double.parseDouble(new SimpleDateFormat("ss").format(instant)))/3600;

					nbHeuresFiniesJour[c.getJourSemaine()-1] += dureeCours;
				}
				break;
			}
			double dureeCours = c.getHeureFin()-c.getHeureDebut() + ((double)(c.getMinuteFin()-c.getMinuteDebut())/60);

			nbHeuresFiniesJour[c.getJourSemaine()-1] += dureeCours;
		}

		int aujourdhui = Integer.parseInt(new SimpleDateFormat("uu").format(instant));
		double cumulSemaine = 0;
		double cumulJour = argentParHeure[aujourdhui - 1] * nbHeuresFiniesJour[aujourdhui - 1];

		for (int i = 0; i < aujourdhui; i++)
		{
			cumulSemaine += argentParHeure[i] * nbHeuresFiniesJour[i];
		}

		ctrl.setCumulSemaine(cumulSemaine);
		ctrl.setCumulJour(cumulJour);

		if(thread != null) {thread.interrupt();}

		thread = new Thread(() -> 
		{
			boolean actif = true;
			final long ATTENTE = 100;
			while(actif)
			{
				try
				{
					Thread.sleep(ATTENTE);

					Date nouvelInstant = Date.from(ZonedDateTime.now(ZoneId.systemDefault()).toInstant());
					boolean coursTrouve = false;

					for(Cours c : listeCours)
					{
						if(c.getDateDebut().before(nouvelInstant) && c.getDateFin().after(nouvelInstant))
						{
							double cumulJourAvant, cumulJourApres;

							cumulJourAvant = ctrl.getCumulJour();
							cumulJourApres = cumulJourAvant+(argentParSeconde[c.getJourSemaine()-1]/(1000/ATTENTE));
							
							ctrl.setCumulSemaine(ctrl.getCumulSemaine()+(argentParSeconde[c.getJourSemaine()-1]/(1000/ATTENTE)));
							ctrl.setCumulJour(cumulJourApres);

							if(c != coursActuel)
							{
								ctrl.majEmploiDuTemps();
							}
							
							coursTrouve = true;
							coursActuel = c;
							break;
						}
					}

					if(!coursTrouve && coursActuel != null)
					{
						ctrl.majEmploiDuTemps();
						coursActuel = null;
					}
				}
				catch (InterruptedException e)
				{
					actif = false;
				}
			}
		});
		thread.start();
	}
}
