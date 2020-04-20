/* zet evacuation tool copyright (c) 2007-20 zet evacuation team
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
package org.zetool.opengl.framework;

import org.zetool.math.vectormath.Vector3;

/**
 * The class {@code CameraPosition} stores a camera position. The position
 * consists of vectors for position, up, view and a name.
 * @author Jan-Philipp Kappmeier
 */
public class CameraPosition {
	/** Direction of view (z-axis) */
	public Vector3 view = new Vector3( 1, -1, 0 );
	/** Direction of up (y-axis) */
	public Vector3 up = new Vector3( 0, 0, 1 );
		/** Position */
	public Vector3 pos = new Vector3( 0, 0, 100 );
	/** A description for the position. */
	public String text = "CameraPosition";

	/**
	 * Creates a new instance of {@code CameraPosition}.
	 */
	public CameraPosition() {	}
}
