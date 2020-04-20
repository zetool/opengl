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

package org.zetool.opengl.bsp;

import org.zetool.math.vectormath.Plane;
import org.zetool.math.vectormath.Vector3;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class Triangle implements Face {
	Vector3 v[] = new Vector3[3];
	int vidx[] = new int[3];
	Plane plane;
	Vector3 faceNormal;

	public Triangle() {
		// gefährlich ;)
	}

	public Triangle( Vector3 v0, Vector3 v1, Vector3 v2, int v0idx, int v1idx, int v2idx ) {
		v[0] = v0;
		v[1] = v1;
		v[2] = v2;
		vidx[0] = v0idx;
		vidx[1] = v1idx;
		vidx[2] = v2idx;
		computePlane();
	}

	/**
	 * Recomputes the plane equations and the normal. This may be called in the
	 * case, the vertex coordinates have been changed. Additionally, this should
	 * be called in case the normal and plane haven't been computed yet.
	 */
	public void computePlane() {
		if( plane == null )
			plane = new Plane( v[0], v[1], v[2] );
		else
			plane.setPlane( v[0], v[1], v[2] );
		//BspMain.planes.add( plane )
		faceNormal = plane.getNormal();
	}

	@Override
	public String toString() {
		return v[0].toString() + " " + v[1].toString() + " " + v[2].toString();
	}
}
