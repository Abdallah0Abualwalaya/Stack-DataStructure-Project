package application;

public class CursorArray<T extends Comparable<T>> {
	Node<T>[] cursorArray;

	public CursorArray(int capacity) {
		cursorArray = new Node[capacity];
		initialization();
	}

	private int initialization() {
		for (int i = 0; i < cursorArray.length - 1; i++)
			cursorArray[i] = new Node<>(null, i + 1);
		cursorArray[cursorArray.length - 1] = new Node<>(null, 0);
		return 0;
	}

	public int size() {
		return cursorArray.length;
	}

	private int malloc() {
		int p = cursorArray[0].getNext();
		cursorArray[0].setNext(cursorArray[p].getNext());
		return p;
	}

	private void free(int p) {
		cursorArray[p] = new Node(null, cursorArray[0].getNext());
		cursorArray[0].setNext(p);
	}

	private boolean isNull(int l) {
		return cursorArray[l] == null;
	}

	public boolean isEmpty(int l) {
		return cursorArray[l].getNext() == 0;
	}

	private boolean isLast(int p) {
		return cursorArray[p].getNext() == 0;
	}

	public int createList() {
		int l = malloc();
		if (l == 0)
			System.out.println("Error: Out of space!!!");
		else
			cursorArray[l] = new Node("-", 0);
		return l;
	}

	public boolean insertAtHead(T data, int l) {
		if (isNull(l))
			return false;
		int p = malloc();
		if (p != 0) {
			cursorArray[p] = new Node(data, cursorArray[l].next);
			cursorArray[l].next = p;
		} else {

			return false;
		}
		return true;
	}

	public void insertAtLast(T data, int l) {
		if (isNull(l))
			return;
		int p = malloc();
		if (p != 0) {
			while (!isLast(l))
				l = cursorArray[l].next;
			cursorArray[p] = new Node(data, 0);
			cursorArray[l].next = p;
		} else
			System.out.println("Error: Out of space!!!");
	}

	public void insertSorted(T data, int l) {
		if (isNull(l))
			return;
		int p = malloc();
		if (p != 0) {
			while (!isLast(l) && cursorArray[cursorArray[l].next].data.compareTo(data) < 0)
				l = cursorArray[l].next;
			if (cursorArray[l].next == 0)
				cursorArray[p] = new Node(data, 0);
			else
				cursorArray[p] = new Node(data, cursorArray[l].next);
			cursorArray[l].next = p;

		} else
			System.out.println("Error: Out of space!!!");
	}

	public void deleteAtLast(int l) {
		if (isNull(l) || isEmpty(l)) {
			System.out.println("Empty List!!!");
			return;
		}

		while (!isLast(cursorArray[l].next))
			l = cursorArray[l].next;
		int p = cursorArray[l].next;
		cursorArray[l].next = 0;
		free(p);

	}

	public void traversList(int l) {
		System.out.print("list_" + l + "-->");
		while (!isNull(l) && !isEmpty(l)) {
			l = cursorArray[l].next;
			System.out.print(cursorArray[l] + "-->");
		}
		System.out.println("null");
	}

	public int find(T data, int l) {
		while (!isNull(l) && !isEmpty(l)) {
			l = cursorArray[l].next;
			if (cursorArray[l].data.equals(data))
				return l;
		}
		return -1;
	}

	public int findPrevious(T data, int l) {
		while (!isNull(l) && !isEmpty(l)) {
			if (cursorArray[cursorArray[l].next].data.equals(data))
				return l;
			l = cursorArray[l].next;
		}
		return -1;
	}

	public T deleteFirst(int l) {
		if (!isNull(l) && !isEmpty(l)) {
			int p = cursorArray[l].next;
			cursorArray[l].next = cursorArray[cursorArray[l].next].next;
			Node temp = cursorArray[p];
			return (T) temp.getData();
		}
		return null;
	}

	public T getFirst(int l) {
		if (!isNull(l) && !isEmpty(l))
			return cursorArray[cursorArray[l].next].getData();
		return null;
	}

	public Node delete(T data, int l) {
		int p = findPrevious(data, l);
		if (p != -1) {
			int c = cursorArray[p].next;
			Node temp = cursorArray[c];
			cursorArray[p].setNext(temp.getNext());
			free(c);
		}
		return null;
	}
}