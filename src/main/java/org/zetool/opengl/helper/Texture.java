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

import javax.media.opengl.GL2;

/**
 * The class {@code Texture} represents a texture used by OpenGL. It can
 * bind and mipmap them.
 * @author Jan-Philipp Kappmeier
 */
public class Texture {
	/** The graphics context used by the texture */
	GL2 gl;
	/** The target */
	final int target;
	/** The texture id */
	final int textureID;
	/** The texture last binded, checked to not bind the same texture twice. */
	static Texture lastbind = null;
	
	public String resourceName = null;
/**
	 * Creates a new instance of {@code Texture}.
 * @param gl the graphics context in which this texture is used
 * @param target the OpenGL target, for example ({@link javax.media.opengl.GL#GL_TEXTURE_2D})
 * @param textureID the internal id of the texture (OpenGL texture name)
	 */
	public Texture( GL2 gl, int target, int textureID ) {
		this.gl = gl;
		this.textureID = textureID;
		this.target = target;
	}

	/**
	 * <p>Binds this texture to the current GL context. This method is a shorthand
	 * equivalent of the OpenGL code</p>
	 * <p>{@code gl.glBindTexture(texture.getTarget(), texture.getTextureID());}</p>
	 */
	public final void bind() {
		if( lastbind == null || lastbind != this ) {
			gl.glBindTexture( target, textureID );
			lastbind = this;
		}
	}

	/**
	 * Returns the {@code OpenGL} target of the texture.
	 * @return the target of the texture
	 */
	public final int getTarget() {
		return target;
	}

	/**
	 * Returns the OpenGL texture object for this texture. The ids are handled
	 * automatically by the {@link #bind()} and {@link #dispose()}.
	 * @return the id of the texture
	 */
	public final int getID() {
		return textureID;
	}

	// TODO Dispose
	public final void dispose() {
		
	}

	/**
	 * Returns the name of the class.
	 * @return the name of the class
	 */
	@Override
	public String toString() {
		return "Texture";
	}
}
