/******************************************************************************
*                                                                             *
*    Movie Renamer                                                            *
*    Copyright (C) 2011 Magré Nicolas                                         *
*                                                                             *
*    Movie Renamer is free software: you can redistribute it and/or modify    *
*    it under the terms of the GNU General Public License as published by     *
*    the Free Software Foundation, either version 3 of the License, or        *
*    (at your option) any later version.                                      *
*                                                                             *
*    This program is distributed in the hope that it will be useful,          *
*    but WITHOUT ANY WARRANTY; without even the implied warranty of           *
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            *
*    GNU General Public License for more details.                             *
*                                                                             *
*    You should have received a copy of the GNU General Public License        *
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.    *
*                                                                             *
******************************************************************************/

package fr.free.movierenamer.ui.res;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 *
 */
public class ContextMenuFieldMouseListener extends MouseAdapter {
    private JPopupMenu popup = new JPopupMenu();

    private Action cut;
    private Action copy;
    private Action paste;
    private Action selectAll;

    private JTextComponent textComponent;
    private String savedString = "";
    private Actions lastActionSelected;

    private enum Actions { CUT, COPY, PASTE, SELECT_ALL };

    public ContextMenuFieldMouseListener() {
      
        cut = new AbstractAction("Cut") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                lastActionSelected = Actions.CUT;
                savedString = textComponent.getText();
                textComponent.cut();
            }
        };
        
        copy = new AbstractAction("Copy") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                lastActionSelected = Actions.COPY;
                textComponent.copy();
            }
        };

        paste = new AbstractAction("Paste") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                lastActionSelected = Actions.PASTE;
                savedString = textComponent.getText();
                textComponent.paste();
            }
        };

        selectAll = new AbstractAction("Select All") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                lastActionSelected = Actions.SELECT_ALL;
                textComponent.selectAll();
            }
        };

        popup.add(copy);
        popup.add(cut);
        popup.add(paste);
        popup.addSeparator();
        popup.add(selectAll);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
            if (!(e.getSource() instanceof JTextComponent)) {
                return;
            }

            textComponent = (JTextComponent) e.getSource();
            textComponent.requestFocus();

            boolean enabled = textComponent.isEnabled();
            boolean editable = textComponent.isEditable();
            boolean nonempty = !(textComponent.getText() == null || textComponent.getText().equals(""));
            boolean marked = textComponent.getSelectedText() != null;

            boolean pasteAvailable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).isDataFlavorSupported(DataFlavor.stringFlavor);

            cut.setEnabled(enabled && editable && marked);
            copy.setEnabled(enabled && marked);
            paste.setEnabled(enabled && editable && pasteAvailable);
            selectAll.setEnabled(enabled && nonempty);

            int x,y;
            x = e.getX();
            y = e.getY();

            popup.show(e.getComponent(), x, y);
        }
    }
}