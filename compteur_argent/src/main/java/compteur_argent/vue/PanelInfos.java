package compteur_argent.vue;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class PanelInfos extends JPanel
{
	private JLabel lblCompteurSemaine, lblCompteurJour;

	public PanelInfos()
	{
		this.setLayout(new GridLayout(1, 3));

		// Création des composants
		this.lblCompteurSemaine = new JLabel("0,000€");
		this.lblCompteurJour = new JLabel("0,000€");
		JPanel panelCompteurSemaine = new JPanel(new FlowLayout());
		JPanel panelCompteurJour = new JPanel(new FlowLayout());
	
		// Positionnement des composants
		panelCompteurSemaine.add(new JLabel("Argent accumulé depuis le début de la semaine : "));
		panelCompteurSemaine.add(this.lblCompteurSemaine);
		panelCompteurJour.add(new JLabel("Argent accumulé aujourd'hui : "));
		panelCompteurJour.add(this.lblCompteurJour);

		this.add(panelCompteurSemaine);
		this.add(panelCompteurJour);
	
		// Activation des Composants
	}

	public void setCumulSemaine(double cumulSemaine)
	{
		String textLbl = String.format("%,10.3f", cumulSemaine) + "€";

		this.lblCompteurSemaine.setText(textLbl);
	}

	public void setCumulJour(double cumulJour)
	{
		String textLbl = String.format("%,10.3f", cumulJour) + "€";

		this.lblCompteurJour.setText(textLbl);
	}
}
