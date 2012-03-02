/******************************************************************************
 *                                                                             *
 *    Movie Renamer                                                            *
 *    Copyright (C) 2012 Magré Nicolas                                         *
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
package fr.free.movierenamer.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import fr.free.movierenamer.utils.Loading;

/**
 *
 * @author duffy
 */
public class LoadingDialog extends JDialog {

  private Map<Integer, JProgressBar> progress;
  private ArrayList<Integer> id;

  /** Creates new form LoadingDialog
   * @param loadingWorker
   * @param parent
   */
  public LoadingDialog(ArrayList<Loading> loadingWorker, Component parent) {
    initComponents();
    progress = new HashMap<Integer, JProgressBar>();
    id = new ArrayList<Integer>();
    for (int i = 0; i < loadingWorker.size(); i++) {
      JProgressBar progressBar = new JProgressBar();
      progressBar.setString(loadingWorker.get(i).getTitle());
      progressBar.setStringPainted(true);
      progressBar.setIndeterminate(loadingWorker.get(i).getIndeterminate());
      progressBar.setMaximum(loadingWorker.get(i).getMax());
      progressBar.setMinimum(0);
      progressBar.setSize(new Dimension(80, 35));
      progress.put(loadingWorker.get(i).getId(), progressBar);
      id.add(loadingWorker.get(i).getId());
      seddPnl.add(progress.get(loadingWorker.get(i).getId()));
    }
    seddPnl.validate();
    seddPnl.repaint();
    pack();
    setModal(true);
    setLocationRelativeTo(parent);
  }

  public boolean isShown(){
    return isVisible();
  }

  public synchronized void setValue(final int value, final int idWorker) {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        if (!progress.containsKey(idWorker)) return;
        progress.get(idWorker).setValue(value);
        if (value == progress.get(idWorker).getMaximum()) {
          progress.get(idWorker).setIndeterminate(false);
          for(int i =0; i< id.size();i++) if(id.get(i) == idWorker) id.remove(i);
          if(id.isEmpty()){
            setVisible(false);
            return;
          }
        }
      }
    });
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    loadingLbl = new javax.swing.JLabel();
    seddPnl = new javax.swing.JPanel();

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 100, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 100, Short.MAX_VALUE)
    );

    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("fr/free/movierenamer/i18n/Bundle"); // NOI18N
    setTitle(bundle.getString("loading")); // NOI18N
    setResizable(false);

    loadingLbl.setText(bundle.getString("loadingWait")); // NOI18N

    seddPnl.setLayout(new javax.swing.BoxLayout(seddPnl, javax.swing.BoxLayout.PAGE_AXIS));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(seddPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
          .addComponent(loadingLbl))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(loadingLbl)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(seddPnl, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel jPanel1;
  private javax.swing.JLabel loadingLbl;
  private javax.swing.JPanel seddPnl;
  // End of variables declaration//GEN-END:variables
}
