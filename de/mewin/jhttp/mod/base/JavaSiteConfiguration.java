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

import de.mewin.jhttp.ServerSettingsFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class JavaSiteConfiguration extends ServerSettingsFile
{
    public String mainClass = null;
    
    public JavaSiteConfiguration(InputStream in) throws IOException, JavaSiteException
    {
        parseConfig(new InputStreamReader(in));
        mainClass = getString("main", null);
        if (mainClass == null)
        {
            throw new JavaSiteException("Main class not set.");
        }
    }
}