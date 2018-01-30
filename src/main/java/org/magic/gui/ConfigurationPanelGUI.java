package org.magic.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTreeTable;
import org.magic.api.interfaces.CardExporter;
import org.magic.api.interfaces.DashBoard;
import org.magic.api.interfaces.DeckSniffer;
import org.magic.api.interfaces.MTGPicturesCache;
import org.magic.api.interfaces.MTGServer;
import org.magic.api.interfaces.MagicDAO;
import org.magic.api.interfaces.MagicPricesProvider;
import org.magic.api.interfaces.MagicShopper;
import org.magic.api.interfaces.PictureProvider;
import org.magic.gui.components.ConfigurationPanel;
import org.magic.gui.components.LoggerViewPanel;
import org.magic.gui.components.ThreadMonitorPanel;
import org.magic.gui.models.conf.DashBoardProviderTreeTableModel;
import org.magic.gui.models.conf.DeckSnifferTreeTableModel;
import org.magic.gui.models.conf.ExportsTreeTableModel;
import org.magic.gui.models.conf.MTGPicsCacheProviderTreeTableModel;
import org.magic.gui.models.conf.MagicDAOProvidersTableModel;
import org.magic.gui.models.conf.MagicPricesProvidersTableModel;
import org.magic.gui.models.conf.MagicShoppersTableModel;
import org.magic.gui.models.conf.PicturesProvidersTableModel;
import org.magic.gui.models.conf.ProvidersTableModel;
import org.magic.gui.models.conf.RssBeanTableModel;
import org.magic.gui.models.conf.ServersTreeTableModel;
import org.magic.services.MTGControler;

public class ConfigurationPanelGUI extends JPanel {
	private JTable cardsProviderTable;
	private JXTreeTable priceProviderTable;
	private JXTreeTable daoProviderTable;
	private JXTreeTable shopperTreeTable;
	private JXTreeTable dashboardTreeTable;
	private JXTreeTable importTreeTable;
	private JXTable rssTable;
	private JXTreeTable exportsTable;
	private JXTreeTable picturesProviderTable;
	private JXTreeTable serversTreeTable;
	private JXTreeTable cachesTreeTable;
	private LoggerViewPanel loggerViewPanel;
	private ThreadMonitorPanel threadMonitorPanel;
	
	public ConfigurationPanelGUI() {
		
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);

		
		JPanel providerConfigPanel = new JPanel();
		tabbedPane.addTab(MTGControler.getInstance().getLangService().getCapitalize("PROVIDERS"), null, providerConfigPanel, null);
		providerConfigPanel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane subTabbedProviders = new JTabbedPane(JTabbedPane.TOP);
		providerConfigPanel.add(subTabbedProviders);
		
		JScrollPane cardsProvidersScrollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("CARDS"), null, cardsProvidersScrollPane, null);
		
		cardsProviderTable = new JTable();
		cardsProvidersScrollPane.setViewportView(cardsProviderTable);
		
		JScrollPane picturesScollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("PICTURES"), null, picturesScollPane, null);
		
		picturesProviderTable = new JXTreeTable(new PicturesProvidersTableModel());
		picturesScollPane.setViewportView(picturesProviderTable);
		picturesProviderTable.addTreeSelectionListener(e->{
				if(e.getNewLeadSelectionPath()!=null)
					if(e.getNewLeadSelectionPath().getPathCount()>1)
						((PicturesProvidersTableModel)picturesProviderTable.getTreeTableModel()).setSelectedNode((PictureProvider)e.getNewLeadSelectionPath().getPathComponent(1));
		});
		
		
		JScrollPane priceProviderScrollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("PRICERS"), null, priceProviderScrollPane, null);
		priceProviderTable = new JXTreeTable(new MagicPricesProvidersTableModel());
		cardsProviderTable.setModel(new ProvidersTableModel());
		
		priceProviderTable.addTreeSelectionListener(e->{
				if(e.getNewLeadSelectionPath()!=null)
					if(e.getNewLeadSelectionPath().getPathCount()>1)
						((MagicPricesProvidersTableModel)priceProviderTable.getTreeTableModel()).setSelectedNode((MagicPricesProvider)e.getNewLeadSelectionPath().getPathComponent(1));
		});
		priceProviderScrollPane.setViewportView(priceProviderTable);
		
		JScrollPane daoProviderScrollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("DATABASES"), null, daoProviderScrollPane, null);
		
		daoProviderTable = new JXTreeTable(new MagicDAOProvidersTableModel());
		daoProviderTable.addTreeSelectionListener(e->{
				if(e.getNewLeadSelectionPath()!=null)
					if(e.getNewLeadSelectionPath().getPathCount()>1)
						((MagicDAOProvidersTableModel)daoProviderTable.getTreeTableModel()).setSelectedNode((MagicDAO)e.getNewLeadSelectionPath().getPathComponent(1));
		});
		daoProviderScrollPane.setViewportView(daoProviderTable);
		
		JScrollPane shopperScrollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("SHOPPERS"), null, shopperScrollPane, null);
		
		shopperTreeTable = new JXTreeTable(new MagicShoppersTableModel());
		shopperTreeTable.addTreeSelectionListener(e->{
				if(e.getNewLeadSelectionPath()!=null)
					if(e.getNewLeadSelectionPath().getPathCount()>1)
						((MagicShoppersTableModel)shopperTreeTable.getTreeTableModel()).setSelectedNode((MagicShopper)e.getNewLeadSelectionPath().getPathComponent(1));
		});
		shopperScrollPane.setViewportView(shopperTreeTable);
		
		JScrollPane exportsScrollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("CARDS_IMPORT_EXPORT"), null, exportsScrollPane, null);
		exportsTable = new JXTreeTable(new ExportsTreeTableModel());
		exportsTable.addTreeSelectionListener(e->{
				if(e.getNewLeadSelectionPath()!=null)
					if(e.getNewLeadSelectionPath().getPathCount()>1)
						((ExportsTreeTableModel)exportsTable.getTreeTableModel()).setSelectedNode((CardExporter)e.getNewLeadSelectionPath().getPathComponent(1));
		});
		exportsScrollPane.setViewportView(exportsTable);
		
		JScrollPane importScrollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("DECKS_IMPORTER"), null, importScrollPane, null);
		
		importTreeTable = new JXTreeTable(new DeckSnifferTreeTableModel());
		importScrollPane.setViewportView(importTreeTable);
		importTreeTable.addTreeSelectionListener(e->{
				if(e.getNewLeadSelectionPath()!=null)
					if(e.getNewLeadSelectionPath().getPathCount()>1)
						((DeckSnifferTreeTableModel)importTreeTable.getTreeTableModel()).setSelectedNode((DeckSniffer)e.getNewLeadSelectionPath().getPathComponent(1));
		});
		
		JScrollPane dashboardScrollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("DASHBOARD_MODULE"), null, dashboardScrollPane, null);
		
		dashboardTreeTable = new JXTreeTable(new DashBoardProviderTreeTableModel());
		dashboardTreeTable.addTreeSelectionListener(e->{
				if(e.getNewLeadSelectionPath()!=null)
					if(e.getNewLeadSelectionPath().getPathCount()>1)
						((DashBoardProviderTreeTableModel)dashboardTreeTable.getTreeTableModel()).setSelectedNode((DashBoard)e.getNewLeadSelectionPath().getPathComponent(1));
		});
		dashboardScrollPane.setViewportView(dashboardTreeTable);
		
		JScrollPane serversScrollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("SERVERS"), null, serversScrollPane, null);
		serversTreeTable = new JXTreeTable(new ServersTreeTableModel());
		serversTreeTable.addTreeSelectionListener(e->{
				if(e.getNewLeadSelectionPath()!=null)
					if(e.getNewLeadSelectionPath().getPathCount()>1)
						((ServersTreeTableModel)serversTreeTable.getTreeTableModel()).setSelectedNode((MTGServer)e.getNewLeadSelectionPath().getPathComponent(1));
		});
		serversScrollPane.setViewportView(serversTreeTable);
		
		JScrollPane cachesScrollPane = new JScrollPane();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("CACHES"), null, cachesScrollPane, null);
		cachesTreeTable = new JXTreeTable(new MTGPicsCacheProviderTreeTableModel());
		cachesTreeTable.addTreeSelectionListener(e->{
				if(e.getNewLeadSelectionPath()!=null)
					if(e.getNewLeadSelectionPath().getPathCount()>1)
						((MTGPicsCacheProviderTreeTableModel)cachesTreeTable.getTreeTableModel()).setSelectedNode((MTGPicturesCache)e.getNewLeadSelectionPath().getPathComponent(1));
			}
		);
		cachesScrollPane.setViewportView(cachesTreeTable);
		
		
		
		JPanel rssPanel = new JPanel();
		subTabbedProviders.addTab(MTGControler.getInstance().getLangService().getCapitalize("RSS_MODULE"), null, rssPanel, null);
		rssPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane rssScrollPane = new JScrollPane();
		rssPanel.add(rssScrollPane);
		
		rssTable = new JXTable(new RssBeanTableModel());
		rssScrollPane.setViewportView(rssTable);
		
		JPanel panneauhaut = new JPanel();
		rssPanel.add(panneauhaut, BorderLayout.NORTH);
	
		
		ConfigurationPanel configurationPanel = new ConfigurationPanel();
		tabbedPane.addTab(MTGControler.getInstance().getLangService().getCapitalize("CONFIGURATION"), null, configurationPanel, null);
		
		ServersGUI serversGUI = new ServersGUI();
		tabbedPane.addTab(MTGControler.getInstance().getLangService().getCapitalize("ACTIVE_SERVERS"), null, serversGUI, null);
		
		loggerViewPanel = new LoggerViewPanel();
		tabbedPane.addTab(MTGControler.getInstance().getLangService().getCapitalize("LOGS"), null, loggerViewPanel, null);
		
		threadMonitorPanel = new ThreadMonitorPanel();
		tabbedPane.addTab(MTGControler.getInstance().getLangService().getCapitalize("THREADS"), null, threadMonitorPanel, null);
		
		
		
	}


}
