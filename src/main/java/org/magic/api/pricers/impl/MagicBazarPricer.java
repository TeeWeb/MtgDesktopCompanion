package org.magic.api.pricers.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicEdition;
import org.magic.api.beans.MagicPrice;
import org.magic.api.interfaces.abstracts.AbstractMagicPricesProvider;
import org.magic.tools.URLTools;

public class MagicBazarPricer extends AbstractMagicPricesProvider {

	Document doc;
	private ArrayList<MagicPrice> list;

	@Override
	public STATUT getStatut() {
		return STATUT.BETA;
	}

	public MagicBazarPricer() {
		super();
		list = new ArrayList<>();
	}

	@Override
	public List<MagicPrice> getLocalePrice(MagicEdition me, MagicCard card) throws IOException {
		list.clear();
		String url = getString("URL") + URLTools.encode(card.getName());
		logger.info(getName() + " looking for prices " + url);

		try {
			doc = URLTools.extractHtml(url);
			Elements els = doc.select("div.filterElement");
			for (int i = 0; i < els.size(); i++) {
				Element e = els.get(i);
				MagicPrice mp = new MagicPrice();
				mp.setLanguage(e.getElementsByClass("langue").get(0).getElementsByTag("img").get(0).attr("alt"));
				mp.setQuality(e.getElementsByClass("etat").html());
				mp.setValue(Double.parseDouble(clean(e.select("div.prix").text())));
				mp.setCurrency("EUR");
				mp.setCountry("France");
				mp.setSite(getName());
				mp.setUrl(url);
				mp.setSeller(e.getElementsByClass("edition").get(0).getElementsByIndexEquals(0).get(0).text());
				mp.setFoil(e.attr("data-foil").equals("O"));
				list.add(mp);
			}
			logger.info(getName() + " found " + list.size() + " item(s)");

			return list;
		} catch (Exception e) {
			logger.trace("Error loading price for " + url, e);
			logger.info(getName() + " no item : " + e.getMessage());
			return list;
		}
	}

	private String clean(String html) {
		return StringEscapeUtils.escapeHtml3(html).replace(",", ".").replace(" ", "").replace("€", "");
	}

	@Override
	public String getName() {
		return "MagicBazar";
	}


	@Override
	public void initDefault() {
		setProperty("URL", "https://www.magicbazar.fr/recherche/result.php?s=");
		

	}

	@Override
	public String getVersion() {
		return "1.4";
	}

}
