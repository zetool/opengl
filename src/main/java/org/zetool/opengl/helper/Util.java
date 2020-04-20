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

package org.zetool.opengl.helper;

import java.io.PrintStream;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class Util {
	/**
	 * Gives out all error messages to a submitted {@link PrintStream}.
	 * @param stream
	 */
	public static void printErrors( GL2 gl, PrintStream stream ) {
		int ret;
		while( (ret = gl.glGetError()) != GL.GL_NO_ERROR ) {
			switch( ret ) {
				case GL.GL_INVALID_ENUM:
					//stream.println( "INVALID ENUM" );
					break;
				case GL.GL_INVALID_VALUE:
					stream.println( "INVALID VALUE" );
					break;
				case GL.GL_INVALID_OPERATION:
					stream.println( "INVALID OPERATION" );
					break;
				case GL2ES1.GL_STACK_OVERFLOW:
					stream.println( "STACK OVERFLOW" );
					break;
				case GL2ES1.GL_STACK_UNDERFLOW:
					stream.println( "STACK UNDERFLOW" );
					break;
				case GL.GL_OUT_OF_MEMORY:
					stream.println( "OUT OF MEMORY" );
					break;
			}
		}
	}

}
