package application;

public class CursorStack<Object extends Comparable<Object>> implements StackInterface<Object> {

	CursorArray<Object> stack = new CursorArray<>(100);
	int list = stack.createList();
	int size = 0;

	@Override
	public void push(Object data) {
		if (!stack.insertAtHead(data, list))
			System.out.println("Error Satck Overflow!!!!");
		else
			size++;
	}

	@Override
	public Object pop() {
		size--;
		return (Object) stack.deleteFirst(list);
	}

	@Override
	public Object peek() {
		return (Object) stack.getFirst(list);
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty(list);
	}

	@Override
	public void clear() {
		while (!isEmpty()) {
			stack.deleteFirst(list);
		}

	}

	public int size() {

		return size;
	}

}
