package compteur_argent.vue;

import compteur_argent.Main;
import compteur_argent.metier.Cours;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.*;

public class PanelAffichage extends JPanel
{
	private final static int NB_HEURES_EMPLOI_DU_TEMPS = 11;
	private Main ctrl;
	private ArrayList<Cours> listeCours;
	private JPanel[][] tabPanelHeure;

	private int nbCol;

	public PanelAffichage(Main ctrl)
	{
		this.ctrl = ctrl;

		listeCours = this.ctrl.getListeCours();

		this.nbCol = 6;

		// si on a cours le samedi j'ajoute la colonne du samedi
		for (Cours c : this.listeCours)
		{
			if(c.getJourSemaine()==6 && !c.estFerier())
			{
				this.nbCol = 7;
			}
		}

		
		this.tabPanelHeure = new JPanel[nbCol][NB_HEURES_EMPLOI_DU_TEMPS];
		this.setLayout(new BorderLayout());

		// création des composants
		JPanel panelJours = new JPanel(new GridLayout(1, this.nbCol));
		JPanel panelEmploiDuTemps = new JPanel(new GridLayout(NB_HEURES_EMPLOI_DU_TEMPS, this.nbCol));

		ArrayList<JLabel> listeLblJours = new ArrayList<JLabel>();

		listeLblJours.add(new JLabel("Lundi", JLabel.CENTER));
		listeLblJours.add(new JLabel("Mardi", JLabel.CENTER));
		listeLblJours.add(new JLabel("Mercredi", JLabel.CENTER));
		listeLblJours.add(new JLabel("Jeudi", JLabel.CENTER));
		listeLblJours.add(new JLabel("Vendredi", JLabel.CENTER));
		listeLblJours.add(new JLabel("Samedi", JLabel.CENTER));

		for (JLabel lbl : listeLblJours)
		{
			lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		for (int i = 0; i < NB_HEURES_EMPLOI_DU_TEMPS; i++)
		{
			String heure = String.format("%02d:00", i+8);
			this.tabPanelHeure[0][i] = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			this.tabPanelHeure[0][i].add(new JLabel(heure, JLabel.RIGHT));
		}
		for (int i = 1; i < nbCol; i++)
		{
			for (int j = 0; j < 11; j++)
			{
				this.tabPanelHeure[i][j] = new PanelHeure();
				this.tabPanelHeure[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
		}

		panelJours.add(new JLabel());
		panelJours.add(listeLblJours.get(0));
		panelJours.add(listeLblJours.get(1));
		panelJours.add(listeLblJours.get(2));
		panelJours.add(listeLblJours.get(3));
		panelJours.add(listeLblJours.get(4));

		if(this.nbCol == 7)
		{
			panelJours.add(listeLblJours.get(5));
		}
		
		// positionnement des composants
		for (int i = 0; i < NB_HEURES_EMPLOI_DU_TEMPS; i++)
		{
			for (int j = 0; j < nbCol; j++)
			{
				panelEmploiDuTemps.add(this.tabPanelHeure[j][i]);
			}
		}

		this.add(panelJours, BorderLayout.NORTH);
		this.add(panelEmploiDuTemps, BorderLayout.CENTER);
	}

	public void dessinerEmploiDuTemps()
	{
		for (Cours c : this.listeCours) 
		{
			if(!c.estFerier())
			{
				int ligDebut = c.getHeureDebut()-8;
				int ligFin = c.getHeureFin()-8;
				int col = c.getJourSemaine();
				if(c.getMinuteFin() == 0){ ligFin--; }

				if(ligDebut == ligFin)
				{
					((PanelHeure)(this.tabPanelHeure[col][ligDebut])).dessinerCours(c);
				}
				else
				{
					((PanelHeure)(this.tabPanelHeure[col][ligDebut])).dessinerDebutCours(c);
					for (int i = ligDebut+1; i < ligFin; i++)
					{
						((PanelHeure)(this.tabPanelHeure[col][i])).remplirCase(c);
					}
					((PanelHeure)(this.tabPanelHeure[col][ligFin])).dessinerFinCours(c);
				}
			}
		}
	}

	public void majEmploiDuTemps()
	{
		for (int i = 1; i < tabPanelHeure.length; i++)
		{
			for (int j = 0; j < tabPanelHeure[i].length; j++)
			{
				((PanelHeure) tabPanelHeure[i][j]).repaint();
			}
		}
	}
}
