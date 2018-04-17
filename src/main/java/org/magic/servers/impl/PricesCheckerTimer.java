package org.magic.servers.impl;

import java.awt.TrayIcon.MessageType;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.magic.api.beans.MagicCardAlert;
import org.magic.api.beans.MagicPrice;
import org.magic.api.interfaces.MTGCardsProvider.STATUT;
import org.magic.api.interfaces.MTGPricesProvider;
import org.magic.api.interfaces.abstracts.AbstractMTGServer;
import org.magic.services.MTGControler;

public class PricesCheckerTimer extends AbstractMTGServer{

	@Override
	public STATUT getStatut() {
		return STATUT.STABLE;
	}
	
	Timer timer ;
	TimerTask tache ;
	private boolean running=false;
	private boolean enableNotify=true;

	public void enableGUINotify(boolean enableNotify) {
		this.enableNotify = enableNotify;
	}

	@Override
    public String description() {
    	return "AutoCheck prices for cards";
    }
	
	public PricesCheckerTimer() {
		
		super();
		timer = new Timer();
	}
	

	public void start()
	{
		running=true;
		tache = new TimerTask() {    
            public void run() {
            	StringBuilder message=new StringBuilder();
            	boolean notify=false;
            	if(MTGControler.getInstance().getEnabledDAO().listAlerts()!=null)
            	for(MagicCardAlert alert : MTGControler.getInstance().getEnabledDAO().listAlerts())
                {
            		alert.getOffers().clear();
                	for(MTGPricesProvider prov : MTGControler.getInstance().getEnabledPricers())
                	{
                		List<MagicPrice> okz = new ArrayList<>();
                		try {
							List<MagicPrice> list=prov.getPrice(alert.getCard().getEditions().get(0), alert.getCard());
							for(MagicPrice p : list)
							{	if(p.getValue()<=alert.getPrice() && p.getValue()>Double.parseDouble(MTGControler.getInstance().get("min-price-alert")))
								{
									alert.getOffers().add(p);
									okz.add(p);
									logger.info("Found offer " + prov + ":" + alert.getCard() + " "+ p.getValue() +p.getCurrency() );
									notify=true;
								}
							}
							prov.alertDetected(okz);
							alert.orderDesc();
						} catch (Exception e) {
							logger.error("error loading price "+prov , e);
						}
                	}
               		message.append(alert.getCard()).append(" : ").append(alert.getOffers().size()).append(" offers").append("\n");
			    }
            	
            	if(enableNotify && notify)
           			MTGControler.getInstance().notify("New offers", message.toString(), MessageType.INFO);
            	
            }
        };
		
		timer.scheduleAtFixedRate(tache,0,Long.parseLong(getString("TIMEOUT_MINUTE"))*60000);
		logger.info("Server start with "+ getString("TIMEOUT_MINUTE")+" min timeout");
	    
	}
	
	public void stop()
	{
		tache.cancel();
		timer.purge();
		running=false;
	}

	@Override
	public boolean isAlive() {
		return running;
	}

	@Override
	public String getName() {
		return "Price Timer";
		
	}


	@Override
	public boolean isAutostart() {
		return getBoolean("AUTOSTART");
	}

	@Override
	public void initDefault() {
		setProperty("AUTOSTART", "true");
		setProperty("TIMEOUT_MINUTE", "120");
		
	}

	@Override
	public String getVersion() {
		return "1.5";
	}
	
	
}
