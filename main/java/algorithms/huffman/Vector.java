package main.java.algorithms.huffman;

import java.util.Arrays;

class Vector {
    private byte[] array;

    public Vector(byte[] array) {
        this.array = array;
    }

    public byte[] getArray() {
        return array;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vector other = (Vector) obj;
        return Arrays.equals(array, other.array);
    }
}
