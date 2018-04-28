package org.magic.api.exports.impl;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicCardStock;
import org.magic.api.beans.MagicDeck;
import org.magic.api.interfaces.MTGCardsProvider.STATUT;
import org.magic.api.interfaces.abstracts.AbstractCardExport;
import org.magic.services.MTGControler;

public class MTGArenaExport extends AbstractCardExport {

	@Override
	public String getFileExtension() {
		return ".mtgarena";
	}

	@Override
	public void export(MagicDeck deck, File dest) throws IOException {
		
		StringBuilder temp = new StringBuilder();
		
		for(Map.Entry<MagicCard, Integer> entry : deck.getMap().entrySet())
		{
			temp.append(entry.getValue())
				.append(" ")
				.append(entry.getKey())
				.append(" (")
				.append(entry.getKey().getEditions().get(0).getId().toUpperCase())
				.append(")")
				.append(" ")
				.append(entry.getKey().getEditions().get(0).getNumber())
				.append("\r\n");
			
		}
		
		if(!deck.getMapSideBoard().isEmpty())
			for(Map.Entry<MagicCard, Integer> entry : deck.getMapSideBoard().entrySet())
			{
				temp.append("\r\n")
					.append(entry.getValue())
					.append(" ")
					.append(entry.getKey())
					.append(" (")
					.append(entry.getKey().getEditions().get(0).getId().toUpperCase())
					.append(")")
					.append(" ")
					.append(entry.getKey().getEditions().get(0).getNumber());
				
			}

		FileUtils.writeStringToFile(dest, temp.toString(), "UTF-8");
		
		
		StringSelection selection = new StringSelection(temp.toString());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
		
		logger.debug("save in clipboard");

	}
	
	@Override
	public MagicDeck importDeck(File f) throws IOException {
		try (BufferedReader read = new BufferedReader(new FileReader(f))) {
			MagicDeck deck = new MagicDeck();
			deck.setName(f.getName().substring(0, f.getName().indexOf('.')));
			String line = read.readLine();

			while (line != null) {
				int qte = Integer.parseInt(line.substring(0, line.indexOf(' ')));
				String name = line.substring(line.indexOf(' '), line.indexOf('('));

				deck.getMap().put(MTGControler.getInstance().getEnabledProviders().searchCardByCriteria("name", name.trim(), null, true).get(0), qte);
				line = read.readLine();
			}
			return deck;
		}
	}

	@Override
	public List<MagicCardStock> importStock(File f) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "MTG Arena";
	}

	@Override
	public STATUT getStatut() {
		return STATUT.DEV;
	}

	@Override
	public void initDefault() {
		

	}

	@Override
	public String getVersion() {
		return "1.0";
	}

}