package compteur_argent.metier;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import compteur_argent.Main;

public class ImporteurCalendrier
{
	private final String urlICal = "https://hplanning.univ-lehavre.fr/Telechargements/ical/Edt_INFOL2.ics?version=2022.0.4.3&idICal=5BCC7F454F0165E5594AC469798EE81D";

	private ArrayList<Cours> listeCours;

	public ImporteurCalendrier(Main ctrl)
	{
		try
		{
			InputStream in = new URL(urlICal).openStream();

			Files.copy(in, Paths.get(Main.NIVEAU_DOSSIER+"tmp/iCal.ics"), StandardCopyOption.REPLACE_EXISTING);

			Scanner sc = new Scanner(new File(Main.NIVEAU_DOSSIER+"tmp/iCal.ics"), "UTF-8");
			String intitule = null, dateDebut = null, dateFin = null;
			boolean estFerier = false;
			listeCours = new ArrayList<Cours>();
			while(sc.hasNextLine())
			{
				String line = sc.nextLine();
				if(line.equals("BEGIN:VEVENT"))
				{
					intitule = "";
					dateDebut = "";
					dateFin = "";
					estFerier = false;
				}

				if(line.equals("END:VEVENT"))
				{
					listeCours.add(new Cours(intitule, dateDebut, dateFin,estFerier));
				}

				String cle = line.split(":")[0];
				String val = line.split(":")[1];

				if(cle.contains("SUMMARY"))
				{
					intitule = val.replace("\\", "");
				}

				if(cle.equals("DTSTART"))
				{
					dateDebut = val;
				}

				if(cle.equals("DTEND"))
				{
					dateFin = val;
				}

				if(cle.equals("DTSTART;VALUE=DATE"))
				{
					dateDebut = val;
					dateFin = val;
					estFerier = true;
				}
			}

			// on ne garde que les cours de cette semaine
			ArrayList<Cours> aSupprimer = new ArrayList<Cours>();
			Date instant = Date.from(LocalDateTime.now(ZoneId.systemDefault()).toInstant(ZoneOffset.UTC));
			int semaine = Integer.parseInt(new SimpleDateFormat("ww").format(instant));

			for(Cours c : listeCours)
			{
				if(c.getSemaine() != semaine)
				{
					aSupprimer.add(c);
				}
			}

			for(Cours c : aSupprimer) {listeCours.remove(c);}
			Collections.sort(listeCours);

			ctrl.setListeCours(listeCours);
		}
		catch (Exception e)
		{
			ctrl.popUpErreuriCal();
			e.printStackTrace();
		}
	}

	public ArrayList<Cours> getListeCours() {
		return listeCours;
	}
}
