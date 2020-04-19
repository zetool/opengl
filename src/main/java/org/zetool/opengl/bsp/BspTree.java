/* zet evacuation tool copyright (c) 2007-14 zet evacuation team
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
import java.util.Queue;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class BspTree {
	// pointer to plane that partitions this node
	private Plane partitionPlane;
	// pointer to list of triangles that are embedded in this node
	private Queue<Triangle> sameDir;
	private Queue<Triangle> oppDir;
	// pointer to the negative and positive subtree
	private BspTree negativeSide = null;
	private BspTree positiveSide = null;

	public BspTree( Plane partitionPlane, Queue<Triangle> sameDir, Queue<Triangle> oppDir ) {
		this.partitionPlane = partitionPlane;
		this.sameDir = sameDir;
		this.oppDir = oppDir;
	}

	void setNegativeSide( BspTree negativeSide ) {
		this.negativeSide = negativeSide;
	}

	void setPositiveSide( BspTree positiveSide ) {
		this.positiveSide = positiveSide;
	}

	public Queue<Triangle> getOppDir() {
		return oppDir;
	}

	public Plane getPartitionPlane() {
		return partitionPlane;
	}

	public Queue<Triangle> getSameDir() {
		return sameDir;
	}

	public BspTree getNegativeSide() {
		return negativeSide;
	}

	public BspTree getPositiveSide() {
		return positiveSide;
	}
}
