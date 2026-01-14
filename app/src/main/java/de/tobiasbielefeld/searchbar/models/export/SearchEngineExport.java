/*
 * Copyright (C) 2025  Tobias Bielefeld
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you want to contact me, send me an e-mail at tobias.bielefeld@gmail.com
 */

package de.tobiasbielefeld.searchbar.models.export;

public class SearchEngineExport {

    /*
     * IMPORTANT: This package path is explicitly set to keep class names in the proguard-rules.pro file
     * This is needed to the correct names are used in the export file.
     * Keep in mind:
     *  * Do not use enums here
     *  * Only use classes that are part of this package
     *  * do not rename variables, nor change type
     */

    public int version = 1;

    public String label;
    public String uri;
    public byte[] icon;
    public boolean hidden;
}
