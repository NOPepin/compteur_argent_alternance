package compteur_argent.vue;

import compteur_argent.metier.Cours;

import javax.swing.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

public class PanelHeure extends JPanel
{
	private HashMap<Cours,boolean[]> listeCours;

	private final static int[] RGB_MIN = {255, 251, 119}; // jaune
	private final static int[] RGB_MAX = {255,  20,  25}; // rouge

	public PanelHeure()
	{ 
		listeCours = new HashMap<Cours,boolean[]>();
	}

	public void remettreAZero()
	{
		listeCours.clear();
	}

	public void dessinerCours(Cours c)
	{
		boolean modeDessinDebut = true;
		boolean modeDessinFin = true;
		
		boolean[] tabModes = {modeDessinDebut, modeDessinFin};
		listeCours.put(c, tabModes);
	}

	public void dessinerDebutCours(Cours c)
	{
		boolean modeDessinDebut = true;
		boolean modeDessinFin = false;
		
		boolean[] tabModes = {modeDessinDebut, modeDessinFin};
		listeCours.put(c, tabModes);
	}

	public void remplirCase(Cours c)
	{
		boolean modeDessinDebut = false;
		boolean modeDessinFin = false;
		
		boolean[] tabModes = {modeDessinDebut, modeDessinFin};
		listeCours.put(c, tabModes);
	}

	public void dessinerFinCours(Cours c)
	{
		boolean modeDessinDebut = false;
		boolean modeDessinFin = true;
		
		boolean[] tabModes = {modeDessinDebut, modeDessinFin};
		listeCours.put(c, tabModes);
	}

	@Override
	protected void paintComponent(Graphics gOriginal)
	{
		Graphics2D g = ((Graphics2D)gOriginal.create());
		g.setColor(new Color(238,238,238));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		for (Cours cours : listeCours.keySet())
		{
			String intitule = cours.getIntitule();
			String valeur = String.format("%,10.2f€", cours.getValeur());

			intitule = cours.getIntitule().split(" - ")[0];
			intitule = intitule.substring(5, intitule.length());

			Date instant = Date.from(ZonedDateTime.now(ZoneId.systemDefault()).toInstant());
			Color couleurFond;

			if(cours.getDateDebut().before(instant) && cours.getDateFin().after(instant)) // cours actuel
			{
				couleurFond = new Color(152,192,84); // vert
			}
			else if (cours.getDateDebut().before(instant) && cours.getDateFin().before(instant)) // cours fini
			{
				couleurFond = new Color(184, 180, 180); // gris
			}
			else // cours à venir
			{
				int coulR, coulG, coulB;
				double ratio = cours.getValeur()/50.0;

				coulR = (int)((ratio*(PanelHeure.RGB_MAX[0]-PanelHeure.RGB_MIN[0]))+PanelHeure.RGB_MIN[0]);
				coulG = (int)((ratio*(PanelHeure.RGB_MAX[1]-PanelHeure.RGB_MIN[1]))+PanelHeure.RGB_MIN[1]);;
				coulB = (int)((ratio*(PanelHeure.RGB_MAX[2]-PanelHeure.RGB_MIN[2]))+PanelHeure.RGB_MIN[2]);;

				couleurFond = new Color(coulR, coulG, coulB);
			}

			boolean modeDessinDebut = listeCours.get(cours)[0];
			boolean modeDessinFin = listeCours.get(cours)[1];

			g.setColor(couleurFond);
			if(modeDessinDebut && modeDessinFin)
			{
				int yDebut = (int)(this.getHeight()*((double)(cours.getMinuteDebut())/60));
				int yfin;
				if(cours.getMinuteFin() == 0)
				{
					yfin = this.getHeight();
				}
				else
				{
					yfin = (int)(this.getHeight()*((double)(cours.getMinuteFin())/60));
				}
				g.fillRect(0, yDebut, this.getWidth(), yfin);
				g.setColor(Color.BLACK);
				g.drawString(intitule, 0, yDebut+g.getFont().getSize());
				g.drawString(valeur, this.getWidth()-valeur.length()*g.getFont().getSize()/2, yfin-2);
			}
			else if(modeDessinDebut)
			{
				int yDebut = (int)(this.getHeight()*((double)(cours.getMinuteDebut())/60));

				Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				g.setStroke(dashed);
				g.setColor(Color.BLACK);
				g.drawLine(0, yDebut-1, this.getWidth(), yDebut-1);

				g.setColor(couleurFond);
				g.fillRect(0, yDebut, this.getWidth(), this.getHeight());
				g.setColor(Color.BLACK);
				g.drawString(intitule, 0, yDebut+g.getFont().getSize());
			}
			else if(modeDessinFin)
			{
				int yfin;
				if(cours.getMinuteFin() == 0)
				{
					yfin = this.getHeight();
				}
				else
				{
					yfin = (int)(this.getHeight()*((double)(cours.getMinuteFin())/60));
				}
				g.fillRect(0, 0, this.getWidth(), yfin);
				g.setColor(Color.BLACK);
				g.drawString(valeur, this.getWidth()-valeur.length()*g.getFont().getSize()/2, yfin-2);

				Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				g.setStroke(dashed);
				g.setColor(Color.BLACK);
				g.drawLine(0, yfin, this.getWidth(), yfin);
			}
			else
			{
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
		}
	}
}
