package org.jenkinsci.plugins.gwt;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class ArrayEnumeration implements Enumeration<String> {

  private final String[] array;

  private int index;

  public ArrayEnumeration(final String[] array) {
    this.array = array;
  }

  @Override
  public boolean hasMoreElements() {
    return index < array.length;
  }

  @Override
  public String nextElement() {
    if (index < array.length) {
      return array[index++];
    }
    throw new NoSuchElementException();
  }
}
