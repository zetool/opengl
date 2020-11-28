/*
 * zet evacuation tool copyright © 2007-20 zet evacuation team
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
package org.zetool.opengl.framework.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLProfile;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Thread that creates an {@link GLContext OpenGL context} before the {@link Runnable} task is executed. Thus code that
 * requires an OpenGL context can be executed.
 *
 * @author Jan-Philipp Kappmeier
 */
public class GLContextAwareThread extends Thread {

    /**
     * The fake drawable instance used to create the OpenGL context.
     */
    private final GLDrawable drawable;
    /**
     * The OpenGL context used in the thread.
     */
    private final GLContext context;

    /**
     * Initializes the thread with a {@code target} to be executed.
     *
     * @param target the object whose {@code run} method is executed in a thread with OpenGL context present
     */
    public GLContextAwareThread(Runnable target) {
        super(target);
        GLDrawableFactory fact = GLDrawableFactory.getFactory(GLProfile.getDefault());
        GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
        caps.setOnscreen(false);
        caps.setDoubleBuffered(false);

        drawable = fact.createOffscreenDrawable(null, caps, null, 800, 600);
        drawable.setRealized(true);
        context = drawable.createContext(null);
    }

    /**
     * Initializes an {@link GLContext OpenGL context} and executes the {@link Runnable run} object. After complete
     * execution the context is destroyed.
     */
    @Override
    public void run() {
        initContext();
        try {
            super.run();
        } finally {
            destroyContext();
        }
    }

    /**
     * Assigns it to the current thread.
     */
    private void initContext() {
        context.makeCurrent();
    }

    /**
     * Destroys the context.
     */
    private void destroyContext() {
        context.destroy();
        drawable.setRealized(false);
    }

    /**
     * Produces an object instance in a thread with {@link GLContext} present.
     *
     * @param <T> the type of the created object
     * @param factory the supplier of {@code T} object
     * @return the created instance
     * @throws RuntimeException if an exception occured during thread execution
     */
    public static <T> T createWithGLContext(Supplier<T> factory) throws RuntimeException {
        AtomicReference<T> resultHolder = new AtomicReference<>();

        Throwable exceptionHolder = executeSafe(() -> resultHolder.set(factory.get()));
        checkForExceptions(exceptionHolder);

        return resultHolder.get();
    }

    /**
     * Executes the runnable and returns any uncought exceptions.
     *
     * @param runnable the task to be executed
     * @return any uncought exception, or {@code null}
     */
    @Nullable
    private static Throwable executeSafe(Runnable runnable) {
        AtomicReference<Throwable> exceptionHolder = new AtomicReference<>();
        Thread glAwareFactory = new GLContextAwareThread(runnable);
        glAwareFactory.setUncaughtExceptionHandler((Thread th, Throwable ex) -> exceptionHolder.set(ex));
        executeGLAwareFactory(glAwareFactory);
        return exceptionHolder.get();
    }

    /**
     * Executes the gl aware factory thread. Waits for the thread to finish. Thread interruptions are properly handled
     * and a {@link RuntimeException} is thrown.
     *
     * @param glAwareFactory the thread that is executed
     * @throws RuntimeException if the thread is interrupted
     */
    private static void executeGLAwareFactory(Thread glAwareFactory) throws RuntimeException {
        glAwareFactory.start();
        try {
            glAwareFactory.join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        }
    }

    /**
     * Throws the exception, if present. Directly rethrows the exception if it is unchecked. Checked exceptions are
     * wrapped as {@link RuntimeException}.
     *
     * @param exception the optional exception that is t hrown, can be {@code null}
     * @throws RuntimeException if the given exception is not {@code null}
     */
    private static void checkForExceptions(@Nullable Throwable exception) throws RuntimeException {
        if (exception != null) {
            if (exception instanceof RuntimeException) {
                throw (RuntimeException) exception;
            }
            throw new RuntimeException(exception);
        }
    }
}
