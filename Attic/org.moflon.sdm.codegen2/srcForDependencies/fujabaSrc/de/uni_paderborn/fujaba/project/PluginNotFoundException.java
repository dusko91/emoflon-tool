/*
 * The FUJABA ToolSuite project:
 *
 *   FUJABA is the acronym for 'From Uml to Java And Back Again'
 *   and originally aims to provide an environment for round-trip
 *   engineering using UML as visual programming language. During
 *   the last years, the environment has become a base for several
 *   research activities, e.g. distributed software, database
 *   systems, modelling mechanical and electrical systems and
 *   their simulation. Thus, the environment has become a project,
 *   where this source code is part of. Further details are avail-
 *   able via http://www.fujaba.de
 *
 *      Copyright (C) Fujaba Development Group
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free
 *   Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *   MA 02111-1307, USA or download the license under
 *   http://www.gnu.org/copyleft/lesser.html
 *
 * WARRANTY:
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 * Contact address:
 *
 *   Fujaba Management Board
 *   Software Engineering Group
 *   University of Paderborn
 *   Warburgerstr. 100
 *   D-33098 Paderborn
 *   Germany
 *
 *   URL  : http://www.fujaba.de
 *   email: info@fujaba.de
 *
 */
package de.uni_paderborn.fujaba.project;



/**
 * @author    $Author$
 * @version   $Revision$ $Date$
 */
public class PluginNotFoundException extends RuntimeException
{
   /**
    * 
    */
   private static final long serialVersionUID = 9066511744344386568L;
   /**
    * No comment provided by developer, please add a comment to improve documentation.
    */
   private final String pluginId;


   /**
    * Constructs a new runtime exception with the specified detail message.
    *
    * @param message   the detail message. The detail message is saved for
    *                later retrieval by the {@link #getMessage()} method.
    * @param pluginId  id of the plugin that was not found
    */
   public PluginNotFoundException (String message, String pluginId)
   {
      super (message);
      this.pluginId = pluginId;
   }


   /**
    * @return   id of the plugin that was not found
    */
   public String getPluginId()
   {
      return pluginId;
   }
}

/*
 * $Log$
 * Revision 1.2  2005/10/09 19:05:03  lowende
 * Removed some compile warnings.
 *
 */