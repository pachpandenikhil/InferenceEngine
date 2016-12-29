# InferenceEngine
AI Inference Engine
<br/>
•	Determines the entailment of logic sentences by a knowledge base using the resolution inference algorithm.<br/>
•	Accepts universally quantified first-order logic sentences as the knowledge base.<br/>
•	Implemented table-based indexing for storing the knowledge base.<br/>
•	Avoids infinite loops during inference by keeping track of the visited sub-queries.<br/>
<br/>
<br/>
Core Technology: Java
<br/>
<br/>

# Input Format
<br/>
Input is provided via the file 'input.txt'.<br/>
<br/>
&lt;NQ = NUMBER OF QUERIES&gt;<br/>
&lt;QUERY 1&gt;<br/>
…<br/>
&lt;QUERY NQ&gt;<br/>
&lt;NS = NUMBER OF GIVEN SENTENCES IN THE KNOWLEDGE BASE&gt;<br/>
&lt;SENTENCE 1&gt;<br/>
…<br/>
&lt;SENTENCE NS&gt;<br/>
<br/>
Constraints :<br/>
<br/>
• Each query is a single literal of the form Predicate(Constant) or ~Predicate(Constant).<br/>
• Variables are all single lowercase letters.<br/>
• All predicates (such as Sibling) and constants (such as John) are case-sensitive alphabetical strings that begin with an uppercase letter.<br/>
• Each predicate takes at least one argument. Predicates can take at most 100 arguments. <br/>
• A given predicate name must not appear with different number of arguments.<br/>
• No syntax erros supported in the input.<br/>

# Output Format
<br/>
Output is written to the file 'output.txt'.<br/>
<br/>
&lt;ANSWER 1&gt;<br/>
…<br/>
&lt;ANSWER NQ&gt;<br/>
where<br/>
each answer is either TRUE if the corresponding query sentence is true given the knowledge base, or FALSE otherwise.
<br/>
<br/>
