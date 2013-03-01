/*
 * Copyright (C) 2012-2013 Nicolas Magré
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.free.movierenamer.ui.panel;

import com.alee.extended.filechooser.WebFileChooser;
import fr.free.movierenamer.settings.Settings;
import fr.free.movierenamer.ui.swing.LogsTableModel;
import fr.free.movierenamer.ui.utils.ImageUtils;
import fr.free.movierenamer.utils.LocaleUtils;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 * Class LogPanel
 *
 * @author Nicolas Magré
 */
public class LogPanel extends JDialog {

  private final WebFileChooser fileChooser;
  private static final long serialVersionUID = 1L;
  private final logHandler handler = new logHandler();
  private final StringWriter text = new StringWriter();
  private final PrintWriter out = new PrintWriter(text);
  private final LogsTableModel logsModel = new LogsTableModel();
  private final TableRowSorter<LogsTableModel> sorter;
  private boolean showInfo = true;

  /**
   * Creates new form LogPanel
   */
  public LogPanel() {

    initComponents();

    fileChooser = new WebFileChooser(this, LocaleUtils.i18nExt("saveLogFile"));// FIXME i18n

    logsTable.setModel(logsModel);
    logsTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
      private static final long serialVersionUID = 1L;

      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Level level = (Level) value;
        if (level.equals(Level.SEVERE)) {
          setBackground(Color.red);
          setForeground(Color.WHITE);
        } else if (level.equals(Level.WARNING)) {
          setBackground(Color.YELLOW);
          setForeground(Color.BLACK);
        } else {
          setBackground(Color.WHITE);
          setForeground(Color.BLACK);
        }

        return this;
      }
    });

    RowFilter<LogsTableModel, Integer> ageFilter = new RowFilter<LogsTableModel, Integer>() {
      @Override
      public boolean include(Entry<? extends LogsTableModel, ? extends Integer> entry) {
        LogsTableModel obj = entry.getModel();
        int index = entry.getIdentifier();
        Level level = (Level) obj.getValueAt(index, 1);
        if (!level.equals(Level.SEVERE) && !level.equals(Level.WARNING) && !showInfo) {
          return false;
        }

        return true;
      }
    };

    sorter = new TableRowSorter<LogsTableModel>(logsModel);
    sorter.setRowFilter(ageFilter);
    logsTable.setRowSorter(sorter);
    logsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane2 = new javax.swing.JScrollPane();
    logsTable = new com.alee.laf.table.WebTable(){
      @Override
      public Component prepareRenderer(TableCellRenderer renderer, int row,
        int column) {
        Component component = super.prepareRenderer(renderer, row, column);
        int rendererWidth = component.getPreferredSize().width;
        TableColumn tableColumn = getColumnModel().getColumn(column);
        tableColumn.setPreferredWidth(Math.max(rendererWidth +
          getIntercellSpacing().width,
          tableColumn.getPreferredWidth()));
      return  component;
    }
  };
  webCheckBox1 = new com.alee.laf.checkbox.WebCheckBox();
  logsFileBtn = new com.alee.laf.button.WebButton();

  jScrollPane2.setViewportView(logsTable);

  webCheckBox1.setText("Hide INFO");
  webCheckBox1.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      webCheckBox1ActionPerformed(evt);
    }
  });

  logsFileBtn.setIcon(ImageUtils.TEXTFILE_16);
  logsFileBtn.setText(LocaleUtils.i18nExt("logfile")); // NOI18N
  logsFileBtn.setEnabled(false);
  logsFileBtn.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      logsFileBtnActionPerformed(evt);
    }
  });

  javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
  getContentPane().setLayout(layout);
  layout.setHorizontalGroup(
    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(layout.createSequentialGroup()
      .addContainerGap()
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
          .addGap(13, 13, 13))
        .addGroup(layout.createSequentialGroup()
          .addComponent(webCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(logsFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addContainerGap())))
  );
  layout.setVerticalGroup(
    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
      .addContainerGap()
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(webCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(logsFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
      .addGap(19, 19, 19)
      .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
      .addContainerGap())
  );

  pack();
  }// </editor-fold>//GEN-END:initComponents

  private void webCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webCheckBox1ActionPerformed
    showInfo = !webCheckBox1.isSelected();
    sorter.allRowsChanged();
  }//GEN-LAST:event_webCheckBox1ActionPerformed

  private void logsFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logsFileBtnActionPerformed
    // TODO save logs
    StringBuilder logs = new StringBuilder();
    for (LogRecord record : handler.getRecord()) {
      logs.append(record.getSequenceNumber()).append(" : ").append(record.getSourceClassName()).append(".");
      logs.append(record.getSourceMethodName()).append(" : ").append(record.getMessage()).append("\n");
    }

    System.out.println(logs);// FIXME remove
  }//GEN-LAST:event_logsFileBtnActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane jScrollPane2;
  private com.alee.laf.button.WebButton logsFileBtn;
  private com.alee.laf.table.WebTable logsTable;
  private com.alee.laf.checkbox.WebCheckBox webCheckBox1;
  // End of variables declaration//GEN-END:variables

  /**
   * @return the handler
   */
  public logHandler getHandler() {
    return handler;
  }

  private class logHandler extends Handler {

    private List<LogRecord> records;

    public logHandler() {
      records = new ArrayList<LogRecord>();
    }

    public List<LogRecord> getRecord() {
      return Collections.unmodifiableList(records);
    }

    @Override
    public void publish(LogRecord record) {
      logsModel.addRecord(record);
      records.add(record);
      if (records.size() == 1) {
        logsFileBtn.setEnabled(true);
      }
    }

    @Override
    public void flush() {
      out.flush();
      text.flush();
    }

    @Override
    public void close() throws SecurityException {
      out.close();
      try {
        text.close();
      } catch (IOException ex) {
        Settings.LOGGER.log(Level.SEVERE, null, ex);
      }
    }
  }
}
