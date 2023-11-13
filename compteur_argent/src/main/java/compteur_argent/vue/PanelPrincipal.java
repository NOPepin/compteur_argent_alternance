package compteur_argent.vue;
import javax.swing.*;

import compteur_argent.Main;

import java.awt.BorderLayout;
import java.awt.Color;
// import java.awt.Component;
// import java.awt.Graphics;
// import java.awt.Graphics2D;

public class PanelPrincipal extends JPanel
{
	private Main ctrl;
	private PanelInfos panelInfos;
	private PanelFormulaire panelFormulaire;
	private PanelAffichage panelAffichage;

	public PanelPrincipal(Main ctrl, Fenetre fenetre)
	{
		this.ctrl = ctrl;
		this.setLayout(new BorderLayout());

		// Création des composants
		this.panelAffichage = new PanelAffichage(ctrl);
		this.panelInfos = new PanelInfos();
		this.ctrl.setPanelInfos(this.panelInfos);
		this.panelFormulaire = new PanelFormulaire(ctrl, fenetre);

		this.panelFormulaire.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.panelInfos.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.add(panelAffichage, BorderLayout.CENTER);
		this.add(panelInfos, BorderLayout.SOUTH);
		this.add(panelFormulaire, BorderLayout.EAST);


	}

	public void dessinerEmploiDuTemps()
	{
		this.panelAffichage.dessinerEmploiDuTemps();
	}

	public void majEmploiDuTemps()
	{
		this.panelAffichage.majEmploiDuTemps();
	}
}
