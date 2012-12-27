/*
 * Movie Renamer
 * Copyright (C) 2012 Nicolas Magré
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

import com.alee.laf.label.WebLabel;
import com.alee.laf.list.DefaultListModel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.laf.toolbar.WebToolBar;
import fr.free.movierenamer.info.MediaInfo;
import fr.free.movierenamer.info.MovieInfo;
import fr.free.movierenamer.ui.settings.UISettings;
import fr.free.movierenamer.ui.utils.UIUtils;
import fr.free.movierenamer.utils.LocaleUtils;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Class MoviePanel
 *
 * @author Magré Nicolas
 */
public class MoviePanel extends MediaPanel {

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private WebList castingList;
  private WebList countryList;
  private WebTextField directorField;
  private WebList fanartList;
  private WebToolBar fanartTb;
  private JScrollPane fanartsScrollPane;
  private WebTextField genreField;
  private JScrollPane jScrollPane1;
  private JScrollPane jScrollPane3;
  private WebToolBar movieTb;
  private WebTextField origTitleField;
  private WebTextField runtimeField;
  private JLabel star;
  private JLabel star1;
  private JLabel star2;
  private JLabel star3;
  private JLabel star4;
  private WebPanel starPanel;
  private JScrollPane synopsScroll;
  private JTextArea synopsisArea;
  private WebLabel synopsisLbl;
  private JLabel thumbLbl;
  private WebToolBar thumbnailTb;
  private WebList thumbnailsList;
  private JScrollPane thumbsScrollPane;
  private WebLabel titleLbl;
  private WebLabel webLabel1;
  private WebLabel webLabel2;
  private WebLabel webLabel5;
  private WebToolBar webToolBar3;
  private WebToolBar webToolBar4;
  private WebLabel yearLbl;
  // End of variables declaration//GEN-END:variables
  private static final long serialVersionUID = 1L;
  private final DefaultListModel castingModel = new DefaultListModel();
  private final DefaultListModel thumbnailsModel = new DefaultListModel();
  private final DefaultListModel fanartsModel = new DefaultListModel();
  private final DefaultListModel subTitleModel = new DefaultListModel();
  private final DefaultListModel audioModel = new DefaultListModel();
  private final DefaultListModel countryModel = new DefaultListModel();
  private final UISettings setting;
  private MediaInfo mediaInfo;

  /**
   * Creates new form MoviePanel
   *
   */
  public MoviePanel() {
    this.setting = UISettings.getInstance();

    // Init
    initComponents();

    mediaInfo = null;
//
//    origTitleField.setLeadingComponent(origTitleLbl);
//    directorField.setLeadingComponent(directorLbl);
//    runtimeField.setLeadingComponent(runtimeLbl);
//    genreField.setLeadingComponent(genreLbl);

    // Add component to toolbar
    movieTb.addToEnd(starPanel);

    thumbnailsList.setModel(thumbnailsModel);
    fanartList.setModel(fanartsModel);
    castingList.setModel(castingModel);
    countryList.setModel(countryModel);
    thumbnailsList.setCellRenderer(UIUtils.iconListRenderer);
    fanartList.setCellRenderer(UIUtils.iconListRenderer);
    castingList.setCellRenderer(UIUtils.iconListRenderer);

    countryList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    countryList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    countryList.setVisibleRowCount(-1);

    thumbnailsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    thumbnailsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    thumbnailsList.setVisibleRowCount(-1);
    thumbnailsList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        // TODO
      }
    });

    fanartList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    fanartList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    fanartList.setVisibleRowCount(-1);
    fanartList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent lse) {
        // TODO
      }
    });

    // TODO Add drag and drop image on thumbnail list

    // TODO Add drag and drop image on fanart list

    castingList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    castingList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    castingList.setVisibleRowCount(-1);

    // Disable drag and drop on list until a movie is added
//    dropFanartTarget.setActive(false);
//    dropThumbTarget.setActive(false);

//    thumbnailTb.setVisible(setting.thumb);
//    fanartTb.setVisible(setting.fanart);
//    thumbsScrollPane.setVisible(setting.thumb);
//    fanartsScrollPane.setVisible(setting.fanart);

  }

  @Override
  public void clear() {
    SwingUtilities.invokeLater(new Thread() {
      @Override
      public void run() {
//        dropFanartTarget.setActive(false);
//        dropThumbTarget.setActive(false);

        mediaInfo = null;
        castingModel.clear();
        thumbnailsModel.clear();
        fanartsModel.clear();

        subTitleModel.clear();
        audioModel.clear();
        countryModel.clear();
        origTitleField.setText("");
        yearLbl.setText("");
        runtimeField.setText("");
        synopsisArea.setText("");
        genreField.setText("");
        directorField.setText("");
        titleLbl.setText("");
        thumbLbl.setIcon(null);
        resetStar();
        validate();
        repaint();
      }
    });
  }



  @Override
  public void setMediaInfo(MediaInfo mediaInfo) {
    this.mediaInfo = mediaInfo;
    MovieInfo movieInfo = (MovieInfo) mediaInfo;
    origTitleField.setText(movieInfo.getOriginalTitle());
    for (String director : movieInfo.getDirectors()) {
      directorField.setText(directorField.getText() + (directorField.getText().isEmpty() ? "" : ",") + director);
    }
    for (String genre : movieInfo.getGenres()) {
      genreField.setText(genreField.getText() + (genreField.getText().isEmpty() ? "" : ",") + genre);
    }
    runtimeField.setText("" + movieInfo.getRuntime());
    //synopsisArea.setText(movieInfo.get);
  }

  @Override
  public MediaInfo getMediaInfo() {
    return mediaInfo;
  }

  @Override
  public WebList getCastingList() {
    return castingList;
  }

  @Override
  public WebList getThumbnailsList() {
    return thumbnailsList;
  }

  @Override
  public WebList getFanartsList() {
    return fanartList;
  }

  @Override
  public WebList getSubtitlesList() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * This method is called from within the constructor to initialize the form.
   */
  //WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    starPanel = new WebPanel();
    star4 = new JLabel();
    star3 = new JLabel();
    star2 = new JLabel();
    star1 = new JLabel();
    star = new JLabel();
    movieTb = new WebToolBar();
    titleLbl = new WebLabel();
    yearLbl = new WebLabel();
    thumbLbl = new JLabel();
    origTitleField = new WebTextField();
    directorField = new WebTextField();
    genreField = new WebTextField();
    webToolBar3 = new WebToolBar();
    webLabel5 = new WebLabel();
    jScrollPane3 = new JScrollPane();
    castingList = new WebList();
    webToolBar4 = new WebToolBar();
    synopsisLbl = new WebLabel();
    synopsScroll = new JScrollPane();
    synopsisArea = new JTextArea();
    runtimeField = new WebTextField();
    jScrollPane1 = new JScrollPane();
    countryList = new WebList(){
      public String getToolTipText(MouseEvent evt) {
        // Get item index
        int index = locationToIndex(evt.getPoint());

        // Get item
        ImageIcon item = (ImageIcon) getModel().getElementAt(index);

        // Return the tool tip text
        return item.getDescription();
      }
    };
    thumbnailTb = new WebToolBar();
    webLabel1 = new WebLabel();
    thumbsScrollPane = new JScrollPane();
    thumbnailsList = new WebList();
    fanartTb = new WebToolBar();
    webLabel2 = new WebLabel();
    fanartsScrollPane = new JScrollPane();
    fanartList = new WebList();

    starPanel.setAlignmentY(0.0F);

    star4.setIcon(UIUtils.STAR_EMPTY);

    star3.setIcon(UIUtils.STAR_EMPTY);

    star2.setIcon(UIUtils.STAR_EMPTY);

    star1.setIcon(UIUtils.STAR_EMPTY);

    star.setIcon(UIUtils.STAR_EMPTY);

    GroupLayout starPanelLayout = new GroupLayout(starPanel);
    starPanel.setLayout(starPanelLayout);
    starPanelLayout.setHorizontalGroup(
      starPanelLayout.createParallelGroup(Alignment.LEADING)
      .addGroup(starPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(star)
        .addGap(8, 8, 8)
        .addComponent(star1)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(star2)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(star3)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(star4)
        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    starPanelLayout.setVerticalGroup(
      starPanelLayout.createParallelGroup(Alignment.LEADING)
      .addComponent(star1)
      .addComponent(star2)
      .addComponent(star3)
      .addComponent(star4)
      .addComponent(star)
    );

    setMargin(new Insets(10, 10, 10, 10));
    setMinimumSize(new Dimension(10, 380));
    setPreferredSize(new Dimension(562, 400));

    movieTb.setFloatable(false);
    movieTb.setRollover(true);

    titleLbl.setFont(new Font("Ubuntu", 1, 14)); // NOI18N
    movieTb.add(titleLbl);

    yearLbl.setFont(new Font("Ubuntu", 1, 14)); // NOI18N
    movieTb.add(yearLbl);

    thumbLbl.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    origTitleField.setEditable(false);

    directorField.setEditable(false);

    webToolBar3.setFloatable(false);
    webToolBar3.setRollover(true);

    webLabel5.setText(LocaleUtils.i18nExt("actor")); // NOI18N
    webLabel5.setFont(new Font("Ubuntu", 1, 13)); // NOI18N
    webToolBar3.add(webLabel5);

    castingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jScrollPane3.setViewportView(castingList);

    webToolBar4.setFloatable(false);
    webToolBar4.setRollover(true);

    synopsisLbl.setText(LocaleUtils.i18nExt("Synopsis")); // NOI18N
    synopsisLbl.setFont(new Font("Ubuntu", 1, 13)); // NOI18N
    webToolBar4.add(synopsisLbl);

    synopsScroll.setPreferredSize(new Dimension(264, 62));

    synopsisArea.setEditable(false);
    synopsisArea.setColumns(20);
    synopsisArea.setLineWrap(true);
    synopsisArea.setRows(4);
    synopsisArea.setWrapStyleWord(true);
    synopsisArea.setBorder(null);
    synopsisArea.setOpaque(false);
    synopsScroll.setViewportView(synopsisArea);

    countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    countryList.setAutoscrolls(false);
    countryList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    jScrollPane1.setViewportView(countryList);

    thumbnailTb.setFloatable(false);
    thumbnailTb.setRollover(true);

    webLabel1.setText(LocaleUtils.i18nExt("thumbnails")); // NOI18N
    webLabel1.setFont(new Font("Ubuntu", 1, 13)); // NOI18N
    thumbnailTb.add(webLabel1);

    thumbnailsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    thumbsScrollPane.setViewportView(thumbnailsList);

    fanartTb.setFloatable(false);
    fanartTb.setRollover(true);
    fanartTb.setFont(new Font("Ubuntu", 1, 13)); // NOI18N

    webLabel2.setText(LocaleUtils.i18nExt("fanart")); // NOI18N
    webLabel2.setFont(new Font("Ubuntu", 1, 13)); // NOI18N
    fanartTb.add(webLabel2);

    fanartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    fanartsScrollPane.setViewportView(fanartList);

    GroupLayout layout = new GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(Alignment.LEADING)
      .addComponent(movieTb, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addComponent(thumbLbl, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(Alignment.LEADING)
          .addComponent(directorField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(genreField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPane3)
          .addComponent(origTitleField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(webToolBar3, GroupLayout.PREFERRED_SIZE, 47, Short.MAX_VALUE)
            .addGap(9, 9, 9)
            .addComponent(runtimeField, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.UNRELATED)
            .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE))))
      .addComponent(synopsScroll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(webToolBar4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(Alignment.LEADING)
          .addComponent(thumbsScrollPane, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
          .addComponent(thumbnailTb, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGap(26, 26, 26)
        .addGroup(layout.createParallelGroup(Alignment.LEADING)
          .addComponent(fanartsScrollPane, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
          .addComponent(fanartTb, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(Alignment.LEADING)
      .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        .addComponent(movieTb, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
          .addComponent(thumbLbl, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(origTitleField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(directorField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(genreField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(Alignment.TRAILING)
              .addComponent(webToolBar3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addComponent(runtimeField, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(ComponentPlacement.RELATED)
            .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(webToolBar4, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(synopsScroll, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        .addPreferredGap(ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(Alignment.TRAILING)
          .addComponent(thumbnailTb, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
          .addComponent(fanartTb, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(Alignment.LEADING)
          .addComponent(thumbsScrollPane, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
          .addComponent(fanartsScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
    );
  }// </editor-fold>//GEN-END:initComponents

  @Override
  public DefaultListModel getCastingModel() {
    return castingModel;
  }

  @Override
  public DefaultListModel getThumbnailsModel() {
    return thumbnailsModel;
  }

  @Override
  public DefaultListModel getFanartsModel() {
    return fanartsModel;
  }

  @Override
  public DefaultListModel getSubtitlesModel() {
    return null;
  }

  @Override
  public WebList getBannersList() {
    return null;
  }

  @Override
  public WebList getCdartsList() {
    return null;
  }

  @Override
  public WebList getLogosList() {
    return null;
  }

  @Override
  public WebList getClearartsList() {
    return null;
  }

  @Override
  public DefaultListModel getBannersModel() {
    return null;
  }

  @Override
  public DefaultListModel getCdartsModel() {
    return null;
  }

  @Override
  public DefaultListModel getLogosModel() {
    return null;
  }

  @Override
  public DefaultListModel getClearartsModel() {
    return null;
  }
}
