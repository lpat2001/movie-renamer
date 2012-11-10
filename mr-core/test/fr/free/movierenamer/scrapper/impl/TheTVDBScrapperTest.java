/*
 * movie-renamer-core
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
package fr.free.movierenamer.scrapper.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Locale;

import org.junit.Assert;

import fr.free.movierenamer.info.CastingInfo;
import fr.free.movierenamer.info.EpisodeInfo;
import fr.free.movierenamer.info.ImageInfo;
import fr.free.movierenamer.info.TvShowInfo;
import fr.free.movierenamer.info.ImageInfo.ImageCategoryProperty;
import fr.free.movierenamer.scrapper.TvShowScrapperTest;
import fr.free.movierenamer.searchinfo.Movie;
import fr.free.movierenamer.searchinfo.TvShow;

/**
 * Class TheTVDBScrapperTest
 * 
 * @author Simon QUÉMÉNEUR
 */
public final class TheTVDBScrapperTest extends TvShowScrapperTest {
  private TheTVDBScrapper thetvdb = null;

  @Override
  public void init() {
    thetvdb = new TheTVDBScrapper();
  }
  
  @Override
  public void search() throws Exception {
    List<TvShow> results = thetvdb.search("desperate housewives");

    assertEquals(1, results.size());

    TvShow tvShow =  results.get(0);

    assertEquals("Desperate Housewives", tvShow.getName());
    assertEquals("http://www.thetvdb.com/banners/graphical/73800-g15.jpg", tvShow.getURL().toExternalForm());
    assertEquals(2004, tvShow.getYear());
    assertEquals(73800, tvShow.getMediaId());
  }
  
  @Override
  public void getTvShowInfo() throws Exception {
    thetvdb.setLocale(Locale.FRENCH);
    TvShowInfo tvShow = thetvdb.getInfo(new TvShow(82066, null, null, -1));

    assertEquals("Fringe", tvShow.getName());
    assertEquals("2008-08-26", tvShow.getFirstAired().toString());
    assertEquals("[Drama, Science-Fiction]", tvShow.getGenres().toString());
  }

  @Override
  public void getCasting() throws Exception {
    List<CastingInfo> cast = thetvdb.getCasting(new TvShow(82066, null, null, -1));
    for(CastingInfo info : cast) {
      if(info.isActor()) {
        assertEquals("Anna Torv", info.getName());
        return;
      }
    }
    
    Assert.fail();
  };

  @Override
  public void getImages() throws Exception {
    List<ImageInfo> images = thetvdb.getImages(new TvShow(70327, null, null, -1));
    assertEquals(ImageCategoryProperty.fanart, images.get(0).getCategory());
    assertEquals("http://www.thetvdb.com/banners/fanart/original/70327-17.jpg", images.get(1).getHref().toExternalForm());
  }
  
  @Override
  public void getEpisodesInfoList() throws Exception {
    thetvdb.setLocale(Locale.GERMAN);
    List<EpisodeInfo> episodes = thetvdb.getEpisodesInfoList(new TvShow(81189, null, null, -1));

    EpisodeInfo first = episodes.get(0);

    assertEquals("Breaking Bad", first.getTvShowName());
    assertEquals("Der Einstieg", first.getName());
    assertEquals("2008-01-20", first.getAirdate().toString());
  }

}