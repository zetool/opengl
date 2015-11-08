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

package org.zetool.opengl.drawingutils;

import java.util.ArrayList;
import javax.media.opengl.GL;

/**
 * A class that supports color gradients that use more than one color. The
 * colors are scaled to build a gradient from time 0 up to a max time.
 * @author Jan-Philipp Kappmeier
 */
public class RainbowGradient {
	/** The 'real' Newton colors of a RAINBOW. */
	final static public GLColor[] RAINBOW = { GLColor.red, GLColor.orange, GLColor.yellow, GLColor.green, GLColor.blue, GLColor.indigo, GLColor.violet };
	/** The list of colors used for the gradient. */
	private ArrayList<GLColor> colors;
	/** The maximal time up to which the color gradient should use. */
	double maxTime = 25;
	/** The time interval that is used for each color. */
	double timeInterval;

	/**
	 * Initializes with a default RAINBOW gradient.
	 */
	public RainbowGradient() {
		this( RAINBOW );
	}

	/**
	 * Initializes with a user specific color gradient.
	 * @param colors the colors of the gradient
	 */
	public RainbowGradient( GLColor[] colors ) {
		this.colors = new ArrayList<>( colors.length );
		for( int i = 0; i < colors.length; ++i )
			this.colors.add( colors[i] );
		timeInterval = maxTime / (colors.length-1);
	}

	/**
	 * Gets the color for the gradient at a specific time.
	 * @param time the time
	 * @return the color for the time
	 */
	public GLColor getColorForTime( double time ) {
		int colorIndex = (int)Math.floor( time/timeInterval );
		GLColor color = GLColor.blend( colors.get( colorIndex ), colors.get( colorIndex+1), time/timeInterval - colorIndex );
		return color;
	}

	/**
	 * Returns he index of the color array that is used at the specified time.
	 * @param time the time
	 * @return the index
	 */
	private int getColorIndex( double time ) {
		int colorIndex = (int)Math.floor( time/timeInterval );
		return colorIndex;
	}

	/**
	 * Returns the last time where the colors in the given array index are used.
	 * @param index the index
	 * @return the time where the index is used the last time.
	 */
	public double getColorIndexEndTime( int index ) {
		return index * timeInterval;
	}

	/**
	 * Draws a cylinder with a color gradient from the base value up to the top
	 * value. The cylinder is positioned in the origin and is drawn along the
	 * {@code z}-axis.
	 * @param gl the graphics object
	 * @param width the width of the cylinder
	 * @param length the length of the cylinder
	 * @param baseColorValue the color value (of the gradient) at the base
	 * @param topColorValue the color value (of the gradient) at the top
	 */
	public void drawCylinder( GL gl, double width, double length, double baseColorValue, double topColorValue ) {
				gl.glPushMatrix();
		double temporaryBaseColorValue = baseColorValue;
		if( topColorValue > temporaryBaseColorValue )
			throw new IllegalArgumentException( "colorLast < colorFirst" );
		while( true ) {
			final int startIndex = getColorIndex( temporaryBaseColorValue );
			if( startIndex == getColorIndex( topColorValue ) ) {
				// easy peasy
				Cylinder.drawCylinder( gl, width, length, 32, getColorForTime( temporaryBaseColorValue), getColorForTime( topColorValue ) );
				break;
			} else {
				// compute the break point where the color changes
				final double breakPointPosition = getColorIndexEndTime( startIndex );
				final double len = (temporaryBaseColorValue-breakPointPosition)/(temporaryBaseColorValue-topColorValue) * length;
				Cylinder.drawCylinder( gl, width, len*1.0001, 32, getColorForTime( temporaryBaseColorValue), getColorForTime( breakPointPosition ) );
				length -= len;
				gl.glTranslated( 0, 0, len );
				temporaryBaseColorValue = breakPointPosition-0.000001;
			}
		}
		gl.glPopMatrix();
	}
}
