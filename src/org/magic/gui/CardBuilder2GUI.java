package org.magic.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.magic.api.beans.MagicEdition;
import org.magic.gui.components.MagicEditionDetailPanel;
import org.magic.gui.models.MagicEditionsTableModel;
import org.magic.services.MTGDesktopCompanionControler;
import org.magic.services.PersonnalSetManager;

public class CardBuilder2GUI extends JPanel{
	
	private JTable table;
	private MagicEditionDetailPanel magicEditionDetailPanel;
	private MagicEditionsTableModel mod;
	private PersonnalSetManager editor;
	
	public CardBuilder2GUI() {
		
		editor=new PersonnalSetManager();
		
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);
		mod = new MagicEditionsTableModel();
		
		mod.init(editor.listEditions());
		mod.fireTableDataChanged();
		
		JPanel panelCards = new JPanel();
		tabbedPane.addTab("Cards", null, panelCards, null);
		panelCards.setLayout(new BorderLayout(0, 0));
		
		JPanel panelSets = new JPanel();
		tabbedPane.addTab("Set", null, panelSets, null);
		panelSets.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelSets.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable(mod);
		scrollPane.setViewportView(table);
		
		JPanel panel = new JPanel();
		panelSets.add(panel, BorderLayout.NORTH);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					editor.saveEdition(magicEditionDetailPanel.getMagicEdition());
					mod.init(editor.listEditions());
					mod.fireTableDataChanged();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				editor.removeEdition((MagicEdition)table.getValueAt(table.getSelectedRow(), 1));
			//	mod.removeRow(table.getSelectedRow());
				mod.fireTableDataChanged();
			}
		});
		panel.add(btnRemove);
		
		magicEditionDetailPanel = new MagicEditionDetailPanel(false);
		magicEditionDetailPanel.setEditable(true);
		panelSets.add(magicEditionDetailPanel, BorderLayout.EAST);
		
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		MTGDesktopCompanionControler.getInstance().getEnabledProviders().init();
		MTGDesktopCompanionControler.getInstance().getEnabledDAO().init();
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new CardBuilder2GUI());
		
		f.setVisible(true);
	}
	
}
