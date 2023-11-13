package compteur_argent.metier;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class Cours implements Comparable<Cours>
{
	private String intitule;
	private Date dateDebut;
	private Date dateFin;
	private boolean estFerier;
	private double valeur;

	public Cours(String intitule, String dateDebut, String dateFin, boolean estFerier) 
	{
		this.intitule = new String(intitule.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
		this.estFerier = estFerier;
		this.dateDebut = convertirDate(dateDebut);
		this.dateFin = convertirDate(dateFin);
		this.valeur=0;
	}

	public String getIntitule() { return this.intitule; }
	public Date getDateDebut() { return this.dateDebut; }
	public Date getDateFin() { return this.dateFin; }

	public int getAnnee() 
	{
		return Integer.parseInt(new SimpleDateFormat("yyyy").format(dateDebut));
	}

	public int getMois() 
	{
		return Integer.parseInt(new SimpleDateFormat("MM").format(dateDebut));
	}

	public int getSemaine()
	{
		return Integer.parseInt(new SimpleDateFormat("ww").format(dateDebut));
	}

	public int getJour() 
	{
		return Integer.parseInt(new SimpleDateFormat("dd").format(dateDebut));
	}

	public int getJourSemaine() 
	{
		return Integer.parseInt(new SimpleDateFormat("uu").format(dateDebut));
	}

	public int getHeureDebut() 
	{
		if(estFerier)
		{
			return 8;
		}
		return Integer.parseInt(new SimpleDateFormat("HH").format(dateDebut));
	}

	public int getMinuteDebut() 
	{
		if(estFerier)
		{
			return 0;
		}
		return Integer.parseInt(new SimpleDateFormat("mm").format(dateDebut));
	}

	public int getHeureFin() 
	{
		if(estFerier)
		{
			return 20;
		}
		return Integer.parseInt(new SimpleDateFormat("HH").format(dateFin));
	}

	public int getMinuteFin() 
	{
		if(estFerier)
		{
			return 0;
		}
		return Integer.parseInt(new SimpleDateFormat("mm").format(dateFin));
	}

	public boolean estFerier() {return estFerier;}

	public double getValeur() { return this.valeur;}
	public void setValeur(double n) { this.valeur = n;}

	public String toString()
	{
		String sRet;
		sRet = this.intitule + " : " + String.format("%02d", this.getJour()) + "/" + String.format("%02d", this.getMois()) + "/" + this.getAnnee() + 
			" de " + this.getHeureDebut() + ":" + String.format("%02d", this.getMinuteDebut()) + 
			" à " + this.getHeureFin() + ":" + String.format("%02d", this.getMinuteFin());

		return sRet;
	}

	private Date convertirDate(String date)
	{
		Date dateRet;

		if(estFerier)
		{
			String part1, part2, part3, parse;

			part1 = date.substring(0, 4);
			part2 = date.substring(4, 6);
			part3 = date.substring(6, 8);

			parse = part1 + "-" + part2 + "-" + part3;

			LocalDate local = LocalDate.parse(parse);

			dateRet = Date.from(local.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		else
		{
			String part1, part2, part3, part4, part5, parse;

			part1 = date.substring(0, 4);
			part2 = date.substring(4, 6);
			part3 = date.substring(6, 11);
			part4 = date.substring(11, 13);
			part5 = date.substring(13, 15);

			parse = part1 + "-" + part2 + "-" + part3 + ":" + part4 + ":" + part5;

			LocalDateTime local = LocalDateTime.parse(parse);

			dateRet = Date.from(local.atOffset(ZoneOffset.UTC).atZoneSameInstant(ZoneId.systemDefault()).toInstant());
		}

		return dateRet;
	}

	
	public int compareTo(Cours c2) 
	{
		return this.dateDebut.compareTo(c2.dateDebut);
	}
}
