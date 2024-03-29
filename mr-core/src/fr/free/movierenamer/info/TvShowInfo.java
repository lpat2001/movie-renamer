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
package fr.free.movierenamer.info;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import fr.free.movierenamer.utils.Date;

/**
 * Class TvShowInfo
 *
 * @author Nicolas Magré
 * @author Simon QUÉMÉNEUR
 */
public class TvShowInfo extends MediaInfo {

  private static final long serialVersionUID = 1L;

  public static enum TvShowProperty {

    id,
    IMDB_ID,
    firstAired,
    genre,
    language,
    overview,
    rating,
    votes,
    runtime,
    name,
    status,
    posterPath
  }
  protected final Map<TvShowProperty, String> fields;

  protected TvShowInfo() {
    // used by serializer
    this.fields = null;
  }

  public TvShowInfo(Map<TvShowProperty, String> fields) {
    this.fields = (fields != null) ? new EnumMap<TvShowProperty, String>(fields) : new HashMap<TvShowInfo.TvShowProperty, String>();
  }

  private String get(TvShowProperty key) {
    return (fields != null) ? fields.get(key) : null;
  }

  public Integer getId() {
    try {
      return Integer.parseInt(get(TvShowProperty.id));
    } catch (Exception e) {
      return null;
    }
  }

  public List<String> getGenres() {
    return split(get(TvShowProperty.genre));
  }

  protected List<String> split(String values) {
    List<String> items = new ArrayList<String>();
    if (values != null && values.length() > 0) {
      for (String it : values.split("[|]")) {
        it = it.trim();
        if (it.length() > 0) {
          items.add(it);
        }
      }
    }
    return items;
  }

  public Date getFirstAired() {
    return Date.parse(get(TvShowProperty.firstAired), "yyyy-MM-dd");
  }

  public Integer getImdbId() {
    try {
      return Integer.parseInt(get(TvShowProperty.IMDB_ID).substring(2));
    } catch (Exception e) {
      return null;
    }
  }

  public Locale getLanguage() {
    try {
      return new Locale(get(TvShowProperty.language));
    } catch (Exception e) {
      return null;
    }
  }

  public String getOverview() {
    return get(TvShowProperty.overview);
  }

  public Double getRating() {
    try {
      return Double.parseDouble(get(TvShowProperty.rating));
    } catch (Exception e) {
      return null;
    }
  }

  public Integer getVotes() {
    try {
      return Integer.parseInt(get(TvShowProperty.votes));
    } catch (Exception e) {
      return null;
    }
  }

  public String getRuntime() {
    return get(TvShowProperty.runtime);
  }

  public String getName() {
    return get(TvShowProperty.name);
  }

  public String getStatus() {
    return get(TvShowProperty.status);
  }

  public URI getPosterPath() {
    try {
      return new URL(get(TvShowProperty.posterPath)).toURI();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public String getRenamedTitle(String format) {
    // TODO Auto-generated method stub
    return getName();
  }

  @Override
  public String toString() {
    return fields.toString();
  }
}