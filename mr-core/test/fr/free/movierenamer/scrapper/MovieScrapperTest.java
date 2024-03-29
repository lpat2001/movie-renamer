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
package fr.free.movierenamer.scrapper;

import org.junit.Test;

/**
 * Class MovieScrapperTest
 * @author Simon QUÉMÉNEUR
 */
public abstract class MovieScrapperTest extends ScrapperTest {
  
  @Test
  public abstract void search() throws Exception;

  @Test
  public abstract void getMovieInfo() throws Exception;
  
  @Test
  public abstract void getCasting() throws Exception;

  @Test
  public abstract void getImages() throws Exception;
}
