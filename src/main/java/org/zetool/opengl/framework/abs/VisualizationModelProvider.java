/*
 * zet evacuation tool copyright (c) 2007-20 zet evacuation team
 *
 * This program is free software; you can redistribute it and/or
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.zetool.opengl.framework.abs;

import org.zetool.opengl.helper.Frustum;

/**
 * Provides information from the general information model to drawable objects. The maintained
 * {@link VisualizationModel} is mutable, this interface provides read only access. Returned values are about to change
 * and should not be cached in most cases.
 *
 * @author Jan-Philipp Kappmeier
 */
public interface VisualizationModelProvider {

    Frustum getFrustum();

    /**
     * Checks whether all parts of the simulation are finished, or not.
     *
     * @return true if the simulation is finished, false otherwise
     */
    public boolean isFinished();
}
