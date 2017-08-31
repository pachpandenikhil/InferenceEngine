# Inference Engine
AI Inference Engine

- Determines the entailment of logic sentences by a knowledge base using the resolution inference algorithm.
- Accepts universally quantified first-order logic sentences as the knowledge base.
- Implemented table-based indexing for storing the knowledge base.
- Avoids infinite loops during inference by keeping track of the visited sub-queries.

Core Technology: Java

# Input Format

Input is provided via the file *input.txt*.
```
<NQ = NUMBER OF QUERIES>
<QUERY 1>
…
<QUERY NQ>
<NS = NUMBER OF GIVEN SENTENCES IN THE KNOWLEDGE BASE>
<SENTENCE 1>
…
<SENTENCE NS>
```
*Constraints* :
- Each query is a single literal of the form Predicate(Constant) or ~Predicate(Constant).
- Variables are all single lowercase letters.
- All predicates (such as Sibling) and constants (such as John) are case-sensitive alphabetical strings that begin with an uppercase letter.
- Each predicate takes at least one argument. Predicates can take at most 100 arguments.
- A given predicate name must not appear with different number of arguments.
- No syntax erros supported in the input.

# Output Format
Output is written to the file *output.txt*.
```
<ANSWER 1>
…
<ANSWER NQ>
```
where each answer is either *TRUE* if the corresponding query sentence is true given the knowledge base, or *FALSE* otherwise.
