/*
 * movie-renamer
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
package fr.free.movierenamer.parser.xml;

import static org.junit.Assert.*;

import java.net.URL;

import java.io.InputStream;

import org.junit.Assert;

import java.net.URISyntaxException;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fr.free.movierenamer.media.movie.MovieInfo;

import java.io.File;

import fr.free.movierenamer.worker.provider.ImdbInfoWorker;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import org.junit.Test;

/**
 * Class AllocineInfoTest
 * 
 * @author Simon QUÉMÉNEUR
 */
public class AllocineInfoTest {

  private AllocineInfo parser;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() throws SAXParseException, IOException, InterruptedException, ParserConfigurationException, SAXException, URISyntaxException {
    Assert.assertEquals("La Haine", loadInfo("12551").getTitle());
    Assert.assertEquals("Charlton Heston", loadInfo("1532").getActorN(0));
    Assert.assertEquals("1999", loadInfo("19776").getYear());
  }
  
  private MovieInfo loadInfo(String allocineId) throws URISyntaxException, SAXParseException, IOException, InterruptedException, ParserConfigurationException, SAXException
  {
    URL resource = this.getClass().getClassLoader().getResource("data/allocine." + allocineId + ".xml");
    File xmlFile = new File(resource.toURI());
    XMLParser<MovieInfo> xmp = new XMLParser<MovieInfo>(xmlFile.getAbsolutePath());
    MrParser<MovieInfo> infoParser = new AllocineInfo();
    infoParser.setOriginalFile(xmlFile);
    xmp.setParser(infoParser);
    return xmp.parseXml();
  }

}
