/*
 * movie-renamer-core
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
package fr.free.movierenamer.searchinfo;

import java.net.URL;

/**
 * Class Subtitle
 *
 * @author Nicolas Magré
 * @author Simon QUÉMÉNEUR
 */
public class Subtitle extends Hyperlink {

  private static final long serialVersionUID = 1L;
  private String shortName;

  protected Subtitle() {
    // used by serializer
  }

  public Subtitle(String shortName, String title, URL link) {
    super(title, null, link);
    this.shortName = shortName;
  }

  @Override
  public String getName() {
    return shortName;
  }

  @Override
  public String toString() {
    if (shortName != null) {
      return String.format("%s", shortName);
    }

    return super.toString();
  }
}
