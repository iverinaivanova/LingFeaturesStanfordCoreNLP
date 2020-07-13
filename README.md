## LingFeaturesStanfordCoreNLP

### Overview of the project
LingFeaturesStanfordCoreNLP contain a java scripts (**src** folder) that parses multiple articles in Compulational Linguistics in xml form (the files can be accessed from the **all_Ks** folder. Source: ACL Anthology Reference Corpus), and uses the Stanford CoreNLP Module to output coreference analysis per document (mean number of coreference chains), as well as text length, sentence count, number of self mentions, 

### How to run the scripts and what the scripts do
**1. Things to consider before you run the scripts:**
- When you open the MycoreNLPDemo project in your editor, make sure that the subbranch called **Libraries** in not empty (i.e. it contains Stanford CoreNLP libraries that the scripts require to run the CoreNLP annotators).If the Libraries directory is empty, do the following:
- Download Stanford CoreNLP https://stanfordnlp.github.io/CoreNLP/download.html and unarchive it.
- Then go back to the **Libraries** subbranch, select it, and right click on it. Then select **Add JAR/Folder**, search for the corenlp package that you've already unarchived and load all libraries from the folder.

**2. What the scripts do...:**
- **SentimentAnalysis.java** - the script iterates over the files stored in the all_Ks directory, gets the first element by tagName, and the sentence count per text. Then iterates over each sentence in a text and analyses its sentiment. The positive, negative, and neutral sentiment values are stored in 3 different listArrays. Then the number of occurrences in each list is counted and divided by the number of sentences per text. The script prints out the average number of negative, positive, and neutral sentences in a document.
- **meanSelfMentions.java** - the script prints out the number of selfMention occurrences (i.e. we, our, us) per document and then divides their number by the number of NPs, and outputs the mean number of selfMentions in the document.
- **countNPs.java** - the script uses the Tree class to parse the constituents of each sentence. Then it iterates over each node and gets the subtree whose label value is "NP". It prints out the mean number of NP occurrences per number of sentences.
- **sentenceComplexity.java** - the script prints out the mean number of words per sentence.
- **embeddedPPsInNPs.java** - the script uses the Tree class to parse the constituents of each sentence. Then it iterates over each node and gets the subtree whose label value is "NP". Then it iterates over all NPs and gets the subtrees whose label value is "PP". It calculates the mean number of PP modifiers per NP.
- **embeddedClausesInNPs.java** - the script uses the Tree class to parse the constituents of each sentence. Then it iterates over each node and gets the subtree whose label value is "NP". Then it iterates over all NPs and gets the subtrees whose label value is "SBAR" (finite and non-finite clauses). It calculates the mean number of Clause modifiers per NP.
- **hedgingMD.java** - One way of marking hedging is by means of MD (Modal Auxiliary Verbs). The script, therefore, calculates the mean number of MD per number of sentences.
  
