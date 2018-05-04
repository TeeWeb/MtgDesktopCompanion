package org.magic.servers.impl;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.magic.api.beans.CardShake;
import org.magic.api.beans.MTGNotification;
import org.magic.api.interfaces.MTGCardsProvider.STATUT;
import org.magic.api.interfaces.abstracts.AbstractMTGServer;
import org.magic.api.notifiers.impl.ConsoleNotifier;
import org.magic.api.notifiers.impl.OSTrayNotifier;
import org.magic.services.MTGControler;
import org.magic.sorters.CardsShakeSorter;
import org.magic.sorters.CardsShakeSorter.SORT;

public class OversightServer extends AbstractMTGServer {

	@Override
	public STATUT getStatut() {
		return STATUT.STABLE;
	}

	private Timer timer;
	private TimerTask tache;
	private boolean running = false;

	@Override
	public String description() {
		return "oversight for daily prices variations";
	}

	public OversightServer() {
		super();
		timer = new Timer();
	}
	
	
	public void start() {
		running = true;
		tache = new TimerTask() {
			public void run() {
				try {
					List<CardShake> ret = MTGControler.getInstance().getEnabledDashBoard().getShakerFor(null);
					Collections.sort(ret, new CardsShakeSorter(SORT.valueOf(getString("SORT_FILTER"))));
				
					
					String msg = toMessage(ret);
					
					MTGNotification notif = new MTGNotification();
									notif.setTitle("Oversight");
									notif.setMessage(msg);
									notif.setType(MessageType.INFO);
					
					for(String not : getString("NOTIFIER").split(","))
						MTGControler.getInstance().getNotifier(not).send(notif);
				
				} catch (IOException e) {
					logger.error(e);
				}
			}

			private String toMessage(List<CardShake> ret) {
				StringBuilder build = new StringBuilder();
				for(CardShake cs : ret)
				{
					build.append(cs.getName())
					   .append("(")
					   .append(cs.getEd())
					   .append(") : ")
					   .append( (cs.getPercentDayChange()>0)?"+":"")
					   .append(formatter.format(cs.getPercentDayChange())+"%")
					   .append(" -> ")
					   .append(cs.getPrice())
					   .append("$\n");
				}
				
				return build.toString();
			}
		};

		timer.scheduleAtFixedRate(tache, 0, Long.parseLong(getString("TIMEOUT_MINUTE")) * 60000);
		logger.info("Server start with " + getString("TIMEOUT_MINUTE") + " min timeout");

	}

	
	public void stop() {
		tache.cancel();
		timer.purge();
		running = false;
	}

	@Override
	public boolean isAlive() {
		return running;
	}

	@Override
	public String getName() {
		return "Oversight Server";

	}

	@Override
	public boolean isAutostart() {
		return getBoolean("AUTOSTART");
	}

	@Override
	public void initDefault() {
		setProperty("AUTOSTART", "true");
		setProperty("TIMEOUT_MINUTE", "120");
		setProperty("ALERT_MIN_PERCENT","40");
		setProperty("NOTIFIER","Tray,Console");
		setProperty("SORT_FILTER","DAY_PRICE_CHANGE");
		setProperty("FORMAT_FILTER","");
	}

	@Override
	public String getVersion() {
		return "1.5";
	}

}