package compteur_argent.vue;
import compteur_argent.Main;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.*;

public class Fenetre extends JFrame implements WindowFocusListener
{
	private Main ctrl;
	private PanelPrincipal panelPrincipal = null;
	
	public Fenetre(Main ctrl)
	{
		this.ctrl = ctrl;
		this.setSize(800, 450);
		this.setLocationRelativeTo(null);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setIconImage(new ImageIcon(Main.NIVEAU_DOSSIER+"ressources/icon.png").getImage());
		this.setTitle("Compteur d'argent - Alternance");
		
		// Création des composants
		this.panelPrincipal = new PanelPrincipal(ctrl, this);
		
		// Positionnement des composants
		this.add(this.panelPrincipal);
		
		// Activation
		modeCompteur();
		this.dessinerEmploiDuTemps();
		this.addWindowFocusListener(this);

		this.setVisible(true);
	}
	
	public void dessinerEmploiDuTemps()
	{
		this.panelPrincipal.dessinerEmploiDuTemps();
	}

	public void majEmploiDuTemps()
	{
		this.panelPrincipal.majEmploiDuTemps();
	}
	public void modeCompteur()
	{
		this.ctrl.demarrerCompteur();
	}

	public void popUpErreur(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void windowGainedFocus(WindowEvent e)
	{
		majEmploiDuTemps();
		modeCompteur();
	}

	@Override
	public void windowLostFocus(WindowEvent e)
	{
		majEmploiDuTemps();
	}
}
