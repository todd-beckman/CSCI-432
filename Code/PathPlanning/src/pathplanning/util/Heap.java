/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathplanning.util;

import java.util.HashMap;

/**
 *
 * @author Alex
 * @param <E>
 */
public class Heap<E extends Comparable> {

    private final Comparable[] arr;
    private final int cap;
    private int len;

    private HashMap<E, Integer> indices;

    public Heap(int cap) {
        arr = new Comparable[cap];
        this.cap = cap;
        len = 0;
        this.indices = new HashMap<>();
    }

    private void swap(int a, int b) {
        indices.put((E) arr[a], b);
        indices.put((E) arr[b], a);
        Comparable temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /**
     *
     * @param toAdd Element to be added, null input not allowed.
     * @throws IndexOutOfBoundsException
     * @throws IllegalArgumentException
     */
    public void insert(E toAdd) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (toAdd == null) {
            throw new IllegalArgumentException("Null input not allowed.");
        }
        if (len == cap) {
            throw new IndexOutOfBoundsException("Heap full.");
        }
        arr[len] = toAdd;
        indices.put(toAdd, len);
        reheap();
        ++len;
    }

    /**
     *
     * @return Minimum element, null if empty.
     */
    public E pop() {
        if (len == 0) {
            return null;
        }
        E toR = (E) arr[0];
        arr[0] = arr[--len];
        sift();
        return toR;
    }

    private void sift() {
        int index = 0;
        E curr = (E) arr[index];
        while (true) {
            int chi = index * 2 + 1; //check left child
            if (chi >= len) {
                break;
            }
            if (arr[chi].compareTo(curr) < 0) {
                swap(chi, index);
                index = chi;
            } else {
                ++chi; //check right child
                if (chi >= len) {
                    break;
                }
                if (arr[chi].compareTo(curr) < 0) {
                    swap(chi, index);
                    index = chi;
                } else {
                    break;
                }
            }

        }
    }

    private void siftFrom(int index) {
        E curr = (E) arr[index];
        while (true) {
            int chi = index * 2 + 1; //check left child
            if (chi >= len) {
                break;
            }
            if (arr[chi].compareTo(curr) < 0) {
                swap(chi, index);
                index = chi;
            } else {
                ++chi; //check right child
                if (chi >= len) {
                    break;
                }
                if (arr[chi].compareTo(curr) < 0) {
                    swap(chi, index);
                    index = chi;
                } else {
                    break;
                }
            }

        }
    }

    private void reheap() {
        int index = len;
        while (true) {
            int par = (index - 1) / 2;
            if (arr[par].compareTo(arr[index]) > 0) {
                swap(par, index);
                index = par;
            } else {
                return;
            }
        }
    }

    private void reheapFrom(int index) {
        while (true) {
            int par = (index - 1) / 2;
            if (arr[par].compareTo(arr[index]) > 0) {
                swap(par, index);
                index = par;
            } else {
                return;
            }
        }
    }

    @Override
    public String toString() {
        if (arr.length == 0) {
            return "[]";
        }
        String s = "[";
        for (int i = 0; i < len - 1; i++) {
            s += arr[i].toString() + ", ";
        }
        s += arr[len - 1] + "]";
        return s;
    }

    public int len() {
        return len;
    }

    public E peek() {
        if (len == 0) {
            return null;
        }
        E toR = (E) arr[0];
        return toR;
    }

    /**
     * I have no idea if this works.
     *
     * @param e
     */
    public void remove(E e) {
        int index = indices.get(e);
        swap(index, len - 1);
        len--;
        siftFrom(index);
        indices.remove(e);
    }

}
