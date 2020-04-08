import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size = 0;
    private Item[] items;
    private int sizeArray = 2;
    private int head = -1;
    private int tail = -1;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.items = (Item[]) new Object[sizeArray];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (this.size == this.sizeArray) {
            Item[] newArray = (Item[]) new Object[this.sizeArray * 2];

//            System.out.println("tail " + tail);
            for (int i = 0; i < this.sizeArray; i++) {
                newArray[i] = this.items[(head + i) % sizeArray];
            }
            this.items = newArray;
            this.tail = sizeArray - 1;
            this.sizeArray *= 2;
            this.head = 0;
        }
        if (head == -1) {
            head = 0;
        }
        tail = (tail + 1) % sizeArray;
        this.items[tail] = item;
        this.size += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) throw new NoSuchElementException();
        Item returnItem = this.items[head];
        this.head = (head + 1) % sizeArray;
        size -= 1;

        if (size > 4 && size < 0.25 * sizeArray) {
            Item[] newArray = (Item[]) new Object[(int) (this.sizeArray / 2)];
            for (int i = 0; i < size; i++) {
                newArray[i] = this.items[(head + i) % sizeArray];
            }
            this.items = newArray;
            this.tail = size - 1;
            this.head = 0;
            this.sizeArray = (int) (sizeArray / 2);
        }

        return returnItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()) throw new NoSuchElementException();
        Item[] temp = (Item[]) new Object[size];
        for (int i = 0; i < size; i++) {
            temp[i] = this.items[(head + i) % sizeArray];
        }
        int id = StdRandom.uniform(size);
        return temp[id];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] iterArray;
        private int rank;

        public RandomizedQueueIterator() {
            iterArray = (Item[]) new Object[size];
            rank = size;
            for (int i = 0; i < size; i++) {
                iterArray[i] = items[(head + i) % sizeArray];
            }
        }

        @Override
        public boolean hasNext() {
            return rank > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int randId = StdRandom.uniform(rank);
            Item returnItem = iterArray[randId];
            iterArray[randId] = iterArray[rank - 1];
            rank -= 1;
            return returnItem;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(165);
        rq.enqueue(199);
        rq.enqueue(173);

        for (int i = 0; i < 10; i++) {
            System.out.println(rq.sample());
        }
    }
}
