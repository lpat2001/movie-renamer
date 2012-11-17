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
package fr.free.movierenamer.ui.worker.listener;

import com.alee.laf.list.WebList;
import fr.free.movierenamer.info.MediaInfo;
import fr.free.movierenamer.scrapper.MediaScrapper;
import fr.free.movierenamer.searchinfo.Media;
import fr.free.movierenamer.searchinfo.SearchResult;
import fr.free.movierenamer.ui.LoadingDialog.LoadingDialogPos;
import fr.free.movierenamer.ui.MovieRenamer;
import fr.free.movierenamer.ui.res.IconListRenderer;
import fr.free.movierenamer.ui.res.UISearchResult;
import fr.free.movierenamer.ui.settings.Settings;
import fr.free.movierenamer.ui.worker.SearchMediaWorker;
import fr.free.movierenamer.utils.LocaleUtils;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * Class MediaListener
 *
 * @author Nicolas Magré
 */
public class MediaListener extends WorkerListener<List<Media>> {

  private final WebList searchResultList;
  private final MediaScrapper<? extends SearchResult, ? extends MediaInfo> scrapper;

  public MediaListener(SearchMediaWorker worker, MovieRenamer mr, WebList searchResultList, MediaScrapper<? extends SearchResult, ? extends MediaInfo> scrapper) {
    super(mr, worker);
    this.searchResultList = searchResultList;
    this.scrapper = scrapper;
  }

  @Override
  protected LoadingDialogPos getLoadingDialogPos() {
    return LoadingDialogPos.search;
  }

  @Override
  protected void done() throws Exception {
    DefaultListModel searchResModel = new DefaultListModel();
    List<Media> results = worker.get();
    
      // // Sort result by similarity and year //FIXME Sort result by similarity and year
      // if (count.size() > 1 && setting.sortBySimiYear) {
      // Levenshtein.sortByLevenshteinDistanceYear(currentMedia.getSearch(), currentMedia.getYear(), results);
      // }

      // searchLbl.setText(LocaleUtils.i18n("search") + " : " + count);//FIXME update labels
    
    for(Media result : results) {
      searchResModel.addElement(new UISearchResult(result, scrapper));
    }

    if (Settings.getInstance().displayThumbResult) {
      searchResultList.setCellRenderer(new IconListRenderer<UISearchResult>());
    } else {
      searchResultList.setCellRenderer(new DefaultListCellRenderer());
    }
    searchResultList.setModel(searchResModel);

    if (searchResModel.isEmpty()) {
      JOptionPane.showMessageDialog(mr, LocaleUtils.i18n("noResult"), LocaleUtils.i18n("error"), JOptionPane.ERROR_MESSAGE);
    } else if (Settings.getInstance().selectFrstRes) {
      searchResultList.setSelectedIndex(0);
    }
  }
}
