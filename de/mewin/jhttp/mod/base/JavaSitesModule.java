/*
 * Copyright (C) 2013 mewin<mewin001@hotmail.de>
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

package de.mewin.jhttp.mod.base;

import de.mewin.jhttp.event.EventHandler;
import de.mewin.jhttp.event.Listener;
import de.mewin.jhttp.event.RequestDocumentEvent;
import de.mewin.jhttp.event.RequestDocumentEvent.HTTPAnswer;
import de.mewin.jhttp.mod.Module;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class JavaSitesModule extends Module implements Listener
{

    @Override
    protected void onEnable()
    {
        getServer().getEventManager().registerEvents(this);
    }
    
    @EventHandler
    public void onRequestDocument(RequestDocumentEvent e)
    {
        if (e.getFile().exists() && e.getFile().isFile() && e.getFile().getName().toLowerCase().endsWith(".jar"))
        {
            try
            {
                JarFile jarFile = new JarFile(e.getFile());
                ZipEntry infoFile = jarFile.getEntry("site.conf");
                
                if (infoFile != null && !infoFile.isDirectory())
                {
                    InputStream in = null;
                    try
                    {
                        in = jarFile.getInputStream(infoFile);
                        JavaSiteConfiguration conf = new JavaSiteConfiguration(in);
                        Class cls = URLClassLoader.newInstance(new URL[] {e.getFile().toURI().toURL()}, this.getClass().getClassLoader()).loadClass(conf.mainClass);
                        if (!JavaSite.class.isAssignableFrom(cls))
                        {
                            throw new JavaSiteException("Main class is not a java site.");
                        }
                        else
                        {
                            HTTPAnswer answer = ((JavaSite) cls.newInstance()).getAnswer(e.getURL(), e.getRequestHeader(), e.getRequestBody());
                            e.setNewAnswer(answer);
                        }
                    }
                    catch(IOException | JavaSiteException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
                    {
                        ex.printStackTrace(System.err);
                        if (in != null)
                        {
                            try
                            {
                                in.close();
                            }
                            catch(Exception ex2)
                            {
                            }
                        }
                    }
                }
            }
            catch (IOException ex)
            {
                
            }
        }
    }
}