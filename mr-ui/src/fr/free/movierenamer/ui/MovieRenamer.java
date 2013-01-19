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
package fr.free.movierenamer.ui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SeparatorList;
import ca.odell.glazedlists.swing.EventListModel;
import com.alee.extended.filechooser.FilesToChoose;
import com.alee.extended.filechooser.SelectionMode;
import com.alee.extended.filechooser.WebFileChooser;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.effects.fade.FadeTransitionEffect;
import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.DefaultListModel;
import com.alee.laf.list.WebList;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextField;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.popup.PopupWay;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import fr.free.movierenamer.info.MediaInfo;
import fr.free.movierenamer.scrapper.MediaScrapper;
import fr.free.movierenamer.scrapper.MovieScrapper;
import fr.free.movierenamer.scrapper.TvShowScrapper;
import fr.free.movierenamer.scrapper.impl.ScrapperManager;
import fr.free.movierenamer.searchinfo.Media;
import fr.free.movierenamer.settings.Settings;
import fr.free.movierenamer.ui.list.DragAndDrop;
import fr.free.movierenamer.ui.list.IIconList;
import fr.free.movierenamer.ui.list.IconListRenderer;
import fr.free.movierenamer.ui.list.UIFile;
import fr.free.movierenamer.ui.list.UILoader;
import fr.free.movierenamer.ui.list.UIScrapper;
import fr.free.movierenamer.ui.list.UISearchResult;
import fr.free.movierenamer.ui.panel.LogPanel;
import fr.free.movierenamer.ui.panel.MediaPanel;
import fr.free.movierenamer.ui.panel.MoviePanel;
import fr.free.movierenamer.ui.panel.SettingPanel;
import fr.free.movierenamer.ui.panel.TvShowPanel;
import fr.free.movierenamer.ui.res.FileFilter;
import fr.free.movierenamer.ui.res.UIMode;
import fr.free.movierenamer.ui.settings.UISettings;
import fr.free.movierenamer.ui.settings.UISettings.SettingPropertyChange;
import fr.free.movierenamer.ui.settings.UISettings.UISettingsProperty;
import fr.free.movierenamer.ui.utils.ImageUtils;
import fr.free.movierenamer.ui.utils.UIUtils;
import fr.free.movierenamer.ui.worker.ListFilesWorker;
import fr.free.movierenamer.ui.worker.SearchMediaImagesWorker;
import fr.free.movierenamer.ui.worker.SearchMediaInfoWorker;
import fr.free.movierenamer.ui.worker.SearchMediaWorker;
import fr.free.movierenamer.utils.LocaleUtils;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToolBar.Separator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Class MovieRenamer
 *
 * @author Nicolas Magré
 * @author Simon QUÉMÉNEUR
 */
public class MovieRenamer extends JFrame {

  private static final long serialVersionUID = 1L;
  private final UISettings setting = UISettings.getInstance();
  // Current variables
  private UIMode currentMode;
  private UIFile currentMedia;
  private boolean groupFile;
  // Scrapper
  private UIScrapper UIMovieScrapper = new UIScrapper(ScrapperManager.getScrapper(setting.coreInstance.getSearchMovieScrapper()));
  private UIScrapper UITvShowScrapper = new UIScrapper(ScrapperManager.getScrapper(setting.coreInstance.getSearchTvshowScrapper()));
  // Media Panel
  private final MoviePanel moviePnl;
  private final TvShowPanel tvShowPanel;
  private final ComponentTransition containerTransitionMediaPanel;// Media Panel container
  // Log Panel
  private final LogPanel logPanel;
  // File chooser
  private final WebFileChooser fileChooser;
  // UI tools
  public static final Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
  public static final Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
  // Clear interface
  private final boolean CLEAR_MEDIALIST = true;
  private final boolean CLEAR_SEARCHRESULTLIST = true;
  // Worker queue
  private Queue<SwingWorker<?, ?>> workerQueue;
  // Model
  private EventList<UIFile> mediaFileEventList = new BasicEventList<UIFile>();
  private final DefaultListModel searchResultModel = new DefaultListModel();
  private final DefaultComboBoxModel movieScrapperModel;
  private final DefaultComboBoxModel tvshowScrapperModel;
  private final EventListModel<UIFile> mediaFileSeparatorModel;
  private final EventListModel<UIFile> mediaFileModel;
  // Separator
  private final SeparatorList<UIFile> mediaFileSeparator;
  // Loader
  private final IconListRenderer<IIconList> loaderListRenderer = new IconListRenderer<IIconList>(true);
  private final DefaultListModel loaderModel = new DefaultListModel();
  // List option checkbox
  private final WebCheckBox showIconMediaListChk;
  private final WebCheckBox showIconResultListChk;
  private final WebCheckBox showFormatFieldChk;
  // Property change
  private final PropertyChangeSupport settingsChange;

  public MovieRenamer() {

    // Cache.clearAllCache();//FIXME remove !!!

    logPanel = new LogPanel();
    moviePnl = new MoviePanel(this);
    tvShowPanel = new TvShowPanel(this);
    workerQueue = new LinkedList<SwingWorker<?, ?>>();
    movieScrapperModel = new DefaultComboBoxModel();
    tvshowScrapperModel = new DefaultComboBoxModel();
    mediaFileSeparator = new SeparatorList<UIFile>(mediaFileEventList, UIUtils.groupFileComparator, 1, 1000);
    settingsChange = new PropertyChangeSupport(setting);
    settingsChange.addPropertyChangeListener(createSettingPropertyChangeListener());
    mediaFileSeparatorModel = new EventListModel<UIFile>(mediaFileSeparator);
    mediaFileModel = new EventListModel<UIFile>(mediaFileEventList);

    UISettings.LOGGER.addHandler(logPanel.getHandler());
    Settings.LOGGER.addHandler(logPanel.getHandler());

    // Set Movie Renamer mode
    currentMode = UIMode.MOVIEMODE;

    // Create media panel container and set transition effect
    containerTransitionMediaPanel = new ComponentTransition(moviePnl);
    containerTransitionMediaPanel.setTransitionEffect(new FadeTransitionEffect());

    fileChooser = new WebFileChooser(this, LocaleUtils.i18nExt("chooseFile"));// FIXME i18n

    initComponents();

    // List option
    showIconMediaListChk = createShowIconChk(mediaFileList, setting.isShowIconMediaList());
    showIconResultListChk = createShowIconChk(searchResultList, setting.isShowThumb());
    showFormatFieldChk = new WebCheckBox(LocaleUtils.i18nExt("showFormatField"));// FIXME i18n
    showFormatFieldChk.setSelected(setting.isShowFormatField());
    showFormatFieldChk.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (showFormatFieldChk.isSelected()) {
          renameTb.add(fileFormatField);
        } else {
          renameTb.remove(fileFormatField);
        }
        renameTb.revalidate();
        renameTb.repaint();
      }
    });

    init();
    renameTb.remove(fileFormatField);

    changeMediaPanel();

    setIconImage(ImageUtils.iconToImage(ImageUtils.LOGO_32));
    setLocationRelativeTo(null);
    setTitle(UISettings.APPNAME + "-" + setting.getVersion() + " " + currentMode.getTitleMode());
    setVisible(true);

    // Check for Movie Renamer update
    if (setting.isCheckUpdate()) {
//      checkUpdate(false);
    }
  }

  private void init() {

    // Add button to main toolbar on right
    mainTb.addToEnd(helpBtn);
    mainTb.addToEnd(logsBtn);
    mainTb.addToEnd(new JSeparator(JSeparator.VERTICAL));
    mainTb.addToEnd(updateBtn);
    mainTb.addToEnd(settingBtn);
    mainTb.addToEnd(exitBtn);

    // Add tooltip
    TooltipManager.setTooltip(openBtn, new WebLabel(LocaleUtils.i18nExt("openFolderBtn"), ImageUtils.FOLDERVIDEO_16), TooltipWay.down);
    //TooltipManager.setTooltip(editBtn, new WebLabel(LocaleUtils.i18nExt("edit"), new ImageIcon(getClass().getResource("/image/accessories-text-editor-6-24.png")), SwingConstants.TRAILING), TooltipWay.down);
    TooltipManager.setTooltip(movieModeBtn, new WebLabel(LocaleUtils.i18nExt("movieMode"), ImageUtils.MOVIE_16, SwingConstants.TRAILING), TooltipWay.down);
    TooltipManager.setTooltip(tvShowModeBtn, new WebLabel(LocaleUtils.i18nExt("tvshowMode"), ImageUtils.TV_16, SwingConstants.TRAILING), TooltipWay.down);
    TooltipManager.setTooltip(helpBtn, new WebLabel(LocaleUtils.i18nExt("help"), ImageUtils.HELP_16, SwingConstants.TRAILING), TooltipWay.down);
    TooltipManager.setTooltip(updateBtn, new WebLabel(LocaleUtils.i18nExt("updateBtn"), ImageUtils.UPDATE_16, SwingConstants.TRAILING), TooltipWay.down);
    TooltipManager.setTooltip(logsBtn, new WebLabel(LocaleUtils.i18nExt("logsBtn"), ImageUtils.LOGS_16, SwingConstants.TRAILING), TooltipWay.down);
    TooltipManager.setTooltip(settingBtn, new WebLabel(LocaleUtils.i18nExt("settingBtn"), ImageUtils.SETTING_16, SwingConstants.TRAILING), TooltipWay.down);
    TooltipManager.setTooltip(exitBtn, new WebLabel(LocaleUtils.i18nExt("exitBtn"), ImageUtils.APPLICATIONEXIT_16, SwingConstants.TRAILING), TooltipWay.down);
    TooltipManager.setTooltip(searchBtn, new WebLabel(LocaleUtils.i18nExt("search"), ImageUtils.SEARCH_16, SwingConstants.TRAILING), TooltipWay.down);
    TooltipManager.setTooltip(renameBtn, new WebLabel(LocaleUtils.i18nExt("rename"), ImageUtils.OK_16, SwingConstants.TRAILING), TooltipWay.up);

    // Add media panel container to media split pane
    mediaSp.setBottomComponent(containerTransitionMediaPanel);
    fileFormatField.setText(setting.coreInstance.getMovieFilenameFormat());

    // Init Movie Scrapper model
    for (MovieScrapper scrapper : ScrapperManager.getMovieScrapperList()) {
      movieScrapperModel.addElement(new UIScrapper(scrapper));
    }

    // Init TvShow Scrapper model
    for (TvShowScrapper scrapper : ScrapperManager.getTvShowScrapperList()) {
      tvshowScrapperModel.addElement(new UIScrapper(scrapper));
    }

    scrapperCb.setRenderer(UIUtils.iconListRenderer);
    scrapperCb.setModel(movieScrapperModel);
    scrapperCb.setSelectedItem(UIMovieScrapper);

    //Add drag and drop listener on mediaFileList
    DragAndDrop dropFile = new DragAndDrop(this, mediaFileList) {
      @Override
      public void getFiles(List<File> files) {
        loadFiles(files);
      }
    };
    DropTarget dt = new DropTarget(mediaFileList, dropFile);
    dt.setActive(true);

    mediaFileList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent lse) {
        if (!lse.getValueIsAdjusting()) {
          if (currentMedia == getSelectedMediaFile()) {
            return;
          }

          currentMedia = getSelectedMediaFile();
          if (currentMedia == null) {
            return;
          }

          moviePnl.clearMediaTag();
          moviePnl.setMediaTag(currentMedia.getMediaTag());

          searchField.setText(currentMedia.getSearch());
          searchMedia();
        }
      }
    });
    mediaFileList.setCellRenderer(new IconListRenderer<IIconList>(false));

    searchResultList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent lse) {
        if (!lse.getValueIsAdjusting()) {
          searchMediaInfo();
        }
      }
    });
    searchResultList.setModel(searchResultModel);
    searchResultList.setCellRenderer(new IconListRenderer<IIconList>(false));

    // file chooser init
    fileChooser.setFilesToChoose(FilesToChoose.all);
    fileChooser.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
    FileFilter ff = new FileFilter();
    fileChooser.setPreviewFilter(ff);
    fileChooser.setChooseFilter(ff);

    loaderModel.addElement(new UILoader(mediaFileList, 0));
    toggleGroup.setText("togglegroup");
    toggleGroup.setPreferredSize(new Dimension(24, 20));
    toggleGroup.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        int index = mediaFileList.getSelectedIndex();
        Object obj = mediaFileList.getSelectedValue();
        groupFile = !groupFile;

        ((IconListRenderer) mediaFileList.getCellRenderer()).showGroup(groupFile);
        mediaFileList.setModel(groupFile ? mediaFileSeparatorModel : mediaFileModel);

        if (index > -1 && obj != null) {
          mediaFileList.setSelectedValue(obj, false);
          mediaFileList.ensureIndexIsVisible(mediaFileList.getSelectedIndex());
        }
        mediaFileList.revalidate();
        toggleGroup.setIcon(groupFile ? ImageUtils.FILEVIEW_16 : ImageUtils.GROUPVIEW_16);
      }
    });
    groupFile = setting.isGroupMediaList();

    renameTb.addToEnd(UIUtils.createSettingbutton(PopupWay.upLeft, "settingHelp", thumbChk, fanartChk, nfoChk, showFormatFieldChk));
    mediaFileTb.addToEnd(UIUtils.createSettingbutton(PopupWay.downRight, "settingHelp", toggleGroup, showIconMediaListChk));
    searchTb.addToEnd(UIUtils.createSettingbutton(PopupWay.downLeft, "settingHelp", showIconResultListChk));
  }

  private WebCheckBox createShowIconChk(final WebList list, boolean selected) {
    final WebCheckBox checkbox = new WebCheckBox(LocaleUtils.i18nExt("showIcon"));
    checkbox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        ((IconListRenderer) list.getCellRenderer()).showIcon(checkbox.isSelected());
        list.revalidate();
        list.repaint();
      }
    });
    checkbox.setSelected(selected);
    return checkbox;
  }

  private PropertyChangeListener createSettingPropertyChangeListener() {
    PropertyChangeListener settingListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent pce) {
        switch (SettingPropertyChange.valueOf(pce.getPropertyName())) {
          case SEARCHMOVIESCRAPPER:
            UIMovieScrapper = new UIScrapper(ScrapperManager.getMovieScrapper());
            if (currentMode.equals(UIMode.MOVIEMODE)) {
              scrapperCb.setSelectedItem(UIMovieScrapper);
            }
            break;
          case SEARCHMTVSHOWSCRAPPER:
            UITvShowScrapper = new UIScrapper(ScrapperManager.getTvShowScrapper());
            if (currentMode.equals(UIMode.TVSHOWMODE)) {
              scrapperCb.setSelectedItem(UITvShowScrapper);
            }
            break;
        }
      }
    };
    return settingListener;
  }

  /**
   *
   * @param files
   */
  private void loadFiles(List<File> files) {
    clearInterface(CLEAR_MEDIALIST, CLEAR_SEARCHRESULTLIST);

    // Add loader image
    mediaFileList.setModel(loaderModel);
    mediaFileList.setCellRenderer(loaderListRenderer);

    mediaLoaderLbl.setIcon(ImageUtils.LOADER_16);

    ListFilesWorker listFileWorker = new ListFilesWorker(this, files, mediaFileList, mediaFileEventList, groupFile ? mediaFileSeparatorModel : mediaFileModel);
    listFileWorker.execute();
    workerQueue.add(listFileWorker);
  }

  /**
   * Search media on web
   */
  private void searchMedia() {

    if (currentMedia == null) {
      return;
    }

    String search = searchField.getText();
    if (search.length() == 0) {
      WebOptionPane.showMessageDialog(MovieRenamer.this, LocaleUtils.i18n("noTextToSearch"), LocaleUtils.i18n("error"), JOptionPane.ERROR_MESSAGE);// FIXME i18n
      return;
    }

    clearInterface(!CLEAR_MEDIALIST, CLEAR_SEARCHRESULTLIST);

    // Add loader image
    searchResultModel.addElement(new UILoader(searchResultList, 0));
    searchResultList.setCellRenderer(loaderListRenderer);

    currentMedia.setSearch(search);

    MediaScrapper<? extends Media, ? extends MediaInfo> mediaScrapper = ((UIScrapper) scrapperCb.getSelectedItem()).getScrapper();
    SearchMediaWorker searchWorker = new SearchMediaWorker(this, currentMedia, mediaScrapper, searchResultList, searchBtn, searchField, searchResultModel);
    searchWorker.execute();
    
    workerQueue.add(searchWorker);
  }

  private void searchMediaInfo() {
    UISearchResult searchResult = getSelectedSearchResult();
    if (searchResult == null) {
      return;
    }

    MediaPanel ipanel = getCurrentMediaPanel();
    if (ipanel == null) {
      return;
    }

    SearchMediaInfoWorker infoWorker = new SearchMediaInfoWorker(this, currentMedia, searchResult);
    SearchMediaImagesWorker imagesWorker = new SearchMediaImagesWorker(this, searchResult);
    //subtitleWorker = new SearchMediaSubtitlesWorker(currentMedia, null);

    infoWorker.execute();
    imagesWorker.execute();

    workerQueue.add(infoWorker);
    workerQueue.add(imagesWorker);
  }

  private void cancelWorker() {
    SwingWorker<?, ?> worker;
    worker = workerQueue.poll();
    while (worker != null) {
      worker.cancel(true);
      worker = workerQueue.poll();
    }
  }

  public synchronized void addWorker(SwingWorker<?, ?> worker) {
    workerQueue.add(worker);
  }

  public UIFile getSelectedMediaFile() {
    UIFile current = null;
    if (mediaFileList != null) {
      Object obj = mediaFileList.getSelectedValue();
      if (obj != null) {
        if (obj instanceof UIFile) {
          current = (UIFile) obj;
          mediaFileList.ensureIndexIsVisible(mediaFileList.getSelectedIndex());
        }
      }
    }
    return current;
  }

  public UISearchResult getSelectedSearchResult() {
    UISearchResult current = null;
    if (searchResultList != null) {
      Object obj = searchResultList.getSelectedValue();
      if (obj != null) {
        if (obj instanceof UISearchResult) {
          current = (UISearchResult) obj;
          searchResultList.ensureIndexIsVisible(searchResultList.getSelectedIndex());
        }
      }
    }
    return current;
  }

  private void changeMediaPanel() {
    movieModeBtn.setEnabled(false);
    tvShowModeBtn.setEnabled(false);
    switch (currentMode) {
      case MOVIEMODE:
        containerTransitionMediaPanel.performTransition(moviePnl);
        tvShowModeBtn.setEnabled(true);
        scrapperCb.setModel(movieScrapperModel);
        scrapperCb.setSelectedItem(UIMovieScrapper);
        fileFormatField.setText(setting.coreInstance.getMovieFilenameFormat());
        break;
      case TVSHOWMODE:
        containerTransitionMediaPanel.performTransition(tvShowPanel);
        movieModeBtn.setEnabled(true);
        scrapperCb.setModel(tvshowScrapperModel);
        scrapperCb.setSelectedItem(UITvShowScrapper);
        fileFormatField.setText(setting.coreInstance.getTvShowFilenameFormat());
        break;
    }

    setTitle(UISettings.APPNAME + "-" + setting.getVersion() + " " + currentMode.getTitleMode());
  }

  public MediaPanel getCurrentMediaPanel() {
    MediaPanel current = null;
    Component compo = containerTransitionMediaPanel.getContent();
    if (compo != null) {
      if (compo instanceof MediaPanel) {
        current = (MediaPanel) compo;
      }
    }
    return current;
  }

  public void updateRenamedTitle() {// TODO
    MediaInfo mediaInfo = getCurrentMediaPanel().getMediaInfo();
    if (mediaInfo != null) {
      renameField.setText(mediaInfo.getRenamedTitle(fileFormatField.getText()));
    } else {
      renameField.setText(null);
    }
  }

  /**
   * Clear Movie Renamer interface
   *
   * @param mediaList Clear media list
   * @param searchList Clear search list
   */
  private void clearInterface(boolean clearMediaList, boolean clearSearchResultList) {
    // Stop all running workers
    cancelWorker();

    if (clearMediaList) {
      searchField.setText(null);
      mediaFileEventList.clear();
    }

    if (clearSearchResultList) {
      searchBtn.setEnabled(false);
      searchField.setEnabled(false);
      searchResultModel.removeAllElements();
    }

    moviePnl.clear();
    tvShowPanel.clear();
    updateRenamedTitle();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    updateBtn = new WebButton();
    settingBtn = new WebButton();
    exitBtn = new WebButton();
    helpBtn = new WebButton();
    renameField = new JTextField();
    thumbChk = new WebCheckBox();
    fanartChk = new WebCheckBox();
    nfoChk = new WebCheckBox();
    toggleGroup = new WebButton();
    fileFormatField = new WebTextField();
    logsBtn = new WebButton();
    mainTb = new WebToolBar();
    openBtn = new WebButton();
    openSep = new WebSeparator();
    movieModeBtn = new WebButton();
    tvShowModeBtn = new WebButton();
    modeSep = new Separator();
    renameTb = new WebToolBar();
    renameBtn = new WebButton();
    listSp = new WebSplitPane();
    mediaSp = new WebSplitPane();
    searchPnl = new WebPanel();
    searchTb = new WebToolBar();
    searchLbl = new WebLabel();
    searchBtn = new WebButton();
    searchField = new WebTextField();
    jScrollPane1 = new JScrollPane();
    searchResultList = new WebList();
    scrapperCb = new JComboBox();
    mediaFilePnl = new WebPanel();
    mediaFileTb = new WebToolBar();
    mediaLbl = new WebLabel();
    mediaLoaderLbl = new WebLabel();
    mediaFileScp = new JScrollPane();
    mediaFileList = new WebList();

    updateBtn.setIcon(ImageUtils.UPDATE_24);
    updateBtn.setFocusable(false);
    updateBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    updateBtn.setRolloverDarkBorderOnly(true);
    updateBtn.setRolloverDecoratedOnly(true);
    updateBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    updateBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        updateBtnActionPerformed(evt);
      }
    });

    settingBtn.setIcon(ImageUtils.SETTING_24);
    settingBtn.setFocusable(false);
    settingBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    settingBtn.setRolloverDarkBorderOnly(true);
    settingBtn.setRolloverDecoratedOnly(true);
    settingBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    settingBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        settingBtnActionPerformed(evt);
      }
    });

    exitBtn.setIcon(ImageUtils.APPLICATIONEXIT_24);
    exitBtn.setFocusable(false);
    exitBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    exitBtn.setRolloverDarkBorderOnly(true);
    exitBtn.setRolloverDecoratedOnly(true);
    exitBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    exitBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        exitBtnActionPerformed(evt);
      }
    });

    helpBtn.setIcon(ImageUtils.HELP_24);
    helpBtn.setFocusable(false);
    helpBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    helpBtn.setRolloverDarkBorderOnly(true);
    helpBtn.setRolloverDecoratedOnly(true);
    helpBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    helpBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        helpBtnActionPerformed(evt);
      }
    });

    renameField.setEnabled(false);

    thumbChk.setText(LocaleUtils.i18nExt("thumb")); // NOI18N
    thumbChk.setFocusable(false);

    fanartChk.setText(LocaleUtils.i18nExt("fanart")); // NOI18N
    fanartChk.setFocusable(false);

    nfoChk.setText(LocaleUtils.i18nExt("xbmcNfo")); // NOI18N
    nfoChk.setFocusable(false);

    toggleGroup.setIcon(ImageUtils.FILEVIEW_16);
    toggleGroup.setUndecorated(true);

    fileFormatField.setPreferredSize(new Dimension(250, 27));
    fileFormatField.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent evt) {
        fileFormatFieldKeyReleased(evt);
      }
    });

    logsBtn.setIcon(ImageUtils.LOGS_24);
    logsBtn.setFocusable(false);
    logsBtn.setRolloverDarkBorderOnly(true);
    logsBtn.setRolloverDecoratedOnly(true);
    logsBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        logsBtnActionPerformed(evt);
      }
    });

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(770, 570));

    mainTb.setFloatable(false);
    mainTb.setRollover(true);
    mainTb.setMargin(new Insets(4, 4, 4, 4));
    mainTb.setRound(10);

    openBtn.setIcon(ImageUtils.FOLDERVIDEO_24);
    openBtn.setFocusable(false);
    openBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    openBtn.setRolloverDarkBorderOnly(true);
    openBtn.setRolloverDecoratedOnly(true);
    openBtn.setRolloverShadeOnly(true);
    openBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    openBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        openBtnActionPerformed(evt);
      }
    });
    mainTb.add(openBtn);
    mainTb.add(openSep);

    movieModeBtn.setIcon(ImageUtils.MOVIE_24);
    movieModeBtn.setFocusable(false);
    movieModeBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    movieModeBtn.setRolloverDarkBorderOnly(true);
    movieModeBtn.setRolloverDecoratedOnly(true);
    movieModeBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    movieModeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        movieModeBtnActionPerformed(evt);
      }
    });
    mainTb.add(movieModeBtn);

    tvShowModeBtn.setIcon(ImageUtils.TV_24);
    tvShowModeBtn.setFocusable(false);
    tvShowModeBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    tvShowModeBtn.setRolloverDarkBorderOnly(true);
    tvShowModeBtn.setRolloverDecoratedOnly(true);
    tvShowModeBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    tvShowModeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        tvShowModeBtnActionPerformed(evt);
      }
    });
    mainTb.add(tvShowModeBtn);
    mainTb.add(modeSep);

    getContentPane().add(mainTb, BorderLayout.PAGE_START);

    renameTb.setFloatable(false);
    renameTb.setRollover(true);
    renameTb.setRound(10);

    renameBtn.setIcon(ImageUtils.OK_24);
    renameBtn.setText(LocaleUtils.i18nExt("rename")); // NOI18N
    renameBtn.setEnabled(false);
    renameBtn.setFocusable(false);
    renameBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        renameBtnActionPerformed(evt);
      }
    });
    renameTb.add(renameBtn);
    renameTb.add(fileFormatField);
    //Add rename text field
    renameTb.add(renameField, ToolbarLayout.FILL);

    getContentPane().add(renameTb, BorderLayout.PAGE_END);

    mediaSp.setOrientation(JSplitPane.VERTICAL_SPLIT);

    searchPnl.setMargin(new Insets(10, 10, 10, 10));

    searchTb.setFloatable(false);
    searchTb.setRollover(true);
    searchTb.setMargin(new Insets(3, 3, 3, 3));
    searchTb.setRound(5);

    searchLbl.setText(LocaleUtils.i18nExt("search")); // NOI18N
    searchLbl.setFont(new Font("Ubuntu", 1, 14)); // NOI18N
    searchTb.add(searchLbl);

    searchBtn.setIcon(ImageUtils.SEARCH_16);
    searchBtn.setEnabled(false);
    searchBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        searchBtnActionPerformed(evt);
      }
    });

    searchField.setEnabled(false);
    searchField.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent evt) {
        searchFieldKeyReleased(evt);
      }
    });

    searchResultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jScrollPane1.setViewportView(searchResultList);

    scrapperCb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        scrapperCbActionPerformed(evt);
      }
    });

    GroupLayout searchPnlLayout = new GroupLayout(searchPnl);
    searchPnl.setLayout(searchPnlLayout);
    searchPnlLayout.setHorizontalGroup(
      searchPnlLayout.createParallelGroup(Alignment.LEADING)
      .addComponent(searchTb, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(searchPnlLayout.createSequentialGroup()
        .addComponent(searchField, GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(scrapperCb, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.UNRELATED)
        .addComponent(searchBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
      .addComponent(jScrollPane1, Alignment.TRAILING)
    );
    searchPnlLayout.setVerticalGroup(
      searchPnlLayout.createParallelGroup(Alignment.LEADING)
      .addGroup(searchPnlLayout.createSequentialGroup()
        .addComponent(searchTb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addGroup(searchPnlLayout.createParallelGroup(Alignment.TRAILING)
          .addGroup(searchPnlLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(scrapperCb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
          .addComponent(searchBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
    );

    searchField.setLeadingComponent(new JLabel(ImageUtils.SEARCH_16));

    mediaSp.setTopComponent(searchPnl);

    mediaSp.setContinuousLayout(true);

    listSp.setRightComponent(mediaSp);

    mediaFilePnl.setMargin(new Insets(10, 10, 10, 10));
    mediaFilePnl.setMinimumSize(new Dimension(60, 0));

    mediaFileTb.setFloatable(false);
    mediaFileTb.setRollover(true);
    mediaFileTb.setMargin(new Insets(2, 5, 2, 5));
    mediaFileTb.setRound(5);

    mediaLbl.setText(LocaleUtils.i18nExt("media")); // NOI18N
    mediaLbl.setFont(new Font("Ubuntu", 1, 14)); // NOI18N
    mediaFileTb.add(mediaLbl);
    mediaFileTb.add(mediaLoaderLbl);

    mediaFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    mediaFileScp.setViewportView(mediaFileList);

    GroupLayout mediaFilePnlLayout = new GroupLayout(mediaFilePnl);
    mediaFilePnl.setLayout(mediaFilePnlLayout);
    mediaFilePnlLayout.setHorizontalGroup(
      mediaFilePnlLayout.createParallelGroup(Alignment.LEADING)
      .addComponent(mediaFileTb, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
      .addComponent(mediaFileScp, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
    );
    mediaFilePnlLayout.setVerticalGroup(
      mediaFilePnlLayout.createParallelGroup(Alignment.LEADING)
      .addGroup(mediaFilePnlLayout.createSequentialGroup()
        .addComponent(mediaFileTb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(mediaFileScp, GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE))
    );

    listSp.setLeftComponent(mediaFilePnl);

    listSp.setContinuousLayout(true);

    getContentPane().add(listSp, BorderLayout.CENTER);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void openBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_openBtnActionPerformed
    fileChooser.setCurrentDirectory(new File(setting.getFileChooserPath()));
    int r = fileChooser.showDialog();
    if (r == 0) {
      List<File> files = fileChooser.getSelectedFiles();
      if (!files.isEmpty()) {// Remember path
        try {
          UISettingsProperty.fileChooserPath.setValue(files.get(0).getParent());
        } catch (IOException e) {
          UISettings.LOGGER.log(Level.SEVERE, "Failed to save current folder path");// FIXME i18n
        }
      }

      loadFiles(files);
    }
  }//GEN-LAST:event_openBtnActionPerformed

  private void movieModeBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_movieModeBtnActionPerformed
    currentMode = UIMode.MOVIEMODE;
    changeMediaPanel();
  }//GEN-LAST:event_movieModeBtnActionPerformed

  private void tvShowModeBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_tvShowModeBtnActionPerformed
    currentMode = UIMode.TVSHOWMODE;
    changeMediaPanel();
  }//GEN-LAST:event_tvShowModeBtnActionPerformed

  private void exitBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
    System.exit(0);
  }//GEN-LAST:event_exitBtnActionPerformed

  private void settingBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_settingBtnActionPerformed
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        SettingPanel pnl = new SettingPanel(MovieRenamer.this, settingsChange);
        pnl.setLocationRelativeTo(MovieRenamer.this);// Ensure that settingsPanel will be centered on Movie Renamer
        pnl.setVisible(true);
      }
    });  }//GEN-LAST:event_settingBtnActionPerformed

  private void updateBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
    //checkUpdate(true); // TODO
  }//GEN-LAST:event_updateBtnActionPerformed

  private void helpBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_helpBtnActionPerformed
    TooltipManager.showOneTimeTooltip(mediaFileList, new Point(mediaFileList.getWidth() / 2, mediaFileList.getHeight() / 2), "Media list help", TooltipWay.up);// FIXME i18n
    TooltipManager.showOneTimeTooltip(searchResultList, new Point(searchResultList.getWidth() / 2, searchResultList.getHeight() / 2), "searchResultList list help", TooltipWay.up);
    TooltipManager.showOneTimeTooltip(fileFormatField, new Point(fileFormatField.getWidth() / 2, fileFormatField.getHeight()), "Change filename on the fly", TooltipWay.down);
  }//GEN-LAST:event_helpBtnActionPerformed

  private void renameBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_renameBtnActionPerformed
  }//GEN-LAST:event_renameBtnActionPerformed

  private void searchBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
    searchMedia();
  }//GEN-LAST:event_searchBtnActionPerformed

  private void searchFieldKeyReleased(KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
      searchBtnActionPerformed(null);
    }
  }//GEN-LAST:event_searchFieldKeyReleased

  private void fileFormatFieldKeyReleased(KeyEvent evt) {//GEN-FIRST:event_fileFormatFieldKeyReleased
    updateRenamedTitle();
  }//GEN-LAST:event_fileFormatFieldKeyReleased

  private void scrapperCbActionPerformed(ActionEvent evt) {//GEN-FIRST:event_scrapperCbActionPerformed
    switch (currentMode) {
      case MOVIEMODE:
        UIMovieScrapper = new UIScrapper(((UIScrapper) scrapperCb.getModel().getSelectedItem()).getScrapper());
        break;
      case TVSHOWMODE:
        UITvShowScrapper = new UIScrapper(((UIScrapper) scrapperCb.getSelectedItem()).getScrapper());
        break;
    }
  }//GEN-LAST:event_scrapperCbActionPerformed

  private void logsBtnActionPerformed(ActionEvent evt) {//GEN-FIRST:event_logsBtnActionPerformed
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        logPanel.setVisible(true);
        logPanel.setLocationRelativeTo(MovieRenamer.this);
      }
    });
  }//GEN-LAST:event_logsBtnActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private WebButton exitBtn;
  private WebCheckBox fanartChk;
  private WebTextField fileFormatField;
  private WebButton helpBtn;
  private JScrollPane jScrollPane1;
  private WebSplitPane listSp;
  private WebButton logsBtn;
  private WebToolBar mainTb;
  private WebList mediaFileList;
  private WebPanel mediaFilePnl;
  private JScrollPane mediaFileScp;
  private WebToolBar mediaFileTb;
  private WebLabel mediaLbl;
  private WebLabel mediaLoaderLbl;
  private WebSplitPane mediaSp;
  private Separator modeSep;
  private WebButton movieModeBtn;
  private WebCheckBox nfoChk;
  private WebButton openBtn;
  private WebSeparator openSep;
  private WebButton renameBtn;
  private JTextField renameField;
  private WebToolBar renameTb;
  private JComboBox scrapperCb;
  private WebButton searchBtn;
  private WebTextField searchField;
  private WebLabel searchLbl;
  private WebPanel searchPnl;
  private WebList searchResultList;
  private WebToolBar searchTb;
  private WebButton settingBtn;
  private WebCheckBox thumbChk;
  private WebButton toggleGroup;
  private WebButton tvShowModeBtn;
  private WebButton updateBtn;
  // End of variables declaration//GEN-END:variables
}
