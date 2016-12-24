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

	public static boolean isOperator(char ch) {

		boolean retVal = false;
		switch (ch) {

		case Operator.PLUS		: 
		case Operator.MINUS		:
		case Operator.MULTIPLY	: 
		case Operator.DIVIDE	:
		case Operator.NEGATE	:
		case Operator.AND		:
		case Operator.OR		:
		case Operator.EQUALS	:
		case Operator.LEFT_PARENTHESIS: 
		case Operator.RIGHT_PARENTHESIS: 
			retVal = true; 
			break;

		default: 
			retVal = false; 
			break;
		}

		return retVal;
	}

}

class InfixToPostfix {

	private Stack<Character> stack;
	private String postfix;

	public InfixToPostfix() {
		stack = new Stack<>();
		postfix = "";
	}

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
				gotParenthesis(ch); 
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

	private void gotParenthesis(char ch){ 
		while (!stack.isEmpty()) {
			char chx = stack.pop();
			if (chx == Operator.LEFT_PARENTHESIS) 
				break; 
			else
				postfix = postfix + chx; 
		}
	}
}

class Node {

	String value;
	Node left, right;

	Node(String item) {
		value = item;
		left = right = null;
	}

	public boolean isOrOperator() {
		return isOperatorNode(this, Operator.OR);
	}

	public boolean isAndOperator() {
		return isOperatorNode(this, Operator.AND);
	}

	public boolean isNegateOperator() {
		return isOperatorNode(this, Operator.NEGATE);
	}

	public boolean isImpliesOperator() {
		return isOperatorNode(this, Operator.EQUALS);
	}

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

class ExpressionTree {
	private String inorderRepresentation;

	public String getInorderRepresentation(Node node) {
		inorderRepresentation = "";
		inorder(node);
		return inorderRepresentation;
	}

	private void inorder(Node node) {
		if (node != null) {
			inorder(node.left);
			inorderRepresentation += node.value;
			inorder(node.right);
		}
	}

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

	private void addFact(String predicate, String sentenceKey, List<String> predicates) {
		if((predicate != null) && (sentenceKey != null) && (predicates != null) ) {
			if(KB.containsKey(predicate)) {
				Map<String, List<List<String>>> sentenceMap = KB.get(predicate);
				List<List<String>> sentenceList = sentenceMap.get(sentenceKey);
				List<String> sentence = new ArrayList<>(predicates);
				sentenceList.add(sentence);
				sentenceMap.put(sentenceKey, sentenceList);
				KB.put(predicate, sentenceMap);
			}
			else {							
				List<String> sentence = new ArrayList<>(predicates);
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

	private String sequenceGenerator(int i) {
        return i < 0 ? "" : sequenceGenerator((i / 26) - 1) + (char)(97 + i % 26);
    }
	
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

	private boolean isConstant(String arg) {
		boolean retVal = false;
		if(arg != null) {
			if((Character.isUpperCase(arg.charAt(0))) && (arg.length() > 1)) {
				retVal = true;
			}
		}
		return retVal;
	}

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

	public boolean isNegativePredicate(String predicate) {
		boolean retVal = false;
		if(predicate != null) {
			if(predicate.charAt(0) == Operator.NEGATE) {
				retVal = true;
			}
		}
		return retVal;
	}

	public boolean predicateExists(String predicate) {
		boolean exists = false;
		if(predicate != null) {
			exists = KB.containsKey(predicate);
		}
		return exists;
	}

	public Map<String, List<List<String>>> getPredicateSentenceMap(String predicate) {
		Map<String, List<List<String>>> sentenceMap = null;
		if(predicate != null) {
			sentenceMap = KB.get(predicate);
		}
		return sentenceMap;
	}
	
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

	public boolean remove(String query) {
		boolean retVal = false;
		if(query != null) {
			String predicateName = getPredicateName(query);
			if(isNegativePredicate(query)) {
				removeFact(predicateName, NEGATIVE_SENTENCE_KEY, query);	
			}
			else {
				removeFact(predicateName, POSITIVE_SENTENCE_KEY, query);
			}
			retVal = true;
		}
		return retVal;		
	}

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

class CNFConverter {

	private ExpressionTree tree;
	private InfixToPostfix converter;


	public CNFConverter() {
		tree = new ExpressionTree();
		converter = new InfixToPostfix();
	}


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

	public String preProcessInput(String input) {

		//trimming all the whitespace
		input = input.trim().replaceAll(" +", "");

		//replacing '=>' with '='
		input = input.replaceAll(Operator.IMPLIES, Operator.EQUALS + "");

		//replacing predicate parenthesis with '[' and ']'		
		input = replacePredicateParanthesis(input);

		return input;

	}

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

class Agent {
	private CNFConverter cnfConverter;
	private KnowledgeBase KB;

	public Agent() {
		KB = new KnowledgeBase();
		cnfConverter = new CNFConverter();
	}

	public boolean tell(String fact) {
		boolean retVal = false;
		if(fact != null) {
			fact = cnfConverter.convertToCNF(fact);
			retVal = KB.store(fact);
		}
		return retVal;
	}

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
	
	private void debugPrint(List<String> query, List<String> unifier, List<String> unification) {
		System.out.println(getQuery(query) + "\t\t" + getQuery(unifier) + "\n");
		System.out.println(getQuery(unification) + "\n");
		System.out.println("-----------------------------------------------------------------------------------------------------\n\n");
		
	}

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
public class homework {

	private List<String> inputFacts;
	private List<String> queries;
	private Agent agent;

	public homework() {
		inputFacts = new ArrayList<>();
		queries = new ArrayList<>();
		agent = new Agent();
	}


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


	private void tellFacts() {
		if(inputFacts != null) {
			for(String fact : inputFacts) {
				agent.tell(fact);
			}
		}
	}

	private List<String> getOutput() {
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
			List<String> outputLines = hw.getOutput();
			writeToFile(outputLines, decodedClassPath + outputFile);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Exception occured : " + e.getMessage());
		}	
	}
}