package application;

public class PostfixEval {

	protected static int evalPostfix(String express) {
		CursorStack<Integer> st = new CursorStack<>();

		for (int i = 0; i < express.length(); i++) {
			char ch = express.charAt(i);

			if (Character.isDigit(ch))
				st.push(ch - '0');

			else {
				int value1 = st.pop();
				int value2 = st.pop();

				switch (ch) {
				case '+':
					st.push(value2 + value1);
					break;

				case '-':
					st.push(value2 - value1);
					break;

				case '*':
					st.push(value2 * value1);
					break;
				case '/':
					st.push(value2 / value1);
					break;
				}
			}
		}
		return st.pop();
	}
}
