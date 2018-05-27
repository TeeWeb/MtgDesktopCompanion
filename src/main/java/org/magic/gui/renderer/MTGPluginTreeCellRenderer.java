package org.magic.gui.renderer;

import java.awt.Component;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.magic.api.interfaces.MTGPlugin;
import org.magic.services.MTGConstants;

public class MTGPluginTreeCellRenderer extends DefaultTreeCellRenderer{

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,int row, boolean hasFocus) {
		tree.setRowHeight(MTGConstants.TREE_ROW_HEIGHT);
		
		JLabel lab = new JLabel();
		
		lab.setBackground(tree.getBackground());
		lab.setForeground(tree.getForeground());
		
		if(value instanceof MTGPlugin)
		{
				   lab.setText(value.toString());
				   lab.setIcon(((MTGPlugin)value).getIcon());
				   return lab;
		}
		else if (value instanceof Entry)
		{
					lab.setText(((Entry)value).getKey().toString());
			return lab;
		}
		
		
		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		
		
	}
	
}