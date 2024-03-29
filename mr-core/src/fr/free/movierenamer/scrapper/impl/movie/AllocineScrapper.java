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
package fr.free.movierenamer.scrapper.impl.movie;

import fr.free.movierenamer.info.IdInfo;
import fr.free.movierenamer.info.MovieInfo.MotionPictureRating;
import fr.free.movierenamer.utils.LocaleUtils.AvailableLanguages;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Class AllocineScrapper : search movie on allocine
 *
 * @author Nicolas Magré
 * @author Simon QUÉMÉNEUR
 */
public final class AllocineScrapper extends AlloGroupScrapper {

  private static final String host = "www.allocine.fr";
  private static final String imageHost = "images.allocine.fr";
  private static final String name = "Allocine";
  private static final String search = "recherche";
  private static final Pattern allocineID = Pattern.compile(".*gen_cfilm=(\\d+).*");
  private static final Pattern allocinePersonID = Pattern.compile(".*cpersonne=(\\d+).*");

  public AllocineScrapper() {
    super(AvailableLanguages.fr);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  protected String getHost() {
    return host;
  }

  @Override
  protected Locale getDefaultLanguage() {
    return Locale.FRENCH;
  }

  @Override
  protected String getSearchString() {
    return search;
  }

  @Override
  protected String getMoviePageString(IdInfo id) {
    return "/film/fichefilm_gen_cfilm=" + id + ".html";
  }

  @Override
  protected String getCastingPageString(IdInfo id) {
    return "/film/fichefilm-" + id + "/casting/";
  }

  @Override
  protected Pattern getIdPattern() {
    return allocineID;
  }

  @Override
  protected Pattern getPersonIdPattern() {
    return allocinePersonID;
  }

  @Override
  protected String getImageHost() {
    return imageHost;
  }
  
  @Override
  protected MotionPictureRating getRatingScale() {
    return MotionPictureRating.FRANCE;
  }

  @Override
  protected InfoTag getInfoTag(String str) {
    try {
      return InfoTag.valueOf(str);
    } catch (Exception ex) {
    }
    return InfoTag.unknown;
  }
}
