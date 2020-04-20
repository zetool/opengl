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

package org.zetool.opengl.drawingutils;

import javax.media.opengl.GL2;

/**
 * A helping class providing methods to draw a cylinder with more features than
 * the {@code gluCylinder} provides.
 * @author Jan-Philipp Kappmeier
 */
public class Cylinder {
	/**
	 * <p>Draws a cylinder. Basically this does the same as {@code gluCylinder}, but
	 * it only supports cylinders with same radius at base and top. Also, the
	 * number of stacks is fixed to 1.</p>
	 * <p>In addition to the (missing) features of {@code gluCylinder}, this method
	 * provides the ability of a color gradient on the cylinder.</p>
	 * @param gl the graphics object used to draw the cylinder
	 * @param radius the radius of the cylinder
	 * @param height the height of the cylinder
	 * @param slices the number of slices of the cylinder
	 * @param baseColor the color at the bottom
	 * @param topColor the color at the top
	 */
	public static void drawCylinder( GL2 gl, double radius, double height, int slices, GLColor baseColor, GLColor topColor ) {
		final double step = 2.0 * Math.PI / slices;
		// todo: give support to sign somehow. (change directions of normals)

		if( gl.glIsEnabled( GL2.GL_LIGHTING ) )
			gl.glEnable( GL2.GL_COLOR_MATERIAL );
		gl.glBegin( GL2.GL_QUAD_STRIP );
		for( int i = 0; i <= slices; i++ ) {
			double x, y;
			if( i == slices ) {
				x = Math.sin( 0.0 );
				y = Math.cos( 0.0 );
			} else {
				x = Math.sin( i * step );
				y = Math.cos( i * step );
			}
			normal3d( gl, x, y, 0 );
			baseColor.draw( gl, false );
			gl.glVertex3d( x * radius, y * radius, 0 );
			normal3d( gl, x, y, 0 );
			topColor.draw( gl, false );
			gl.glVertex3d( x * radius, y * radius, height );
		}
		gl.glEnd();

		if( gl.glIsEnabled( GL2.GL_LIGHTING ) )
			gl.glDisable( GL2.GL_COLOR_MATERIAL );
	}

	/**
	 * Call glNormal3f after scaling normal to unit length.
	 */
	private static void normal3d( GL2 gl, double x, double y, double z  ) {
		final double mag = Math.sqrt( x * x + y * y + z * z );
		if( mag > 0.00001 ) {
			x /= mag;
			y /= mag;
			z /= mag;
		}
		gl.glNormal3d( x, y, z );
	}
}
