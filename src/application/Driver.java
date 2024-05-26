package application;

import java.io.File;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Driver extends Application {

	Stage stage;
	TextArea AreaFiles = new TextArea();
	TextArea AreaEquations = new TextArea();

	@Override
	public void start(Stage primaryStage) {

		try {
			Pane root = new Pane();

			Button back = new Button("Back");

			back.setMaxWidth(80.5);
			back.setTranslateY(50);
			back.setMaxHeight(30.5);
			back.setTranslateX(20);

			TextField path = new TextField();
			path.setTranslateX(100);
			path.setTranslateY(50);

			Button load = new Button("Load");

			load.setOnAction(e -> {
				FileChooser fileChooser = new FileChooser();

				File selectedFile = fileChooser.showOpenDialog(stage);
				String filePath = selectedFile.getPath();
				path.setText(filePath);
				try {

					readFile(filePath);

				} catch (Exception exx) {
					System.out.println("Error reading file");

				}
			});

			load.setMaxWidth(80.5);
			load.setTranslateY(50);
			load.setMaxHeight(30.5);
			load.setTranslateX(300);

			Text en = new Text("Equation");

			en.setTranslateY(95);

			AreaEquations.setMaxWidth(400);
			AreaEquations.setMaxHeight(200);
			AreaEquations.setTranslateY(100);
			AreaEquations.setTranslateX(0);

			Text fi = new Text("File");
			fi.setTranslateY(295);
			AreaFiles.setMaxWidth(400);
			AreaFiles.setMaxHeight(200);
			AreaFiles.setTranslateY(300);
			AreaFiles.setTranslateX(0);

			root.getChildren().addAll(back, path, load, AreaEquations, AreaFiles, en, fi);
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("project2 ");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);

	}

	public static boolean isValidFile(String s) {
		CursorStack<String> stack = new CursorStack<>();
		boolean res=true;
		
		///////////////////
		for(int i=0;i<s.length();i++) {
			
			int firstIndex = s.indexOf('<', i);
	        if (firstIndex == -1) {
	            break;
	        }

	        int finalIndex = s.indexOf('>', firstIndex + 1);
	        if (finalIndex == -1) {
	            res = false;
	        }

	        String tag = s.substring(firstIndex + 1, finalIndex);
	        if (tag.startsWith("/")) {
	            if (stack.isEmpty()) {
	                res = false;
	            }
	            String startTag = stack.pop();
	            if (!tag.substring(1).equals(startTag)) {
	                res = false;
	            }
	        } else {
	            stack.push(tag);
	        }
	        i = finalIndex;
		}
		///////////////
		
		return res;
	}
	
	public void readFile(String path) {
		
		try {

			File File = new File(path);
			Scanner scanner = new Scanner(File);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if (line.contains("<file>")) {
					int startIndex = line.indexOf("<file>") + "<file>".length();
					int endIndex = line.indexOf("</file>");
					String file = line.substring(startIndex, endIndex).trim();
					AreaFiles.appendText(file + "\n");
					System.out.println(file);
				}

				if (line.contains("<equation>")) {
					int startIndex = line.indexOf("<equation>") + "<equation>".length();
					int endIndex = line.indexOf("</equation>");
					String equation = line.substring(startIndex, endIndex).trim();

					boolean bool = isBalanced(equation);
					boolean flagValid = isValidExpression(equation);
					
					if (bool==true && flagValid==true) {
						String c = convert_infix_to_postfix(equation);
						AreaEquations.appendText(equation + "      >>>"+convert_infix_to_postfix(equation)+"      >>>"+calculatePostFix(c.trim()));
					} else if(bool==false) {
						Text text = new Text("UnBalanced");
						AreaEquations.appendText(equation + "      >>>  ");
						AreaEquations.appendText(text.getText());

					}
					else if(flagValid==false) {
						Text text1 = new Text("inValid");
						AreaEquations.appendText(equation + "      >>>  ");
						AreaEquations.appendText(text1.getText());
					}

					
					AreaEquations.appendText("\n");
					System.out.println(equation);

				}

			}
			scanner.close();
		} catch (Exception ex) {
			AreaEquations.appendText("invalid file: missing end tag");
		}
	}

	public static boolean isBalanced(String expression) {
		
		CursorStack<String> balancedStack = new CursorStack<>();
		
		for (int i = 0; i < expression.length(); i++) {
			
			char c = expression.charAt(i);
			
			switch (c + "") {
			case "{":
			case "[":
			case "(":
			case "<":
			
				balancedStack.push(c + "");
				break;
			case "}":
			case "]":
			case ")":
			case ">":

				if (balancedStack.isEmpty())
					return false;

				
				char o = balancedStack.pop().charAt(0); //prev 

				if (!((c == '}' && o == '{') || (c == ']' && o == '[') || (c == ')' && o == '(')
						|| (c == '>' && o == '<')))
					return false;
			}
		}
		return (balancedStack.isEmpty()) ? true : false;
	}

	public static boolean hasPrecedence(String op1, String op2) {
			if (op2.equals("(")) {
	            return false;
	        }
	        if ((op1.equals("*") || op1.equals("/") || op1.equals("%")) || (op2.equals("+") || op2.equals("-"))) {
	            return false;
	        }
	        
	        return true;
	    
		}
		
	public static String convert_infix_to_postfix(String s) {
			CursorStack<String> stack = new CursorStack<>();
			String postFix="";
			
			////////////////////
			String infix[]= s.split(" ");
			for(int i=0;i<infix.length;i++) {
				
				// if it's operator, the operator (+) or (-)
				if(infix[i].equals("+") || infix[i].equals("-")) {
					while(!stack.isEmpty() && !stack.peek().equals("(")) {
						postFix= postFix+" "+stack.pop();
					}
					stack.push(infix[i]);
				}
				// 1+2-3*(1+2)
				// if it's operator, the operator (*) or (/) or (%)
				else if(infix[i].equals("*") || infix[i].equals("/") || infix[i].equals("%")) {
					while( (!stack.isEmpty()) && (!stack.peek().equals("(")) && (!hasPrecedence(stack.peek(),infix[i])) ) {
						postFix= postFix+" "+stack.pop();
					}
					stack.push(infix[i]);
				}
				
				// if it's operator, the operator [--> ( <--] or (^) 
				else if(infix[i].equals("(") || infix[i].equals("^")) {
					stack.push(infix[i]);
				}
				
				// if it's operator, the operator [--> ) <--]
				else if(infix[i].equals(")")) {
					while(!stack.isEmpty() && !stack.peek().equals("(")) {
						postFix= postFix+" "+stack.pop();
					}
					stack.pop();
				}
				
				// if it's operand
				else {
					postFix= postFix+" "+infix[i];
				}
			}
			
			//////////////////////////
			while(!stack.isEmpty()) {
				postFix= postFix+" "+stack.pop();
			}
			return postFix;
		}

	public static int calculatePostFix(String s) {
			CursorStack<Integer> stack = new CursorStack<>();
			
			// to get the two operand
			int x=0;
			int y=0;
			
			String c[]=s.split(" ");
			///////////////////
			for(int i=0;i<c.length;i++) {
				
			///////////////////////////////////////////////////////////////
			/* if the char is operator --> pop two element(get two element)
			   from stack and make the operator on them */
				 
				if(c[i].equals("+") || c[i].equals("-") || c[i].equals("*") || c[i].equals("/") || c[i].equals("%") || c[i].equals("^"))  {
					  y = stack.pop(); // r u forget it's LIMFO (last in first out)
		              x = stack.pop();
		              
		              char c1 = c[i].charAt(0);
		              
						switch (c1) {
							case '+': {
								stack.push(x+y);
								break;
							}
							case '-':{
								stack.push(x-y);
								break;
							}
							case '*':{
								stack.push(x*y);
								break;
							}
							case '/':{
								stack.push(x/y);
								break;
							}
							case '%':{
								stack.push(x%y);
								break;
							}
							case '^':{
								stack.push((int) Math.pow(y, x));
								break;
							}
							default:{
								System.out.println("there is no operator");
							}
						}
				 }
				 ///////////
				// if it's operand
				 else {
					 stack.push(Integer.parseInt(c[i])); 
				 }
			}
			
			int res=stack.pop();
			
			return res;
		}
	
	public static boolean isValidExpression(String expression) {
		
		boolean result = true;
		
		CursorStack<Integer> digitStack = new CursorStack();
		CursorStack<Character> operatorStack = new CursorStack();
		
		boolean isTrue = true;
		
		for (int i = 0; i < expression.length(); i++) {
			
			char temp = expression.charAt(i);
			
			if (isDigit(temp)) {
				
				digitStack.push(temp - '0');
				
				if (isTrue) {
					isTrue = true;
				} else {
					return false;
				}
			}
			else if (isOperator(temp)) {
				operatorStack.push(temp);
				isTrue = true;
			}
		}
		while (!operatorStack.isEmpty()) {
			char c = operatorStack.pop();
			if (!isOperator(c)) {
				return false;
			}
			if (digitStack.size() < 2) {
				return false;
			} else {
				digitStack.pop();
			}
		}
		if (digitStack.size() > 1 || !operatorStack.isEmpty()) {
			return false;
		}
		return result;
	}

	public static boolean isDigit(char c) {
		
		if (c >= '0' && c <= (char)1000000000) {
			return true;
		}
		return false;
	}

	public static boolean isOperator(char c) {
		if (c == '+' || c == '-' || c == '*') {
			return true;
		}
		return false;
	}

}