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
package fr.free.movierenamer.media.tvshow;

import fr.free.movierenamer.utils.Utils;

/**
 * Class SxE
 * @author Nicolas Magré
 */
public class SxE {

  private int season;
  private int episode;
  private String match;

  public SxE() {
    season = -1;
    episode = -1;
    match = "";
  }
  
  public SxE(int season, int episode) {
    this.season = season;
    this.episode = episode;
    this.match = "";
  }
  
  public SxE(int season, int episode, String match) {
    this.season = season;
    this.episode = episode;
    this.match = match;
  }

  public int getSeason() {
    return season;
  }

  public int getEpisode() {
    return episode;
  }
  
  public String getMatch() {
    return match;
  }
  
  public int getAbsNum(){
    if(!Utils.isDigit(match)){
      return -1;
    }
    return Integer.parseInt(match);
  }
  
  public boolean isValid(){
    return season !=-1 && episode != -1;
  }
  
  public boolean isPartial(){
    return season != -1 || episode != -1;
  }
  
  public void setEpisode(int episode){
    this.episode = episode;
  }
  
  public void setSeason(int season){
    this.season = season;
  }
  
  @Override
  public boolean equals(Object obj){
    if (obj instanceof SxE) {
			SxE sxe = (SxE) obj;
			return sxe.getEpisode() == episode && sxe.getSeason() == season;
		}
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 29 * hash + this.season;
    hash = 29 * hash + this.episode;
    return hash;
  }

  @Override
  public String toString() {
    return "Season " + season + " Episode " + episode;
  }
}