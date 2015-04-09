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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.zetool.opengl.bsp;

//import io.FileTypeException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class OFFReader {
	DynamicTriangleMesh mesh;

	public void readOff( String filename ) throws FileNotFoundException, IOException {
		if( mesh == null )
			mesh = new DynamicTriangleMesh();

		System.out.println( "Reading model file " + filename );

		BufferedReader reader = new BufferedReader( new FileReader( new File( filename ) ) );

		String line = reader.readLine();

		if( !line.equals( "OFF" ) )
			throw new IllegalArgumentException( "Not a valid OFF file" );
			//throw new FileTypeException( "Not a valid OFF file" ); // in the original zet package.

		int numVertices = 0;
		int numFaces = 0;
		int numEdges = 0;

		mesh.prepareSize( numVertices, numEdges, numFaces );


		do { // skip comments
			line = reader.readLine().trim();
		} while( line.charAt( 0 ) == '#' );


		String[] split = line.split( "[\\s]+" );
		numVertices = Integer.parseInt( split[0] );
		numFaces = Integer.parseInt( split[1] );
		numEdges = Integer.parseInt( split[2] );

		// read vertices
		System.out.println( "Read " + numVertices + " vertices." );
		for( int i = 0; i < numVertices; i++ ) {
			float x, y, z;
			line = reader.readLine().trim();
			split = line.split( "[\\s]+" );
			x = Float.parseFloat( split[0] );
			y = Float.parseFloat( split[1] );
			z = Float.parseFloat( split[2] );

			mesh.addVertex( x, y, z );
		}

		// create a new Triangle object for each face and pass it the pointers to the
		// corresponding vertex objects, do not make copies!
		// store the triangle in the TrianglesList

		System.out.println( "Read " + numFaces + " faces." );
		for( int i = 0; i < numFaces; i++ ) {
			int i1, i2, i3;

			line = reader.readLine().trim();
			split = line.split( "[\\s]+" );
			int n = Integer.parseInt( split[0] );

			switch( n ) {
				case 3:
					i1 = Integer.parseInt( split[1] );
					i2 = Integer.parseInt( split[2] );
					i3 = Integer.parseInt( split[3] );
					break;
				default:
					throw new IllegalArgumentException( "Currently only files with triangles are supported." );
			}
			mesh.addTriangle( i1, i2, i3 );
		}

		reader.close();
	}

	public DynamicTriangleMesh getMesh() {
		return mesh;
	}

	public void setMesh( DynamicTriangleMesh mesh ) {
		this.mesh = mesh;
	}
}
