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
package fr.free.movierenamer.media;

/**
 * Class Images
 *
 * @author Nicolas Magré
 */
public class MediaImage {

  public static final int THUMB = 0;
  public static final int FANART = 1;
  private int id;
  private int type;
  private String orig;
  private String medium;
  private String thumb;

  /**
   * Constructor arguments
   *
   * @param id Image id, "-1" -> image added from web/hdd/..., "0" image from API/NFO/...
   * @param type Media image type
   */
  public MediaImage(int id, int type) {
    this.id = id;
    this.type = type;
    orig = "";
    medium = "";
    thumb = "";
  }

  /**
   * Get Id
   *
   * @return Id
   */
  public int getId() {
    return id;
  }

  /**
   * Get media image type
   *
   * @return Media image type
   */
  public int getType() {
    return type;
  }

  /**
   * Get image original URL
   *
   * @return Original URL
   */
  public String getOrigUrl() {
    return orig;
  }

  /**
   * Get image middle URL
   *
   * @return Middle URL
   */
  public String getMidUrl() {
    return medium;
  }

  /**
   * Get image thumb URL
   *
   * @return Thumb URL
   */
  public String getThumbUrl() {
    return thumb;
  }

  /**
   * Set original image URL
   *
   * @param url URL
   */
  public void setOrigUrl(String url) {
    orig = url;
  }

  /**
   * Set middle image URL
   *
   * @param url URL
   */
  public void setMidUrl(String url) {
    medium = url;
  }

  /**
   * Set thumb URL
   *
   * @param url URL
   */
  public void setThumbUrl(String url) {
    thumb = url;
  }

  @Override
  public String toString() {
    return orig;
  }
}