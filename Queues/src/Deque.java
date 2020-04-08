import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Node next;
        private Node prev;
        private Item content;

        public Node(Item item) {
            this.content = item;
            this.next = null;
            this.prev = null;
        }

        public Node(Item item, Node prevNode, Node nextNode) {
            this.content = item;
            this.next = nextNode;
            this.prev = prevNode;
        }

        public Node getPrev() {
            return this.prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return this.next;
        }

        public Item getContent() {
            return this.content;
        }

        public void setNext(Node nextNode) {
            this.next = nextNode;
        }
    }

    private Node first;
    private Node last;
    private int size = 0;

    public Deque() {
        this.first = null;
        this.last = null;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node newNode = new Node(item, null, this.first);
        if (this.first != null) this.first.setPrev(newNode);
        this.first = newNode;
        if (this.last == null) this.last = newNode;
        this.size += 1;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node newNode = new Node(item, this.last, null);
        if (this.last != null) this.last.setNext(newNode);
        this.last = newNode;
        if (this.first == null) this.first = newNode;
        this.size += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) throw new NoSuchElementException();
        Item returnItem = this.first.getContent();
        this.first = this.first.getNext();

        if (this.first != null) this.first.setPrev(null);
        else this.last = null;

        this.size -= 1;
        return returnItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.isEmpty()) throw new NoSuchElementException();
        Item returnItem = this.last.getContent();
        this.last = this.last.getPrev();

        if (this.last != null) this.last.setNext(null);
        else this.first = null;

        this.size -= 1;
        return returnItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator(this.first);
    }

    private class DequeIterator implements Iterator<Item> {
        private Node first;

        public DequeIterator(Node first) {
            this.first = first;
        }

        @Override
        public boolean hasNext() {
            return this.first != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item returnItem = this.first.getContent();
            this.first = this.first.getNext();
            return returnItem;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addLast(3);
        deque.addFirst(4);
        for (int i : deque) {
            System.out.println(i);
        }
        System.out.println("");
        System.out.println(deque.removeLast());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());

        System.out.println("");
        for (int i : deque) {
            System.out.println(i);
        }

    }

}
