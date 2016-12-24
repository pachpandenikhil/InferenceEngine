import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing arithmetic and logical operators and their utility functions.
 * 
 * @author pachpandenikhil
 *
 */
class Operator {
	public static final char PLUS 							= '+';
	public static final char MINUS 							= '-';
	public static final char MULTIPLY 						= '*';
	public static final char DIVIDE 						= '/';
	public static final char NEGATE 						= '~';
	public static final char AND 							= '&';
	public static final char OR 							= '|';
	public static final char EQUALS 						= '=';
	public static final char LEFT_PARENTHESIS 				= '(';
	public static final char RIGHT_PARENTHESIS 				= ')';
	public static final char PREDICATE_LEFT_PARENTHESIS 	= '[';
	public static final char PREDICATE_RIGHT_PARENTHESIS 	= ']';
	public static final String IMPLIES 						= "=>";
	
	/**
	 * Returns true if the input is an operator.
	 * @param input the char to be tested.
	 * @return true if and only if the char is an operator.
	 */
	
	public static boolean isOperator(char input) {

		boolean retVal = false;
		switch (input) {

		case Operator.PLUS				: 
		case Operator.MINUS				:
		case Operator.MULTIPLY			: 
		case Operator.DIVIDE			:
		case Operator.NEGATE			:
		case Operator.AND				:
		case Operator.OR				:
		case Operator.EQUALS			:
		case Operator.LEFT_PARENTHESIS	: 
		case Operator.RIGHT_PARENTHESIS	: 
			retVal = true; 
			break;

		default: 
			retVal = false; 
			break;
		}

		return retVal;
	}

}

/**
 * Class for infix to postfix conversion.
 * @author pachpandenikhil
 *
 */
class InfixToPostfix {

	private Stack<Character> stack;
	private String postfix;

	public InfixToPostfix() {
		stack = new Stack<>();
		postfix = "";
	}
	
	/**
	 * Converts the infix expression to postfix.
	 * @param infix the infix expression in string form.
	 * @return postfix expression in string form.
	 */
	public String convertToPostfix(String infix) {
		for (int j = 0; j < infix.length(); j++) {
			char ch = infix.charAt(j);
			switch (ch) {

			case Operator.PLUS	: 
			case Operator.MINUS	:
				gotOperator(ch, 1); 
				break; 

			case Operator.MULTIPLY	: 
			case Operator.DIVIDE	:
			case Operator.NEGATE	:
			case Operator.AND		:
			case Operator.OR		:
			case Operator.EQUALS	:
				gotOperator(ch, 2); 
				break; 

			case Operator.LEFT_PARENTHESIS: 
				stack.push(ch);
				break;

			case Operator.RIGHT_PARENTHESIS: 
				gotParenthesis(); 
				break;

			default: 
				postfix = postfix + ch; 
				break;
			}
		}
		while (!stack.isEmpty()) {
			postfix = postfix + stack.pop();
		}
		return postfix; 
	}
	
	/**
	 * Called when an operator is encountered during postfix conversion.
	 * @param opThis the current operator
	 * @param prec1 the operator precedence
	 */
	private void gotOperator(char opThis, int prec1) {
		while (!stack.isEmpty()) {
			char opTop = stack.pop();
			if (opTop == Operator.LEFT_PARENTHESIS) {
				stack.push(opTop);
				break;
			}
			else {
				int prec2;
				if (opTop == Operator.PLUS || opTop == Operator.MINUS)
					prec2 = 1;
				else
					prec2 = 2;
				if (prec2 < prec1) { 
					stack.push(opTop);
					break;
				}
				else
					postfix = postfix + opTop;
			}
		}
		stack.push(opThis);
	}

	/**
	 * Called when a parenthesis is encountered during postfix expression conversion.
	 *  
	 */
	private void gotParenthesis(){ 
		while (!stack.isEmpty()) {
			char chx = stack.pop();
			if (chx == Operator.LEFT_PARENTHESIS) 
				break; 
			else
				postfix = postfix + chx; 
		}
	}
}

/**
 * Class representing a node in the expression tree.
 * @author pachpandenikhil
 *
 */
class Node {

	protected String value;
	protected Node left, right;

	public Node(String item) {
		value = item;
		left = right = null;
	}
	
	/**
	 * Return true if the node is an OR operator.
	 * @return true if and only if the node is an OR operator.
	 */
	public boolean isOrOperator() {
		return isOperatorNode(this, Operator.OR);
	}
	
	/**
	 * Return true if the node is an AND operator.
	 * @return true if and only if the node is an AND operator.
	 */
	public boolean isAndOperator() {
		return isOperatorNode(this, Operator.AND);
	}
	
	/**
	 * Return true if the node is a negation operator.
	 * @return true if and only if the node is a negation operator.
	 */
	public boolean isNegateOperator() {
		return isOperatorNode(this, Operator.NEGATE);
	}
	
	/**
	 * Return true if the node is an implication operator.
	 * @return true if and only if the node is an implication operator.
	 */
	public boolean isImpliesOperator() {
		return isOperatorNode(this, Operator.EQUALS);
	}
	
	/**
	 * Returns true if the node is the operator specified by the <CODE>operator</CODE> argument.
	 * @param node the node to be verified.
	 * @param operator the operator against which the node is to be verified.
	 * @return true if and only if the node is the operator specified by the <CODE>operator</CODE> argument.
	 */
	public static boolean isOperatorNode(Node node, char operator) {
		boolean retVal = false;
		if(node != null) {
			char nodeValue = node.value.charAt(0);
			if(nodeValue == operator) {
				retVal = true;
			}
		}
		return retVal;
	}
	
	/**
	 * Returns true if the node is a negated predicate.<BR><B>For eg.</B> ~Run[x].
	 * @return true if and only if the node is a negated predicate.
	 */
	public boolean isNegatedPredicate() {
		boolean retVal = false;
		if(this.value != null) {
			if( (this.value.charAt(0) == Operator.NEGATE) && (this.value.length() > 1) ) {
				retVal = true;
			}
		}
		return retVal;
	}
}

/**
 * Class representing the postfix expression tree.
 * @author pachpandenikhil
 *
 */
class ExpressionTree {
	private String inorderRepresentation;
	
	/**
	 * Returns the inorder traversal of the tree.
	 * @param node the root node.
	 * @return the inorder traversal.
	 */
	public String getInorderRepresentation(Node node) {
		inorderRepresentation = "";
		inorder(node);
		return inorderRepresentation;
	}
	
	/**
	 * Performs inorder traversal of the tree rooted at <CODE>node</CODE>.
	 * @param node the root node.
	 */
	private void inorder(Node node) {
		if (node != null) {
			inorder(node.left);
			inorderRepresentation += node.value;
			inorder(node.right);
		}
	}

	/**
	 * Constructs the expression tree from postfix expression.
	 * @param postfix the char array representing the postfix expression.
	 * @return the root node of the expression tree.
	 */
	public Node constructTree(char postfix[]) {

		Stack<Node> stack = new Stack<>();
		Node t, t1, t2;

		//Traverse through every character of
		//input expression
		for (int i = 0; i < postfix.length; i++) {

			//If operand, simply push into stack
			if (!Operator.isOperator(postfix[i])) {

				String nodeValue = postfix[i++] + "";
				while (postfix[i] != Operator.PREDICATE_RIGHT_PARENTHESIS) {
					nodeValue += postfix[i++]; 
				}
				nodeValue += Operator.PREDICATE_RIGHT_PARENTHESIS;
				t = new Node(nodeValue);
				stack.push(t);
			}
			else if (postfix[i] == Operator.NEGATE) {
				t = new Node(postfix[i] + "");
				t1 = stack.pop();
				t.right = t1;
				stack.push(t);
			}
			else { 	
				t = new Node(postfix[i] + "");
				//Pop two top nodes
				//Store top
				t1 = stack.pop();      //Remove top
				t2 = stack.pop();
				//make them children
				t.right = t1;
				t.left = t2;
				//Add this subexpression to stack
				stack.push(t);
			}
		}
		//only element will be root of expression tree
		t = stack.peek();
		stack.pop();

		return t;
	}
}

/**
 * Class representing the inference engine's knowledge base.
 * @author pachpandenikhil
 *
 */
class KnowledgeBase {

	private Map<String, Map<String, List<List<String>>>> KB;
	private int variableSequence;
	public static final String POSITIVE_SENTENCE_KEY 	= "Positive";
	public static final String NEGATIVE_SENTENCE_KEY 	= "Negative";
	public static final String ARG_TYPE_VARIABLE 		= "Variable";
	public static final String ARG_TYPE_CONSTANT 		= "Constant";

	public KnowledgeBase() {
		KB = new HashMap<String, Map<String,List<List<String>>>>();
		variableSequence = 0;
	}
	
	/**
	 * Adds a fact to the knowledge base.
	 * @param predicate the predicate against which the fact is to be added.
	 * @param sentenceKey the key of the predicate against which the fact is to be added.
	 * @param fact the fact to be added.
	 */
	private void addFact(String predicate, String sentenceKey, List<String> fact) {
		if((predicate != null) && (sentenceKey != null) && (fact != null) ) {
			if(KB.containsKey(predicate)) {
				Map<String, List<List<String>>> sentenceMap = KB.get(predicate);
				List<List<String>> sentenceList = sentenceMap.get(sentenceKey);
				List<String> sentence = new ArrayList<>(fact);
				sentenceList.add(sentence);
				sentenceMap.put(sentenceKey, sentenceList);
				KB.put(predicate, sentenceMap);
			}
			else {							
				List<String> sentence = new ArrayList<>(fact);
				List<List<String>> sentenceList = new ArrayList<>();
				sentenceList.add(sentence);
				Map<String, List<List<String>>> sentenceMap = new HashMap<>();
				sentenceMap.put(sentenceKey, sentenceList);

				//creating the entry for the other key
				if(sentenceKey.equalsIgnoreCase(POSITIVE_SENTENCE_KEY)) {
					sentenceMap.put(NEGATIVE_SENTENCE_KEY, new ArrayList<>());
				}
				else {
					sentenceMap.put(POSITIVE_SENTENCE_KEY, new ArrayList<>());
				}
				KB.put(predicate, sentenceMap);
			}
		}
	}
	
	/**
	 * Stores a fact in the knowledge base.<BR>Separates the conjuncts and standardizes the variables before adding the fact. 
	 * @param fact the fact to be stored.
	 * @return true if and only if the fact was successfully added to the knowledge base.
	 */
	public boolean store(String fact) {
		boolean retVal = false;
		if(fact != null) {
			String[] conjuncts = fact.split(Operator.AND + "");
			String[] updatedConjuncts = new String[conjuncts.length];

			//Standardizing variables
			for (int i = 0; i < conjuncts.length; i++) {
				String conjunct = conjuncts[i];
				conjunct = standardizeVariables(conjunct);
				updatedConjuncts[i] = conjunct;
			}

			for(String conjunct : updatedConjuncts) {
				String[] predicates = conjunct.split("\\" + Operator.OR + "");
				for(String predicate : predicates) {
					String predicateName = getPredicateName(predicate);
					if(isNegativePredicate(predicate)) {
						addFact(predicateName, NEGATIVE_SENTENCE_KEY, Arrays.asList(predicates));	
					}
					else {
						addFact(predicateName, POSITIVE_SENTENCE_KEY, Arrays.asList(predicates));
					}
				}
			}
			retVal = true;
		}
		return retVal;
	}
	
	/**
	 * Returns the predicate name(i.e without the variables) of the predicate.<BR>In case of negated predicates, the negation operator is ignored.
	 * @param predicate the predicate whose name is required.
	 * @return the predicate name.
	 */
	public String getPredicateName(String predicate) {
		String name = "";
		if(predicate != null) {
			//removing arguments
			name = predicate.substring(0, predicate.indexOf(Operator.PREDICATE_LEFT_PARENTHESIS));
			if(isNegativePredicate(name)) {
				name = name.substring(1, name.length());
			}
		}
		return name;
	}
	
	/**
	 * Returns the next alphabet in the sequence starting from 'a'.
	 * @param index the index of the next alphabet. a's index : 0.
	 * @return the next alphabet in the sequence.
	 */
	private String sequenceGenerator(int index) {
        return index < 0 ? "" : sequenceGenerator((index / 26) - 1) + (char)(97 + index % 26);
    }
	
	/**
	 * Standardizes the variables of the fact.
	 * @param fact the string representation of the fact.
	 * @return the fact with standardized variables.
	 */
	private String standardizeVariables(String fact) {
		if(fact != null) {

			Map<String, String> replacementMap = new HashMap<>();
			Map<String, String> processedArguments = new HashMap<>();

			String argumentsRegEx = "\\[(.*?)\\]";
			Pattern pattern = Pattern.compile(argumentsRegEx);
			Matcher matcher = pattern.matcher(fact);
			
			while (matcher.find()) {
				String group = matcher.group();
				List<String> args = getArguments(group);
				for(String arg : args) {
					if(!processedArguments.containsKey(arg)) {
						String replacementArg = "";
						if(!isConstant(arg)) {
							replacementArg = sequenceGenerator((variableSequence++));
						}
						else {
							replacementArg = arg;
						}
						
						processedArguments.put(arg, replacementArg);
					}
				}
				
				String groupReplacement = Operator.PREDICATE_LEFT_PARENTHESIS + "";
				for(String arg : args) {
					groupReplacement += processedArguments.get(arg);
					groupReplacement += ",";
				}
				groupReplacement = groupReplacement.substring(0, groupReplacement.length()-1);
				groupReplacement += Operator.PREDICATE_RIGHT_PARENTHESIS + "";
				
				group = group.replaceAll("\\[", "\\\\[");
				group = group.replaceAll("\\]", "\\\\]");
				
				groupReplacement = groupReplacement.replaceAll("\\[", "\\\\[");
				groupReplacement = groupReplacement.replaceAll("\\]", "\\\\]");
				
				
				replacementMap.put(group, groupReplacement);
			}
			
			//Replacing all the arguments
			for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
			    String originalGroup = entry.getKey();
			    String replacementGroup = entry.getValue();
			    fact = fact.replaceAll(originalGroup, replacementGroup);
			}
		}

		return fact;
	}
	
	/**
	 * Returns true if the <CODE>input</CODE> is a constant.<BR><B>For eg.</B> Bob.
	 * @param arg the argument of the predicate to be verified
	 * @return true if and only if the <CODE>input</CODE> is a constant.
	 */
	private boolean isConstant(String arg) {
		boolean retVal = false;
		if(arg != null) {
			if((Character.isUpperCase(arg.charAt(0))) && (arg.length() > 1)) {
				retVal = true;
			}
		}
		return retVal;
	}
	
	/**
	 * Return a list of arguments extracted from the <CODE>strArgs</CODE>. 
	 * @param strArgs the arguments in comma separated string form.
	 * @return list of arguments.
	 */
	public List<String> getArguments(String strArgs) {
		List<String> args = new ArrayList<>();
		if(strArgs != null) {
			String[] argsArr = strArgs.split(",");
			for(String arg : argsArr) {
				arg = arg.replaceAll("\\[", "");
				arg = arg.replaceAll("\\]", "");
				args.add(arg);
			}
		}
		return args;
	}
	
	/**
	 * Returns true if the predicate is preceded by a negation operator.
	 * @param predicate the predicate to be verified.
	 * @return true if and only if the predicate is preceded by a negation operator.
	 */
	public boolean isNegativePredicate(String predicate) {
		boolean retVal = false;
		if(predicate != null) {
			if(predicate.charAt(0) == Operator.NEGATE) {
				retVal = true;
			}
		}
		return retVal;
	}
	
	/**
	 * Returns true if the knowledge base contains the predicate <CODE>predicate</CODE>.
	 * @param predicate the predicate to be found.
	 * @return true if and only if the knowledge base contains the predicate.
	 */
	public boolean predicateExists(String predicate) {
		boolean exists = false;
		if(predicate != null) {
			exists = KB.containsKey(predicate);
		}
		return exists;
	}
	
	/**
	 * Returns the map of all the facts/sentences containing the predicate <CODE>predicate</CODE>.
	 * @param predicate the sentences containing this predicate are returned.
	 * @return the map of all the facts/sentences containing the predicate <CODE>predicate</CODE>.
	 */
	public Map<String, List<List<String>>> getPredicateSentenceMap(String predicate) {
		Map<String, List<List<String>>> sentenceMap = null;
		if(predicate != null) {
			sentenceMap = KB.get(predicate);
		}
		return sentenceMap;
	}
	
	/**
	 * Returns the type of argument from Constant or Variable.
	 * @param argument the argument whose type is to be found. 
	 * @return the type of argument from Constant or Variable.
	 */
	public String getArgumentType(String argument) {
		String type = ARG_TYPE_CONSTANT;
		if(argument != null) {
			char firstChar = argument.charAt(0);
			if(Character.isUpperCase(firstChar)) {
				type = ARG_TYPE_CONSTANT;
			}
			else {
				type = ARG_TYPE_VARIABLE;
			}
		}
		return type;
	}
	
	/**
	 * Removes the fact from the knowledge base.
	 * @param fact the fact to be removed.
	 * @return true if and only if the fact is successfully removed.
	 */
	public boolean remove(String fact) {
		boolean retVal = false;
		if(fact != null) {
			String predicateName = getPredicateName(fact);
			if(isNegativePredicate(fact)) {
				removeFact(predicateName, NEGATIVE_SENTENCE_KEY, fact);	
			}
			else {
				removeFact(predicateName, POSITIVE_SENTENCE_KEY, fact);
			}
			retVal = true;
		}
		return retVal;		
	}
	
	/**
	 * Removes the fact from the knowledge base.
	 * @param predicate the predicate against which the fact is stored in the knowledge base.
	 * @param sentenceKey the key of the predicate against which the fact is stored.
	 * @param fact the fact to be removed.
	 */
	private void removeFact(String predicate, String sentenceKey, String fact) {
		if((predicate != null) && (sentenceKey != null) && (fact != null) ) {
			if(KB.containsKey(predicate)) {
				Map<String, List<List<String>>> sentenceMap = KB.get(predicate);
				List<List<String>> sentenceList = sentenceMap.get(sentenceKey);
				List<List<String>> updatedSentenceList = new ArrayList<>();
				for(List<String> sentence : sentenceList) {
					String currentFact = Agent.getQuery(sentence);
					if(!currentFact.equalsIgnoreCase(fact)) {
						updatedSentenceList.add(sentence);
					}
				}
				sentenceMap.put(sentenceKey, updatedSentenceList);
				KB.put(predicate, sentenceMap);
			}
		}		
	}
}

/**
 * Class for converting FOL sentences to CNF form.
 * @author pachpandenikhil
 *
 */
class CNFConverter {

	private ExpressionTree tree;
	private InfixToPostfix converter;


	public CNFConverter() {
		tree = new ExpressionTree();
		converter = new InfixToPostfix();
	}

	/**
	 * Replaces parenthesis from '()' to '[]'.
	 * @param inputExpr the FOL sentence.
	 * @return the FOL sentence with replaced parenthesis.
	 */
	private static String replacePredicateParanthesis(String inputExpr) {
		StringBuilder input = new StringBuilder(inputExpr);
		if(input != null) {
			for (int idx = 0; idx < input.length(); idx++) {
				char ch = input.charAt(idx);

				//replacing left parenthesis
				if(ch == Operator.LEFT_PARENTHESIS) {
					if( (idx != 0) && (!Operator.isOperator(input.charAt(idx - 1))) ) {
						input.setCharAt(idx, Operator.PREDICATE_LEFT_PARENTHESIS);
					}
				}
				//replacing right parenthesis
				else if(ch == Operator.RIGHT_PARENTHESIS) {
					if( (!Operator.isOperator(input.charAt(idx - 1))) && (input.charAt(idx - 1) != Operator.PREDICATE_RIGHT_PARENTHESIS)) {
						input.setCharAt(idx, Operator.PREDICATE_RIGHT_PARENTHESIS);
					}
				}
			}
		}
		return input.toString();
	}
	
	/**
	 * Performs the following:<BR>
	 * <UL>
	 * <LI>Removes whitespaces.</LI>
	 * <LI>Replaces implication operator.</LI>
	 * <LI>Replaces predicate parenthesis.</LI>
	 * </UL>
	 * @param input the FOL sentence.
	 * @return the updated FOL sentence.
	 */
	public String preProcessInput(String input) {

		//trimming all the whitespace
		input = input.trim().replaceAll(" +", "");

		//replacing '=>' with '='
		input = input.replaceAll(Operator.IMPLIES, Operator.EQUALS + "");

		//replacing predicate parenthesis with '[' and ']'		
		input = replacePredicateParanthesis(input);

		return input;

	}
	
	/**
	 * Converts the FOL sentence to CNF.
	 * @param fact the FOL sentence
	 * @return the sentence in CNF form.
	 */
	public String convertToCNF(String fact) {
		String CNFExpression = "";
		if(fact != null) {
			fact = preProcessInput(fact);
			String postfix = converter.convertToPostfix(fact);
			char[] charArray = postfix.toCharArray();
			Node root = tree.constructTree(charArray);
			replaceImplications(root);
			moveNegationInwards(root);
			root = handleRootNodeNegation(root);
			distributeAndOverOr(root);
			CNFExpression = tree.getInorderRepresentation(root);
		}
		return CNFExpression;
	}
	
	/**
	 * Distributes AND operator over OR operator.
	 * @param node the root of the expression tree.
	 */
	private void distributeAndOverOr(Node node) {
		if(node != null) {
			if(node.isOrOperator()) {
				Node leftNode = node.left;
				Node rightNode = node.right;
				if( (leftNode != null) && (rightNode != null) && leftNode.isAndOperator() && rightNode.isAndOperator() ) {
					performDoubleDistribution(node);

				}
				else if ( (leftNode != null) && leftNode.isAndOperator() ) {
					performDoubleDistribution(node);
				}
				else if( (rightNode != null) && rightNode.isAndOperator()) {
					node.value = Operator.AND + "";

					Node leftORNode = new Node(Operator.OR + "");
					leftORNode.left = leftNode;
					leftORNode.right = rightNode.left;

					Node rightORNode = new Node(Operator.OR + "");
					rightORNode.left = leftNode;
					rightORNode.right = rightNode.right;
					
					node.left = leftORNode;
					node.right = rightORNode;

				}
			}

			distributeAndOverOr(node.left);
			distributeAndOverOr(node.right);
		}

	}
	
	/**
	 * Performs double distribution of AND operator over OR operators.
	 * @param node
	 */
	private void performDoubleDistribution(Node node) {
		if(node != null) {
			Node leftNode = node.left;
			Node rightNode = node.right;

			node.value = Operator.AND + "";

			Node leftORNode = new Node(Operator.OR + "");
			if(leftNode != null) {
				leftORNode.left = leftNode.left;
			}

			leftORNode.right = rightNode;

			Node rightORNode = new Node(Operator.OR + "");
			if(leftNode != null) {
				rightORNode.left = leftNode.right;
			}

			rightORNode.right = rightNode;

			node.left = leftORNode;
			node.right = rightORNode;
		}

	}
	
	/**
	 * Moves negation inwards the expression.
	 * @param node the root node of the expression tree.
	 */
	private void moveNegationInwards(Node node) {
		if(node != null) {
			moveNegationInwards(node.left);
			moveNegationInwards(node.right);
			Node leftNode = node.left;
			Node rightNode = node.right;

			if( (leftNode != null) && (leftNode.isNegateOperator()) ) {
				negateTree(leftNode.right);
				node.left = leftNode.right;
			}

			if( (rightNode != null) && (rightNode.isNegateOperator()) ) {
				negateTree(rightNode.right);
				node.right = rightNode.right;
			}
		}
	}
	
	/**
	 * Handles special scenario where the root node is a negation operator.
	 * @param root the root node of the expression tree.
	 * @return the root node of the negated subtree.
	 */
	private Node handleRootNodeNegation(Node root) {
		Node updatedRoot = root;
		if(root != null) {
			if(root.isNegateOperator()) {
				negateTree(root.right);
				updatedRoot = root.right;
			}
		}
		return updatedRoot;
	}

	/**
	 * Negates the operator and predicate nodes of the expression tree rooted at <CODE>node</CODE>.
	 * @param node the root node of the expression tree.
	 */
	private void negateTree(Node node) {
		if(node != null) {

			//negating the current node first
			char nodeValue = node.value.charAt(0);
			if(node.isNegatedPredicate()) {
				node.value = node.value.substring(1, node.value.length());
			}		
			else if(Operator.isOperator(nodeValue)) {
				switch (nodeValue) {

				case Operator.AND		:
					node.value = Operator.OR + "";
					break;

				case Operator.OR		:
					node.value = Operator.AND + "";
					break;
				}
			}
			else {
				node.value = Operator.NEGATE + node.value;
			}

			negateTree(node.left);
			negateTree(node.right);
		}
	}

	/**
	 * Replaces implication operators in the expression tree with the corresponding logic sentence.
	 * @param node the root node of the expression tree.
	 */
	private void replaceImplications(Node node) {
		if(node != null) {
			replaceImplications(node.left);
			replaceImplications(node.right); 
			if(node.isImpliesOperator()) {
				node.value = Operator.OR + "";
				Node negationNode = new Node(Operator.NEGATE + "");
				negationNode.right = node.left;
				node.left = negationNode;
			}
		}
	}
}

/**
 * Class representing the inference engine/agent.
 * @author pachpandenikhil
 *
 */
class Agent {
	private CNFConverter cnfConverter;
	private KnowledgeBase KB;

	public Agent() {
		KB = new KnowledgeBase();
		cnfConverter = new CNFConverter();
	}
	
	/**
	 * Adds the FOL fact/sentence to the knowledge base.
	 * @param fact the FOL fact.
	 * @return true if and only if the fact was successfully added. 
	 */
	public boolean tell(String fact) {
		boolean retVal = false;
		if(fact != null) {
			fact = cnfConverter.convertToCNF(fact);
			retVal = KB.store(fact);
		}
		return retVal;
	}
	
	/**
	 * Returns true if the query is entailed by the knowledge base.
	 * @param query the query to be verified.
	 * @return true if and only if the query is entailed by the knowledge base.
	 */
	public boolean ask(String query) {
		boolean retVal = false;
		if(query != null) {
			List<String> lQuery = new ArrayList<>();
			query = cnfConverter.preProcessInput(query);
			query = negateQuery(query);
			lQuery.add(query);
			
			//adding the negated query to the KB
			tell(query);
			
			//checking if contradiction exists
			List<String> visitedGoals = new ArrayList<>();
			retVal = isContradiction(lQuery, visitedGoals);
			
			//removing the added fact
			KB.remove(query);
		}
		
		return retVal;
	}
	
	/**
	 * Returns the negation of the query.
	 * @param query the query to be negated.
	 * @return the negation of the query.
	 */
	private String negateQuery(String query) {
		String negatedQuery = "";
		if(query != null) {
			if(KB.isNegativePredicate(query)) {
				negatedQuery = query.substring(1, query.length());
			}
			else {
				negatedQuery = Operator.NEGATE + query;
			}
		}
		return negatedQuery;
	}

	/**
	 * Performs inference using Resolution algorithm
	 * @param query Query as a list
	 * @param visitedQueries Sub-queries formed so far
	 * @return true, if contradiction exists for the query
	 */
	private boolean isContradiction(List<String> query, List<String> visitedQueries) {
		boolean isContradiction = false;
		
		if(query.size() == 0) {
			return true;
		}
		
		if(isQueryVisited(query, visitedQueries)) {
			return false;
		}
		else {
			visitedQueries.add(getQuery(query));
		}
		
		if(negationExists(query.get(0))) {
			List<List<String>> unifiers = getUnifiers(query.get(0));
			for(List<String> unifier : unifiers) {
				if(canUnify(query, unifier)) {
					List<String> unification = unify(query, unifier);
					//debugPrint(query, unifier, unification);					
					isContradiction = isContradiction(unification, visitedQueries);
					if(isContradiction) {
						break;
					}
					else {
						visitedQueries.remove(getQuery(unification));
					}
				}
			}
		}
		return isContradiction;
	}
	
	/**
	 * Prints step by step resolution onto the console.
	 * @param query
	 * @param unifier
	 * @param unification
	 */
	private void debugPrint(List<String> query, List<String> unifier, List<String> unification) {
		System.out.println(getQuery(query) + "\t\t" + getQuery(unifier) + "\n");
		System.out.println(getQuery(unification) + "\n");
		System.out.println("-----------------------------------------------------------------------------------------------------\n\n");
		
	}
	
	/**
	 * Returns true if the query <CODE>query</CODE> has already been visited during the process of resolution.
	 * @param query the list representing the query to be verified.
	 * @param visitedQueries the list of queries visited so far.
	 * @return true if and only if the query has already been visited during the process of resolution.
	 */
	private boolean isQueryVisited(List<String> query, List<String> visitedQueries) {
		boolean isVisited = false;
		if( (query != null) && (visitedQueries != null) ) {
			for(String visitedQuery : visitedQueries) {
				if(visitedQuery.equalsIgnoreCase(getQuery(query))) {
					isVisited = true;
					break;
				}
			}
		}
		return isVisited;
	}
	
	/**
	 * Returns the string representation of the query in the list form.
	 * @param lQuery the list containing query predicates.
	 * @return the string representation of the query in the list form.
	 */
	public static String getQuery(List<String> lQuery) {
		String query = "";
		if(lQuery != null) {
			for(String predicate : lQuery) {
				query += predicate;
				query += Operator.OR + "";
			}
			if(query.isEmpty()) {
				query = "<<EMPTY>>";
			}
			else {
				query = query.substring(0, query.length()-1);
			}
			
		}
		return query;
	}
	
	/**
	 * Performs unification of two sentences.
	 * @param lQuery the query on which the unification is to be performed.
	 * @param lUnifier the fact with which the unification is performed.
	 * @return the unified sentence.
	 */
	private List<String> unify(List<String> lQuery, List<String> lUnifier) {
		List<String> unification = null;
		if( (lQuery != null) && (lUnifier != null) ) {
			unification = new ArrayList<>();
			String query = lQuery.get(0);
			String negatedQuery = negateQuery(query);
			//Removing arguments
			negatedQuery = negatedQuery.substring(0, negatedQuery.indexOf(Operator.PREDICATE_LEFT_PARENTHESIS));
			String unifier = null;
			for(String predicate : lUnifier) {
				if(predicate.startsWith(negatedQuery)) {
					unifier = predicate;
					break;
				}
			}
			
			String queryArguments = query.substring(query.indexOf(Operator.PREDICATE_LEFT_PARENTHESIS));
			String unifierArguments = unifier.substring(unifier.indexOf(Operator.PREDICATE_LEFT_PARENTHESIS));
			List<String> queryArgs = KB.getArguments(queryArguments);
			List<String> unifierArgs = KB.getArguments(unifierArguments);
			Map<String, String> substitution = getSubstitution(queryArgs, unifierArgs);
			
			if(substitution != null) {
				unification.addAll(lQuery);
				unification.addAll(lUnifier);
				unification.remove(query);
				unification.remove(unifier);
				
				unification = substitute(unification, substitution);
			}
			
			
		}
		return unification;
	}
	
	/**
	 * Substitutes the variables in the query/fact with the substitution map. 
	 * @param query the query/fact for substitution.
	 * @param substitution the map containing the substitution parameters.
	 * @return the query after substitution.
	 */
	private List<String> substitute(List<String> query, Map<String, String> substitution) {
		List<String> updatedQuery = new ArrayList<>();
		
		if( (query != null) & (substitution != null) ) {
			for(String predicate : query) {
				
				Map<String, String> replacementMap = new HashMap<>();
				String argumentsRegEx = "\\[(.*?)\\]";
				Pattern pattern = Pattern.compile(argumentsRegEx);
				Matcher matcher = pattern.matcher(predicate);

				while (matcher.find()) {
					String group = matcher.group();
					String groupReplacement = Operator.PREDICATE_LEFT_PARENTHESIS + "";
					List<String> args = KB.getArguments(group);
					for(String arg : args) {
						if(substitution.containsKey(arg)) {
							groupReplacement += substitution.get(arg);
							groupReplacement += ",";
						}
						else {
							groupReplacement += arg;
							groupReplacement += ",";
						}
					}
					
					groupReplacement = groupReplacement.substring(0, groupReplacement.length()-1);
					groupReplacement += Operator.PREDICATE_RIGHT_PARENTHESIS + "";
					group = group.replaceAll("\\[", "\\\\[");
					group = group.replaceAll("\\]", "\\\\]");
					
					groupReplacement = groupReplacement.replaceAll("\\[", "\\\\[");
					groupReplacement = groupReplacement.replaceAll("\\]", "\\\\]");
					
					
					replacementMap.put(group, groupReplacement);
				}

				//Replacing all the arguments
				for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
					String originalGroup = entry.getKey();
					String replacementGroup = entry.getValue();
					predicate = predicate.replaceAll(originalGroup, replacementGroup);
				}
				updatedQuery.add(predicate);
			}
		}
		return updatedQuery;
	}

	/**
	 * Determines if the sentence <CODE>lQuery</CODE> can be unified with the sentence <CODE>lUnifier</CODE>.
	 * @param lQuery the sentence for unification.
	 * @param lUnifier the sentence to be unified with.
	 * @return true if and only if the sentence <CODE>lQuery</CODE> can be unified with the sentence <CODE>lUnifier</CODE>.
	 */
	private boolean canUnify(List<String> lQuery, List<String> lUnifier) {
		boolean canUnify = false;
		if( (lQuery != null) && (lUnifier != null) ) {
			String query = lQuery.get(0);
			String negatedQuery = negateQuery(query);
			//Removing arguments
			negatedQuery = negatedQuery.substring(0, negatedQuery.indexOf(Operator.PREDICATE_LEFT_PARENTHESIS));
			String unifier = null;
			for(String predicate : lUnifier) {
				if(predicate.startsWith(negatedQuery)) {
					unifier = predicate;
					break;
				}
			}
			if(unifier != null) {
				//Checking if both the predicates have same number of arguments
				if(isArgumentsCompatible(query, unifier)) {
					canUnify = true;
				}
			}
		}
		return canUnify;
	}
	
	/**
	 * Returns true if the argument types of the query are compatible with the unifier.
	 * @param query the sentence for unification.
	 * @param unifier the sentence to be unified with.
	 * @return true if and only if the argument types of the query are compatible with the unifier.
	 */
	private boolean isArgumentsCompatible(String query, String unifier) {
		boolean isCompatible = false;
		if( (query != null) && (unifier != null) ) {
			
			//Checking if the number of arguments is same for both 
			String queryArguments = query.substring(query.indexOf(Operator.PREDICATE_LEFT_PARENTHESIS));
			String unifierArguments = unifier.substring(unifier.indexOf(Operator.PREDICATE_LEFT_PARENTHESIS));
			List<String> queryArgs = KB.getArguments(queryArguments);
			List<String> unifierArgs = KB.getArguments(unifierArguments);
			if(queryArgs.size() == unifierArgs.size()) {
				
				//Checking if each argument type is compatible with the other
				if(isArgumentTypesCompatible(queryArgs, unifierArgs)) {
					
					//Checking if the number of distinct arguments is same for both
					Set<String> distinctQueryArgs = new HashSet<>(queryArgs);
					Set<String> distinctUnifierArgs = new HashSet<>(unifierArgs);
					if(distinctQueryArgs.size() == distinctUnifierArgs.size()) {
						
						//Checking if successful substitution exists for the argument variables
						if(canSubstitute(queryArgs, unifierArgs)) {
							isCompatible = true;
						}
					}
				}
			}
			
		}
		return isCompatible;
	}
	
	/**
	 * Returns true if the query arguments can be substituted with unifier arguments.  
	 * 
	 * @param queryArgs the list of query arguments.
	 * @param unifierArgs the list of unifier arguments.
	 * @return true if and only if the query arguments can be substituted with unifier arguments.
	 */
	private boolean canSubstitute(List<String> queryArgs, List<String> unifierArgs) {
		boolean canSubstitute = true;
		if( (queryArgs != null) && (unifierArgs != null) ) {
			Map<String, String> substitution = getSubstitution(queryArgs, unifierArgs);
			if(substitution == null) {
				canSubstitute = false;
			}
		}
		return canSubstitute;
	}
	
	/**
	 * Returns the substitution map for query arguments with unifier arguments. 
	 * @param queryArgs the list of query arguments.
	 * @param unifierArgs the list of unifier arguments.
	 * @return the substitution map for query arguments with unifier arguments.
	 */
	private Map<String, String> getSubstitution(List<String> queryArgs, List<String> unifierArgs) {
		Map<String, String> substitution = null;
		if( (queryArgs != null) && (unifierArgs != null) ) { 
			substitution = new LinkedHashMap<>();
			for (int idx = 0; idx < unifierArgs.size(); idx++) {
				String unifierArg = unifierArgs.get(idx);
				String queryArg = queryArgs.get(idx);
				if(!substitution.containsKey(unifierArg)) {
					if(KB.getArgumentType(unifierArg).equalsIgnoreCase(KnowledgeBase.ARG_TYPE_VARIABLE)) {
						substitution.put(unifierArg, queryArg);
					}
					else {
						substitution.put(queryArg, unifierArg);
					}
				}
				else {
					//checking if the previously added substitution is the same as the new value
					String previousSubstituion = substitution.get(unifierArg);
					if(!previousSubstituion.equalsIgnoreCase(queryArg)) {
						substitution = null;
						break;
					}
				}
			}
		}
		
		return substitution;
	}
	
	/**
	 * Determines if the query argument types are compatible with unifier argument types.
	 * @param queryArgs the list of query arguments.
	 * @param unifierArgs the list of unifier arguments.
	 * @return true if and only if the query argument types are compatible with unifier argument types. 
	 */
	private boolean isArgumentTypesCompatible(List<String> queryArgs, List<String> unifierArgs) {
		boolean isCompatible = false;
		if( (queryArgs != null) && (unifierArgs != null) ) {
			isCompatible = true;
			
			for (int idx = 0; idx < queryArgs.size(); idx++) {
				String queryArg = queryArgs.get(idx);
				String unifierArg = unifierArgs.get(idx);
				String queryArgType = KB.getArgumentType(queryArg);
				String unifierArgType = KB.getArgumentType(unifierArg);
				
				if( (queryArgType.equalsIgnoreCase(KnowledgeBase.ARG_TYPE_CONSTANT)) && (unifierArgType.equalsIgnoreCase(KnowledgeBase.ARG_TYPE_CONSTANT)) ) {
					if(!queryArg.equalsIgnoreCase(unifierArg)) {
						isCompatible = false;
						break;
					}
				}
			}
			
		}
		return isCompatible;
	}
	
	/**
	 * Returns the list of possible unifiers for the query.
	 * @param query the query to be unified.
	 * @return the list of possible unifiers.
	 */
	private List<List<String>> getUnifiers(String query) {
		List<List<String>> unifiers = new ArrayList<>();
		if(query != null) {
			String name = KB.getPredicateName(query);
			if(KB.predicateExists(name)) {
				Map<String, List<List<String>>> sentenceMap = KB.getPredicateSentenceMap(name);
				if(sentenceMap != null) {
					if(KB.isNegativePredicate(query)) {
						unifiers = sentenceMap.get(KnowledgeBase.POSITIVE_SENTENCE_KEY);
					}
					else {
						unifiers = sentenceMap.get(KnowledgeBase.NEGATIVE_SENTENCE_KEY);
					}
				}
			}
		}
		return unifiers;
	}
	
	/**
	 * Returns true if the knowledge base contains a sentence with the negation of the query. 
	 * @param query the query whose negation is to be found.
	 * @return true if and only if the knowledge base contains a sentence with the negation of the query.
	 */
	private boolean negationExists(String query) {
		boolean negationExists = false;
		if(query != null) {
			List<List<String>> unifiers = getUnifiers(query);
			if( (unifiers != null) && (unifiers.size() > 0) ) {
				negationExists = true;
			}
		}
		return negationExists;
	}
}

/**
 * Class containing the execution logic.
 * 
 * @author pachpandenikhil
 *
 */
public class homework {

	private List<String> inputFacts;
	private List<String> queries;
	private Agent agent;

	public homework() {
		inputFacts = new ArrayList<>();
		queries = new ArrayList<>();
		agent = new Agent();
	}

	/**
	 * Reads the input parameters as per the guidelines.
	 * @param inputFile the complete path of the input file.
	 */
	private void readInputParameters(String inputFile) {
		if(inputFile != null) {
			BufferedReader br = null;
			File f = null;
			try {
				f = new File(inputFile);
				br = new BufferedReader(new FileReader(f));
				if(br != null) {

					//Reading queries
					int nQueries = Integer.parseInt(br.readLine());
					while(nQueries > 0) {
						queries.add(br.readLine());
						nQueries--;
					}

					//Reading facts
					int nFacts = Integer.parseInt(br.readLine());
					while(nFacts > 0) {
						inputFacts.add(br.readLine());
						nFacts--;
					}

				}
			} catch (FileNotFoundException e) {
				System.err.println("Exception occured while reading input file : " + e.getMessage());
			} catch (NumberFormatException e) {
				System.err.println("Exception occured while reading input file : " + e.getMessage());
			} catch (IOException e) {
				System.err.println("Exception occured while reading input file : " + e.getMessage());
			}
			finally {
				if(br != null) {
					try {
						br.close();
					} catch (IOException e) {
						System.err.println("Exception occured while reading input file : " + e.getMessage());
					}
				}
			}
		}

	}

	/**
	 * Tells the facts/sentences to the inference engine.
	 */
	private void tellFacts() {
		if(inputFacts != null) {
			for(String fact : inputFacts) {
				agent.tell(fact);
			}
		}
	}
	
	/**
	 * Performs the inference for all the queries and generates the results.
	 * @return the list of results.
	 */
	private List<String> execute() {
		List<String> outputLines = null;
		if( (queries != null) && (queries.size() > 0) ) {
			outputLines = new ArrayList<>();
			boolean isQueryEntailed = false;
			for(String query : queries) {
				isQueryEntailed = agent.ask(query);
				outputLines.add(String.valueOf(isQueryEntailed).toUpperCase());
			}
			
		}
		return outputLines;
	}
	
	/**
	 * Writes the result to the output file.
	 * @param outputLines the list of results.
	 * @param outputFilePath the output file path.
	 */
	private static void writeToFile(List<String> outputLines, String outputFilePath) {
		if((outputLines != null) && (outputFilePath != null)) {
			File f = null;
			FileOutputStream fos = null;
			BufferedWriter bw = null;
			try {
				f = new File(outputFilePath);
				fos = new FileOutputStream(f);
				bw = new BufferedWriter(new OutputStreamWriter(fos));
				int noOutputLines = outputLines.size();
				for (int i = 0; i < noOutputLines; i++) {
					bw.write(outputLines.get(i));
					if(i < (noOutputLines - 1))
						bw.newLine();
				}
			} catch (FileNotFoundException e) {
				System.err.println("FileNotFoundException occured. Exception : " + e.getMessage());
			} catch (IOException e) {
				System.err.println("IOException occured. Exception : " + e.getMessage());
			}
			finally {
				if(bw != null ) {
					try {
						bw.close();
					} catch (IOException e) {
						System.err.println("IOException occured. Exception : " + e.getMessage());
					}
				}
				if(fos != null ) {
					try {
						fos.close();
					} catch (IOException e) {
						System.err.println("IOException occured. Exception : " + e.getMessage());
					}
				}
			}
			
		}
	}
	
	/**
	 * Performs execution.
	 * @param args
	 */
	public static void main(String[] args) {

		// Variables for parsing
		String inputFile = "input.txt";
		String outputFile = "output.txt";
		String classPath = homework.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			String decodedClassPath = URLDecoder.decode(classPath, "UTF-8");
			homework hw = new homework(); 
			hw.readInputParameters(decodedClassPath + inputFile);
			hw.tellFacts();
			List<String> outputLines = hw.execute();
			writeToFile(outputLines, decodedClassPath + outputFile);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Exception occured : " + e.getMessage());
		}	
	}
}