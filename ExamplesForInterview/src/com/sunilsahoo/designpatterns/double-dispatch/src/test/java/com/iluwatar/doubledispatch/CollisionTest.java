/**
 * The MIT License
 * Copyright (c) 2014 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sunilsahoo.designpatterns.doubledispatch;

import org.junit.After;
import org.junit.Before;

import java.io.PrintStream;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Date: 12/10/15 - 8:37 PM
 *
 * @author Jeroen Meulemeester
 */
public abstract class CollisionTest<O extends GameObject> {

  /**
   * The mocked standard out {@link PrintStream}, required if some of the actions on the tested
   * object don't have a direct influence on any other accessible objects, except for writing to
   * std-out using {@link System#out}
   */
  private final PrintStream stdOutMock = mock(PrintStream.class);

  /**
   * Keep the original std-out so it can be restored after the test
   */
  private final PrintStream stdOutOrig = System.out;

  /**
   * Inject the mocked std-out {@link PrintStream} into the {@link System} class before each test
   */
  @Before
  public void setUp() {
    System.setOut(this.stdOutMock);
  }

  /**
   * Removed the mocked std-out {@link PrintStream} again from the {@link System} class
   */
  @After
  public void tearDown() {
    System.setOut(this.stdOutOrig);
  }

  /**
   * Get the mocked stdOut {@link PrintStream}
   *
   * @return The stdOut print stream mock, renewed before each test
   */
  final PrintStream getStdOutMock() {
    return this.stdOutMock;
  }

  /**
   * Get the tested object
   *
   * @return The tested object, should never return 'null'
   */
  abstract O getTestedObject();

  /**
   * Collide the tested item with the other given item and verify if the damage and fire state is as
   * expected
   *
   * @param other        The other object we have to collide with
   * @param otherDamaged Indicates if the other object should be damaged after the collision
   * @param otherOnFire  Indicates if the other object should be burning after the collision
   * @param thisDamaged  Indicates if the test object should be damaged after the collision
   * @param thisOnFire   Indicates if the other object should be burning after the collision
   * @param description  The expected description of the collision
   */
  void testCollision(final GameObject other, final boolean otherDamaged, final boolean otherOnFire,
                     final boolean thisDamaged, final boolean thisOnFire, final String description) {

    Objects.requireNonNull(other);
    Objects.requireNonNull(getTestedObject());

    final O tested = getTestedObject();

    tested.collision(other);

    verify(getStdOutMock(), times(1)).println(description);
    verifyNoMoreInteractions(getStdOutMock());

    testOnFire(other, tested, otherOnFire);
    testDamaged(other, tested, otherDamaged);

    testOnFire(tested, other, thisOnFire);
    testDamaged(tested, other, thisDamaged);

  }

  /**
   * Test if the fire state of the target matches the expected state after colliding with the given
   * object
   *
   * @param target             The target object
   * @param other              The other object
   * @param expectTargetOnFire The expected state of fire on the target object
   */
  private void testOnFire(final GameObject target, final GameObject other, final boolean expectTargetOnFire) {
    final String targetName = target.getClass().getSimpleName();
    final String otherName = other.getClass().getSimpleName();

    final String errorMessage = expectTargetOnFire 
        ? "Expected [" + targetName + "] to be on fire after colliding with [" + otherName + "] but it was not!" 
        : "Expected [" + targetName + "] not to be on fire after colliding with [" + otherName + "] but it was!";

    assertEquals(errorMessage, expectTargetOnFire, target.isOnFire());
  }

  /**
   * Test if the damage state of the target matches the expected state after colliding with the
   * given object
   *
   * @param target         The target object
   * @param other          The other object
   * @param expectedDamage The expected state of damage on the target object
   */
  private void testDamaged(final GameObject target, final GameObject other, final boolean expectedDamage) {
    final String targetName = target.getClass().getSimpleName();
    final String otherName = other.getClass().getSimpleName();

    final String errorMessage = expectedDamage
        ? "Expected [" + targetName + "] to be damaged after colliding with [" + otherName + "] but it was not!" 
        : "Expected [" + targetName + "] not to be damaged after colliding with [" + otherName + "] but it was!";

    assertEquals(errorMessage, expectedDamage, target.isDamaged());
  }

}
