package compteur_argent.vue;
import compteur_argent.Main;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.*;

public class PanelFormulaire extends JPanel implements ActionListener
{
	private Main ctrl;
	private Fenetre frameMere;

	private JPanel panelForm;
	private JPanel panelBtn;

	private JButton btnConfirmer;
	private JTextField txtPourcentage;
	private JTextField txtHeuresSemaine;

	public PanelFormulaire(Main ctrl, Fenetre frameMere)
	{
		this.ctrl = ctrl;
		this.frameMere = frameMere;
		this.setLayout(new BorderLayout());

		// création des composants
		this.btnConfirmer = new JButton("Valider");
		this.txtPourcentage = new JTextField(Double.toString((ctrl.getPourcentageSMIC())));
		this.txtHeuresSemaine = new JTextField(Double.toString(ctrl.getNbHeuresSemaine()));
		this.panelForm = new JPanel(new GridLayout(3, 1, 15, 0));
		this.panelBtn = new JPanel(new FlowLayout());

		JPanel panelTxtPC = new JPanel(new FlowLayout(FlowLayout.LEFT)), panelTxtNbH = new JPanel(new FlowLayout(FlowLayout.LEFT));

		this.txtPourcentage.setColumns(5);
		this.txtHeuresSemaine.setColumns(5);

		// positionnement des composants

		panelTxtPC.add(new JLabel("Pourcentage du SMIC : ", JLabel.LEFT));
		panelTxtPC.add(txtPourcentage);
		panelTxtNbH.add(new JLabel("Nombre d'heures par semaine : ", JLabel.LEFT));
		panelTxtNbH.add(txtHeuresSemaine);

		this.panelForm.add(panelTxtPC);
		this.panelForm.add(panelTxtNbH);
		this.panelForm.add(new JLabel(new ImageIcon(Main.NIVEAU_DOSSIER+"ressources/making-money.gif")));
		this.panelBtn.add(this.btnConfirmer);
		this.add(this.panelForm, BorderLayout.CENTER);
		this.add(this.panelBtn, BorderLayout.SOUTH);

		// activation des composants
		this.btnConfirmer.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		try 
		{
			ctrl.setPourcentageSMIC(Double.parseDouble(this.txtPourcentage.getText()));
			ctrl.setNbHeuresSemaine(Double.parseDouble(this.txtHeuresSemaine.getText()));
			frameMere.modeCompteur();
			frameMere.majEmploiDuTemps();
			ctrl.sauvegarderDonnees();
		} 
		catch (NumberFormatException ex)
		{
			ex.printStackTrace();
		}
	}
}
