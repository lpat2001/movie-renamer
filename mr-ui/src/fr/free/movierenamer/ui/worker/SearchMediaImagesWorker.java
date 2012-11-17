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
package fr.free.movierenamer.ui.worker;

import fr.free.movierenamer.info.ImageInfo;
import fr.free.movierenamer.info.MediaInfo;
import fr.free.movierenamer.scrapper.MediaScrapper;
import fr.free.movierenamer.searchinfo.Media;
import fr.free.movierenamer.ui.res.UISearchResult;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Class SearchMediaImagesWorker
 *
 * @author Nicolas Magré
 * @author Simon QUÉMÉNEUR
 */
public class SearchMediaImagesWorker extends AbstractWorker<List<ImageInfo>> {

  private final UISearchResult searchResult;
  private final MediaScrapper<Media, MediaInfo> scrapper;

  /**
   * Constructor arguments
   *
   * @param errorSupport
   * @param searchResult
   */
  public SearchMediaImagesWorker(PropertyChangeSupport errorSupport, UISearchResult searchResult) {
    super(errorSupport);
    this.searchResult = searchResult;
    this.scrapper = (searchResult != null) ? (MediaScrapper<Media, MediaInfo>) searchResult.getScrapper() : null;
  }

  @Override
  public List<ImageInfo> executeInBackground() throws Exception {

    List<ImageInfo> infos = new ArrayList<ImageInfo>();

    if (searchResult != null && scrapper != null) {
      Media media = searchResult.getSearchResult();
      infos = scrapper.getImages(media);
      int count = infos.size();
      for (int i = 0; i < count; i++) {
        if (isCancelled()) {
          return infos;
        }

        double progress = (i + 1) / (double) count;
        setProgress((int) (progress * 100));
      }
    }

    return infos;
  }
}
