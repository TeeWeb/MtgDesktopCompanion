package org.magic.gui.renderer;

import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.magic.api.beans.MagicEdition;
import org.magic.gui.renderer.MagicEditionIconListRenderer.SIZE;

public class MagicEditionsComboBoxRenderer implements TableCellRenderer {
	
	private static final long serialVersionUID = 1L;
	private boolean enable;
	
	
	public MagicEditionsComboBoxRenderer() {
		enable=true;
	}
	
	public MagicEditionsComboBoxRenderer(boolean enabled) {
		enable=enabled;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {

		List<MagicEdition> e = (List) value;
		JComboBox<MagicEdition> cbo = new JComboBox<>(e.toArray(new MagicEdition[e.size()]));
		cbo.setOpaque(false);
		cbo.setEnabled(enable);
		cbo.setRenderer(new MagicEditionIconListRenderer(SIZE.SMALL));
		return cbo;
	}
}
